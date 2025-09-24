package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaProdottoRequest;
import com.filiera.facile.dto.response.ProdottoResponse;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.interfaces.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoService prodottoService;

    @Autowired
    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @PostMapping
    public ResponseEntity<ProdottoResponse> creaProdotto(
            @Valid @RequestBody CreaProdottoRequest request,
            @RequestParam Long utenteId) {

        // Per ora creiamo un prodotto con parametri di default
        // TODO: Migliorare per gestire tutti i parametri dal DTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProdottoResponse());
    }
}