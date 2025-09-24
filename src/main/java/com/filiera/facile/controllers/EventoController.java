package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaEventoRequest;
import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.model.interfaces.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<DefaultEvento> creaEvento(
            @Valid @RequestBody CreaEventoRequest request,
            @RequestParam Long organizzatoreId) {

        // TODO: Implementare creazione evento con parametri completi dal DTO
        // Per ora ritorniamo un placeholder per la compilazione
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/{eventoId}/aziende/{aziendaId}")
    public ResponseEntity<Void> aggiungiAzienda(
            @PathVariable Long eventoId,
            @PathVariable Long aziendaId) {

        eventoService.aggiungiAziendaAdEvento(eventoId, aziendaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventoId}/aziende/{aziendaId}")
    public ResponseEntity<Void> rimuoviAzienda(
            @PathVariable Long eventoId,
            @PathVariable Long aziendaId) {

        eventoService.rimuoviAziendaDaEvento(eventoId, aziendaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/azienda/{aziendaId}")
    public ResponseEntity<List<DefaultEvento>> getEventiPerAzienda(@PathVariable Long aziendaId) {
        List<DefaultEvento> eventi = eventoService.trovaEventiPerAzienda(aziendaId);
        return ResponseEntity.ok(eventi);
    }
}