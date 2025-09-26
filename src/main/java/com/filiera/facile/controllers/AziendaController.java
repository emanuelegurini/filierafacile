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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aziende")
@Tag(name = "Aziende", description = "API per la gestione delle aziende")
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
    @Operation(summary = "Crea una nuova azienda", description = "Crea una nuova azienda con i dettagli forniti")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Azienda creata con successo"),
        @ApiResponse(responseCode = "400", description = "Dati richiesta non validi"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<AziendaResponse> creaAzienda(
            @Valid @RequestBody @Parameter(description = "Dati per la creazione dell'azienda") CreaAziendaRequest request) {
        try {
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
    @Operation(summary = "Dettagli azienda", description = "Recupera i dettagli di un'azienda specifica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Azienda trovata"),
        @ApiResponse(responseCode = "404", description = "Azienda non trovata")
    })
    public ResponseEntity<AziendaResponse> getAzienda(
            @PathVariable @Parameter(description = "ID dell'azienda") Long id) {
        return aziendaRepository.findById(id)
                .map(azienda -> ResponseEntity.ok(new AziendaResponse(azienda)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Lista tutte le aziende", description = "Recupera la lista di tutte le aziende registrate")
    @ApiResponse(responseCode = "200", description = "Lista aziende recuperata con successo")
    public ResponseEntity<List<AziendaResponse>> getAllAziende() {
        List<AziendaResponse> aziende = aziendaRepository.findAll().stream()
                .map(AziendaResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(aziende);
    }

    // TODO: implementare l'endpoint per ottenere aziende in base al tipo
    // @GetMapping("/tipo/{tipo}"

    @PutMapping("/{id}")
    public ResponseEntity<AziendaResponse> aggiornaAzienda(
            @PathVariable Long id,
            @Valid @RequestBody CreaAziendaRequest request) {
        try {
            return aziendaRepository.findById(id)
                    .map(azienda -> {
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