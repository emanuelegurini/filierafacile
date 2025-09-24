package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.ValutaPraticaRequest;
import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.interfaces.ValidazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/validazione")
public class ValidazioneController {

    private final ValidazioneService validazioneService;

    @Autowired
    public ValidazioneController(ValidazioneService validazioneService) {
        this.validazioneService = validazioneService;
    }

    @PostMapping("/curatori/{curatoreId}")
    public ResponseEntity<Void> registraCuratore(@PathVariable Long curatoreId) {
        validazioneService.registraCuratore(curatoreId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/curatori/{curatoreId}")
    public ResponseEntity<Void> rimuoviCuratore(@PathVariable Long curatoreId) {
        validazioneService.rimuoviCuratore(curatoreId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pratiche")
    public ResponseEntity<List<DefaultPraticaValidazione>> getPratichePendenti() {
        List<DefaultPraticaValidazione> pratiche = validazioneService.getPratichePendenti();
        return ResponseEntity.ok(pratiche);
    }

    @GetMapping("/pratiche/{praticaId}")
    public ResponseEntity<DefaultPraticaValidazione> getPratica(@PathVariable Long praticaId) {
        Optional<DefaultPraticaValidazione> pratica = validazioneService.getPraticaById(praticaId);
        return pratica.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/curatori/{curatoreId}/pratiche")
    public ResponseEntity<List<DefaultPraticaValidazione>> getPratichePerCuratore(
            @PathVariable Long curatoreId) {
        List<DefaultPraticaValidazione> pratiche = validazioneService.getPratichePerCuratore(curatoreId);
        return ResponseEntity.ok(pratiche);
    }

    @PostMapping("/pratiche/{praticaId}/approva")
    public ResponseEntity<Void> approvaPratica(
            @PathVariable Long praticaId,
            @RequestParam Long curatoreId,
            @Valid @RequestBody ValutaPraticaRequest request) {

        validazioneService.approvaPratica(praticaId, curatoreId, request.getNote());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pratiche/{praticaId}/respingi")
    public ResponseEntity<Void> respingiPratica(
            @PathVariable Long praticaId,
            @RequestParam Long curatoreId,
            @Valid @RequestBody ValutaPraticaRequest request) {

        validazioneService.respingiPratica(praticaId, curatoreId, request.getNote());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pratiche/{praticaId}/modifiche")
    public ResponseEntity<Void> richiedeModifiche(
            @PathVariable Long praticaId,
            @RequestParam Long curatoreId,
            @Valid @RequestBody ValutaPraticaRequest request) {

        validazioneService.richiedeModifiche(praticaId, curatoreId, request.getNote());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/curatori/{curatoreId}/libera")
    public ResponseEntity<Void> liberaCuratore(@PathVariable Long curatoreId) {
        validazioneService.liberaCuratore(curatoreId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/curatori/liberi")
    public ResponseEntity<List<Long>> getCuratoriLiberi() {
        List<Long> curatori = validazioneService.getCuratoriLiberi();
        return ResponseEntity.ok(curatori);
    }
}