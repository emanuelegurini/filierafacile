package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.AcquistaBigliettoRequest;
import com.filiera.facile.dto.response.BigliettoResponse;
import com.filiera.facile.entities.DefaultBiglietto;
import com.filiera.facile.model.interfaces.BigliettoService;
import com.filiera.facile.repositories.BigliettoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/biglietti")
public class BigliettoController {

    private final BigliettoService bigliettoService;
    private final BigliettoRepository bigliettoRepository;

    @Autowired
    public BigliettoController(BigliettoService bigliettoService, BigliettoRepository bigliettoRepository) {
        this.bigliettoService = bigliettoService;
        this.bigliettoRepository = bigliettoRepository;
    }

    @PostMapping
    public ResponseEntity<BigliettoResponse> acquistaBiglietto(@Valid @RequestBody AcquistaBigliettoRequest request) {
        try {
            DefaultBiglietto bigliettoAcquistato = bigliettoService.acquistaBiglietto(
                    request.getUtenteId(),
                    request.getEventoId()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BigliettoResponse(bigliettoAcquistato));
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante l'acquisto del biglietto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BigliettoResponse> getBiglietto(@PathVariable Long id) {
        return bigliettoRepository.findById(id)
                .map(biglietto -> ResponseEntity.ok(new BigliettoResponse(biglietto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BigliettoResponse>> getAllBiglietti() {
        List<BigliettoResponse> biglietti = bigliettoRepository.findAll().stream()
                .map(BigliettoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(biglietti);
    }

    // TODO: implementare la chiamata per ottenere tutti i biglietti di un determinato utente
    // @GetMapping("/utente/{utenteId}")

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<BigliettoResponse>> getBigliettiPerEvento(@PathVariable Long eventoId) {
        try {
            List<DefaultBiglietto> biglietti = bigliettoService.trovaBigliettiPerEvento(eventoId);
            List<BigliettoResponse> response = biglietti.stream()
                    .map(BigliettoResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante il recupero dei biglietti: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/annulla")
    public ResponseEntity<Void> annullaBiglietto(@PathVariable Long id) {
        try {
            bigliettoService.annullaBiglietto(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante l'annullamento del biglietto: " + e.getMessage());
        }
    }

    @GetMapping("/evento/{eventoId}/count")
    public ResponseEntity<Long> countBigliettiPerEvento(@PathVariable Long eventoId) {
        try {
            List<DefaultBiglietto> biglietti = bigliettoService.trovaBigliettiPerEvento(eventoId);
            return ResponseEntity.ok((long) biglietti.size());
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante il conteggio dei biglietti: " + e.getMessage());
        }
    }

    @GetMapping("/evento/{eventoId}/disponibilita")
    public ResponseEntity<Boolean> verificaDisponibilita(@PathVariable Long eventoId) {
        try {
            List<DefaultBiglietto> biglietti = bigliettoService.trovaBigliettiPerEvento(eventoId);
            boolean disponibile = biglietti.size() < 100;
            return ResponseEntity.ok(disponibile);
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante la verifica della disponibilità: " + e.getMessage());
        }
    }
}