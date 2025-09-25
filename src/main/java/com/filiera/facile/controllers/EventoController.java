package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaEventoRequest;
import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.model.interfaces.EventoService;
import com.filiera.facile.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;

    @Autowired
    public EventoController(EventoService eventoService, EventoRepository eventoRepository) {
        this.eventoService = eventoService;
        this.eventoRepository = eventoRepository;
    }

    @PostMapping
    public ResponseEntity<DefaultEvento> creaEvento(
            @Valid @RequestBody CreaEventoRequest request,
            @RequestParam Long organizzatoreId) {

        // TODO: Implementare creazione completa una volta rifattorizzato il service
        // Il costruttore di DefaultEvento richiede DefaultUtente organizzatore che deve essere caricato dal DB
        // Per ora restituiamo un errore controllato
        throw new UnsupportedOperationException("Creazione eventi non ancora supportata - in fase di sviluppo");
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

    @GetMapping("/{id}")
    public ResponseEntity<DefaultEvento> getEvento(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .map(evento -> ResponseEntity.ok(evento))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DefaultEvento>> getAllEventi() {
        List<DefaultEvento> eventi = eventoRepository.findAll();
        return ResponseEntity.ok(eventi);
    }

    // TODO: implementare il controller per prendere tutti gli eventi di un determinato organizzatore
    // @GetMapping("/organizzatore/{organizzatoreId}")

    @PutMapping("/{id}")
    public ResponseEntity<DefaultEvento> aggiornaEvento(
            @PathVariable Long id,
            @Valid @RequestBody CreaEventoRequest request) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    // Update basic fields that are safe to update
                    if (request.getDescrizione() != null) {
                        evento.setDescrizione(request.getDescrizione());
                    }
                    // Note: Other fields like date, location need more careful validation

                    DefaultEvento eventoAggiornato = eventoRepository.save(evento);
                    return ResponseEntity.ok(eventoAggiornato);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) {
        try {
            if (eventoRepository.existsById(id)) {
                eventoRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'evento: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/posti-disponibili")
    public ResponseEntity<Integer> getPostiDisponibili(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .map(evento -> ResponseEntity.ok(evento.getPostiDisponibili()))
                .orElse(ResponseEntity.notFound().build());
    }

}