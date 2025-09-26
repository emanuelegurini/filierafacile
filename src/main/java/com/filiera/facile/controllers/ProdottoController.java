package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaProdottoRequest;
import com.filiera.facile.dto.response.ProdottoResponse;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.ProdottoService;
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

@RestController
@RequestMapping("/api/prodotti")
@Tag(name = "Prodotti", description = "API per la gestione dei prodotti")
public class ProdottoController {

    private final ProdottoService prodottoService;
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public ProdottoController(ProdottoService prodottoService, ProdottoRepository prodottoRepository) {
        this.prodottoService = prodottoService;
        this.prodottoRepository = prodottoRepository;
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo prodotto", description = "Crea un nuovo prodotto per un'azienda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prodotto creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati richiesta non validi"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<ProdottoResponse> creaProdotto(
            @Valid @RequestBody @Parameter(description = "Dati per la creazione del prodotto") CreaProdottoRequest request) {

        try {
            DefaultProdotto prodottoCreato = ((com.filiera.facile.application.services.DefaultProdottoService) prodottoService)
                .creaNuovoProdotto(request.getAziendaId(), request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ProdottoResponse(prodottoCreato));
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del prodotto: " + e.getMessage());
        }
    }

    @GetMapping("/approvati")
    @Operation(summary = "Lista prodotti approvati", description = "Recupera solo i prodotti con stato APPROVATO")
    @ApiResponse(responseCode = "200", description = "Lista prodotti approvati recuperata con successo")
    public ResponseEntity<java.util.List<ProdottoResponse>> getProdottiApprovati() {
        java.util.List<ProdottoResponse> prodottiApprovati = prodottoRepository.findAll().stream()
                .filter(prodotto -> prodotto.getStatoValidazione() == StatoValidazione.APPROVATO)
                .map(ProdottoResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(prodottiApprovati);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dettagli prodotto", description = "Recupera i dettagli di un prodotto specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prodotto trovato"),
        @ApiResponse(responseCode = "404", description = "Prodotto non trovato")
    })
    public ResponseEntity<ProdottoResponse> getProdotto(
            @PathVariable @Parameter(description = "ID del prodotto") Long id) {
        return prodottoRepository.findById(id)
                .map(prodotto -> ResponseEntity.ok(new ProdottoResponse(prodotto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Lista tutti i prodotti", description = "Recupera la lista di tutti i prodotti disponibili")
    @ApiResponse(responseCode = "200", description = "Lista prodotti recuperata con successo")
    public ResponseEntity<java.util.List<ProdottoResponse>> getAllProdotti() {
        java.util.List<ProdottoResponse> prodotti = prodottoRepository.findAll().stream()
                .map(ProdottoResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(prodotti);
    }

    // TODO: Mancano da implementare tutti questi endpoint?!!!!
    // @GetMapping("/azienda/{aziendaId}")
    // @GetMapping("/categoria/{categoria}")
    // @PutMapping("/{id}")

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina prodotto", description = "Elimina un prodotto dal sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Prodotto eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Prodotto non trovato")
    })
    public ResponseEntity<Void> eliminaProdotto(
            @PathVariable @Parameter(description = "ID del prodotto da eliminare") Long id) {
        try {
            if (prodottoRepository.existsById(id)) {
                prodottoRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione del prodotto: " + e.getMessage());
        }
    }

    // TODO: Endpoint per tipo prodotto - dipende da repository method non implementato
    // @GetMapping("/tipo/{tipo}")
}