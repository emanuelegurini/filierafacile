package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaAziendaRequest;
import com.filiera.facile.dto.response.AziendaResponse;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultCoordinate;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.enums.TipoAzienda;
import com.filiera.facile.model.interfaces.AziendaService;
import com.filiera.facile.repositories.AziendaRepository;
import com.filiera.facile.repositories.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aziende")
public class AziendaController {

    private final AziendaService aziendaService;
    private final AziendaRepository aziendaRepository;
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public AziendaController(AziendaService aziendaService, AziendaRepository aziendaRepository, ProdottoRepository prodottoRepository) {
        this.aziendaService = aziendaService;
        this.aziendaRepository = aziendaRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @PostMapping
    public ResponseEntity<AziendaResponse> creaAzienda(@Valid @RequestBody CreaAziendaRequest request) {
        try {
            // Crea coordinate con valori forniti o default
            Float lat = request.getLatitudine() != null ? request.getLatitudine() : 0.0f;
            Float lon = request.getLongitudine() != null ? request.getLongitudine() : 0.0f;
            DefaultCoordinate coordinate = new DefaultCoordinate(lat, lon);

            DefaultAzienda nuovaAzienda = new DefaultAzienda(
                request.getRagioneSociale(),
                request.getPartitaIva(),
                request.getIndirizzo(),
                request.getEmail(),
                request.getNumeroTelefono(),
                request.getSitoWeb() != null ? request.getSitoWeb() : "",
                coordinate
            );

            DefaultAzienda aziendaCreata = aziendaService.creaNuovaAzienda(nuovaAzienda);

            // Aggiungi i tipi azienda
            if (request.getTipiAzienda() != null) {
                for (TipoAzienda tipo : request.getTipiAzienda()) {
                    aziendaService.aggiungiTipoAzienda(aziendaCreata.getId(), tipo);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AziendaResponse(aziendaCreata));
        } catch (Exception e) {
            // Il GlobalExceptionHandler gestirà l'eccezione
            throw new RuntimeException("Errore durante la creazione dell'azienda: " + e.getMessage());
        }
    }

    @PostMapping("/{aziendaId}/tipi")
    public ResponseEntity<Void> aggiungiTipoAzienda(
            @PathVariable Long aziendaId,
            @RequestParam TipoAzienda tipo) {

        aziendaService.aggiungiTipoAzienda(aziendaId, tipo);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AziendaResponse> getAzienda(@PathVariable Long id) {
        return aziendaRepository.findById(id)
                .map(azienda -> ResponseEntity.ok(new AziendaResponse(azienda)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AziendaResponse>> getAllAziende() {
        List<AziendaResponse> aziende = aziendaRepository.findAll().stream()
                .map(AziendaResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(aziende);
    }

    // TODO: Endpoint per tipo azienda - dipende da repository method non implementato
    // @GetMapping("/tipo/{tipo}")

    // TODO: Endpoint ricerca aziende - dipende da repository method non implementato
    // @GetMapping("/search")

    @PutMapping("/{id}")
    public ResponseEntity<AziendaResponse> aggiornaAzienda(
            @PathVariable Long id,
            @Valid @RequestBody CreaAziendaRequest request) {
        try {
            return aziendaRepository.findById(id)
                    .map(azienda -> {
                        // Update fields
                        azienda.setRagioneSociale(request.getRagioneSociale());
                        azienda.setEmail(request.getEmail());
                        azienda.setNumeroTelefono(request.getNumeroTelefono());
                        azienda.setIndirizzo(request.getIndirizzo());
                        if (request.getSitoWeb() != null) {
                            azienda.setSitoWeb(request.getSitoWeb());
                        }

                        DefaultAzienda aziendaAggiornata = aziendaRepository.save(azienda);
                        return ResponseEntity.ok(new AziendaResponse(aziendaAggiornata));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'azienda: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaAzienda(@PathVariable Long id) {
        try {
            if (aziendaRepository.existsById(id)) {
                aziendaRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'azienda: " + e.getMessage());
        }
    }

    @DeleteMapping("/{aziendaId}/tipi")
    public ResponseEntity<Void> rimuoviTipoAzienda(
            @PathVariable Long aziendaId,
            @RequestParam TipoAzienda tipo) {

        try {
            // Cast to concrete class to access rimuoviTipoAzienda method
            if (aziendaService instanceof com.filiera.facile.application.services.DefaultAziendaService defaultService) {
                defaultService.rimuoviTipoAzienda(aziendaId, tipo);
                return ResponseEntity.ok().build();
            }
            throw new RuntimeException("Servizio azienda non supportato");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la rimozione del tipo azienda: " + e.getMessage());
        }
    }

    @GetMapping("/partita-iva/{partitaIva}")
    public ResponseEntity<AziendaResponse> getAziendaPerPartitaIva(@PathVariable String partitaIva) {
        return aziendaRepository.findByPartitaIva(partitaIva)
                .map(azienda -> ResponseEntity.ok(new AziendaResponse(azienda)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/magazzino")
    public ResponseEntity<Map<String, Integer>> getMagazzino(@PathVariable Long id) {
        return aziendaRepository.findById(id)
                .map(azienda -> {
                    // This would need proper implementation in the entity
                    // For now return empty map
                    Map<String, Integer> magazzino = new java.util.HashMap<>();
                    return ResponseEntity.ok(magazzino);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{aziendaId}/magazzino")
    public ResponseEntity<Void> aggiungiScorte(
            @PathVariable Long aziendaId,
            @RequestParam Long prodottoId,
            @RequestParam int quantita) {
        try {
            DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                    .orElseThrow(() -> new RuntimeException("Azienda non trovata"));

            DefaultProdotto prodotto = prodottoRepository.findById(prodottoId)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            azienda.aggiungiScorte(prodotto, quantita);
            aziendaRepository.save(azienda);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiunta delle scorte: " + e.getMessage());
        }
    }
}