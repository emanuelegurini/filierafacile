package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaProdottoRequest;
import com.filiera.facile.dto.response.ProdottoResponse;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.interfaces.ProdottoService;
import com.filiera.facile.repositories.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoService prodottoService;
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public ProdottoController(ProdottoService prodottoService, ProdottoRepository prodottoRepository) {
        this.prodottoService = prodottoService;
        this.prodottoRepository = prodottoRepository;
    }

    @PostMapping
    public ResponseEntity<ProdottoResponse> creaProdotto(
            @Valid @RequestBody CreaProdottoRequest request) {

        try {
            // Usiamo il metodo semplificato senza autenticazione
            DefaultProdotto prodottoCreato = ((com.filiera.facile.application.services.DefaultProdottoService) prodottoService)
                .creaNuovoProdotto(request.getAziendaId(), request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ProdottoResponse(prodottoCreato));
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del prodotto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdotto(@PathVariable Long id) {
        return prodottoRepository.findById(id)
                .map(prodotto -> ResponseEntity.ok(new ProdottoResponse(prodotto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<java.util.List<ProdottoResponse>> getAllProdotti() {
        java.util.List<ProdottoResponse> prodotti = prodottoRepository.findAll().stream()
                .map(ProdottoResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(prodotti);
    }

    // TODO: I seguenti endpoints dipendono da metodi repository non ancora implementati
    // @GetMapping("/azienda/{aziendaId}")
    // @GetMapping("/categoria/{categoria}")
    // @GetMapping("/search")
    // @PutMapping("/{id}")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaProdotto(@PathVariable Long id) {
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