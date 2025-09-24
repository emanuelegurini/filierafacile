package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaAziendaRequest;
import com.filiera.facile.dto.response.AziendaResponse;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;
import com.filiera.facile.model.interfaces.AziendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/aziende")
public class AziendaController {

    private final AziendaService aziendaService;

    @Autowired
    public AziendaController(AziendaService aziendaService) {
        this.aziendaService = aziendaService;
    }

    @PostMapping
    public ResponseEntity<AziendaResponse> creaAzienda(@Valid @RequestBody CreaAziendaRequest request) {
        // TODO: Implementare creazione azienda utilizzando il service corretto
        // Per ora placeholder per la compilazione
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AziendaResponse());
    }

    @PostMapping("/{aziendaId}/tipi")
    public ResponseEntity<Void> aggiungiTipoAzienda(
            @PathVariable Long aziendaId,
            @RequestParam TipoAzienda tipo) {

        aziendaService.aggiungiTipoAzienda(aziendaId, tipo);

        return ResponseEntity.ok().build();
    }
}