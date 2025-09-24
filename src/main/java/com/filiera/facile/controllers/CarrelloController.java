package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.AggiungiAlCarrelloRequest;
import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.model.interfaces.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    private final CarrelloService carrelloService;

    @Autowired
    public CarrelloController(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    @GetMapping("/{utenteId}")
    public ResponseEntity<DefaultCarrello> getCarrello(@PathVariable Long utenteId) {
        DefaultCarrello carrello = carrelloService.getCarrelloPerUtente(utenteId);
        return ResponseEntity.ok(carrello);
    }

    @PostMapping("/{utenteId}/articoli")
    public ResponseEntity<Void> aggiungiArticolo(
            @PathVariable Long utenteId,
            @Valid @RequestBody AggiungiAlCarrelloRequest request) {

        carrelloService.aggiungiArticoloAlCarrello(
            utenteId,
            request.getArticoloId(),
            request.getQuantita()
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{utenteId}/articoli/{articoloId}")
    public ResponseEntity<Void> rimuoviArticolo(
            @PathVariable Long utenteId,
            @PathVariable Long articoloId) {

        carrelloService.rimuoviArticoloDalCarrello(utenteId, articoloId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{utenteId}")
    public ResponseEntity<Void> svuotaCarrello(@PathVariable Long utenteId) {
        carrelloService.svuotaCarrello(utenteId);
        return ResponseEntity.ok().build();
    }
}