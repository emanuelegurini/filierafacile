package com.filiera.facile.controllers;

import com.filiera.facile.application.services.DefaultPacchettoProdottiService;
import com.filiera.facile.dto.request.AggiungiProdottoPacchettoRequest;
import com.filiera.facile.dto.request.CreaPacchettoRequest;
import com.filiera.facile.dto.response.PacchettoResponse;
import com.filiera.facile.entities.DefaultPacchettoProdotti;
import com.filiera.facile.repositories.PacchettoProdottiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacchetti")
public class PacchettoProdottiController {

    private final DefaultPacchettoProdottiService pacchettoService;
    private final PacchettoProdottiRepository pacchettoRepository;

    @Autowired
    public PacchettoProdottiController(DefaultPacchettoProdottiService pacchettoService,
                                       PacchettoProdottiRepository pacchettoRepository) {
        this.pacchettoService = pacchettoService;
        this.pacchettoRepository = pacchettoRepository;
    }

    @PostMapping
    public ResponseEntity<PacchettoResponse> creaPacchetto(@Valid @RequestBody CreaPacchettoRequest request) {
        try {
            DefaultPacchettoProdotti pacchetto = pacchettoService.creaNuovoPacchetto(
                    request.getUtenteId(),
                    request.getAziendaId(),
                    request.getNome(),
                    request.getDescrizione()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PacchettoResponse(pacchetto));
        } catch (SecurityException e) {
            throw new RuntimeException("Accesso negato: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Risorsa non trovata: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del pacchetto: " + e.getMessage());
        }
    }

    @PostMapping("/{pacchettoId}/prodotti")
    public ResponseEntity<PacchettoResponse> aggiungiProdotto(
            @PathVariable Long pacchettoId,
            @Valid @RequestBody AggiungiProdottoPacchettoRequest request) {

        try {
            DefaultPacchettoProdotti pacchettoAggiornato = pacchettoService.aggiungiProdottoAlPacchetto(
                    request.getUtenteId(),
                    pacchettoId,
                    request.getProdottoId(),
                    request.getQuantita()
            );

            return ResponseEntity.ok(new PacchettoResponse(pacchettoAggiornato));
        } catch (SecurityException e) {
            throw new RuntimeException("Accesso negato: " + e.getMessage());
        } catch (com.filiera.facile.utils.ScorteInsufficientiException e) {
            throw new RuntimeException("Scorte insufficienti: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Risorsa non trovata: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiunta del prodotto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{pacchettoId}/prodotti/{prodottoId}")
    public ResponseEntity<PacchettoResponse> rimuoviProdotto(
            @PathVariable Long pacchettoId,
            @PathVariable Long prodottoId,
            @RequestParam Long utenteId) {

        try {
            DefaultPacchettoProdotti pacchettoAggiornato = pacchettoService.rimuoviProdottoDalPacchetto(
                    utenteId,
                    pacchettoId,
                    prodottoId
            );

            return ResponseEntity.ok(new PacchettoResponse(pacchettoAggiornato));
        } catch (SecurityException e) {
            throw new RuntimeException("Accesso negato: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Risorsa non trovata: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la rimozione del prodotto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacchettoResponse> getPacchetto(@PathVariable Long id) {
        return pacchettoRepository.findById(id)
                .map(pacchetto -> ResponseEntity.ok(new PacchettoResponse(pacchetto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PacchettoResponse>> getAllPacchetti() {
        List<PacchettoResponse> pacchetti = pacchettoRepository.findAll().stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacchetti);
    }

    // TODO: implementare l'endpoint per ottenere tutti pacchetti in base all'azienda
    // @GetMapping("/azienda/{aziendaId}")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaPacchetto(
            @PathVariable Long id,
            @RequestParam Long utenteId) {

        try {
            // TODO: terminare di implementare questa delete, in base all'id dei pacchetti
            DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + id));

            pacchettoRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Risorsa non trovata: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione del pacchetto: " + e.getMessage());
        }
    }
}