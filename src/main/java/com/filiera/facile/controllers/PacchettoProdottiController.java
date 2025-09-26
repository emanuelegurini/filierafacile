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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacchetti")
@Tag(name = "Pacchetti Prodotti", description = "API per la gestione dei pacchetti di prodotti")
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
    @Operation(summary = "Crea un nuovo pacchetto prodotti", description = "Crea un nuovo pacchetto di prodotti con i dettagli forniti")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pacchetto creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati richiesta non validi"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<PacchettoResponse> creaPacchetto(
            @Valid @RequestBody @Parameter(description = "Dati per la creazione del pacchetto") CreaPacchettoRequest request) {
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
    @Operation(summary = "Aggiungi prodotto al pacchetto", description = "Aggiunge un prodotto con quantità specificata al pacchetto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prodotto aggiunto con successo"),
        @ApiResponse(responseCode = "404", description = "Pacchetto o prodotto non trovato"),
        @ApiResponse(responseCode = "400", description = "Scorte insufficienti o dati non validi")
    })
    public ResponseEntity<PacchettoResponse> aggiungiProdotto(
            @PathVariable @Parameter(description = "ID del pacchetto") Long pacchettoId,
            @Valid @RequestBody @Parameter(description = "Dati del prodotto da aggiungere") AggiungiProdottoPacchettoRequest request) {

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
    @Operation(summary = "Rimuovi prodotto dal pacchetto", description = "Rimuove un prodotto dal pacchetto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prodotto rimosso con successo"),
        @ApiResponse(responseCode = "404", description = "Pacchetto o prodotto non trovato")
    })
    public ResponseEntity<PacchettoResponse> rimuoviProdotto(
            @PathVariable @Parameter(description = "ID del pacchetto") Long pacchettoId,
            @PathVariable @Parameter(description = "ID del prodotto da rimuovere") Long prodottoId,
            @RequestParam @Parameter(description = "ID dell'utente") Long utenteId) {

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
    @Operation(summary = "Dettagli pacchetto", description = "Recupera i dettagli di un pacchetto specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pacchetto trovato"),
        @ApiResponse(responseCode = "404", description = "Pacchetto non trovato")
    })
    public ResponseEntity<PacchettoResponse> getPacchetto(
            @PathVariable @Parameter(description = "ID del pacchetto") Long id) {
        return pacchettoRepository.findById(id)
                .map(pacchetto -> ResponseEntity.ok(new PacchettoResponse(pacchetto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Lista tutti i pacchetti", description = "Recupera la lista di tutti i pacchetti prodotti")
    @ApiResponse(responseCode = "200", description = "Lista pacchetti recuperata con successo")
    public ResponseEntity<List<PacchettoResponse>> getAllPacchetti() {
        List<PacchettoResponse> pacchetti = pacchettoRepository.findAll().stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacchetti);
    }

    // TODO: implementare l'endpoint per ottenere tutti pacchetti in base all'azienda
    // @GetMapping("/azienda/{aziendaId}")

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina pacchetto", description = "Elimina un pacchetto dal sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pacchetto eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Pacchetto non trovato")
    })
    public ResponseEntity<Void> eliminaPacchetto(
            @PathVariable @Parameter(description = "ID del pacchetto da eliminare") Long id,
            @RequestParam @Parameter(description = "ID dell'utente") Long utenteId) {

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