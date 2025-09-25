package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.RegistraUtenteRequest;
import com.filiera.facile.dto.response.UtenteResponse;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.model.enums.RuoloPiattaforma;
import com.filiera.facile.model.interfaces.UserService;
import com.filiera.facile.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UserService userService;
    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteController(UserService userService, UtenteRepository utenteRepository) {
        this.userService = userService;
        this.utenteRepository = utenteRepository;
    }

    @PostMapping("/registrazione")
    public ResponseEntity<UtenteResponse> registraUtente(@Valid @RequestBody RegistraUtenteRequest request) {
        try {
            DefaultUtente nuovoUtente = new DefaultUtente(
                request.getNome(),
                request.getCognome(),
                request.getEmail(),
                request.getAddress(),
                request.getPhoneNumber(),
                request.getPassword()
            );

            DefaultUtente utenteRegistrato = userService.registraNuovoUtente(nuovoUtente);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UtenteResponse(utenteRegistrato));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Errore durante la registrazione: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtenteResponse> getUtente(@PathVariable Long id) {
        return utenteRepository.findById(id)
                .map(utente -> ResponseEntity.ok(new UtenteResponse(utente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UtenteResponse>> getAllUtenti() {
        List<UtenteResponse> utenti = utenteRepository.findAll().stream()
                .map(UtenteResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(utenti);
    }

    @GetMapping("/{id}/profilo")
    public ResponseEntity<UtenteResponse> getProfiloUtente(@PathVariable Long id) {
        return utenteRepository.findById(id)
                .map(utente -> ResponseEntity.ok(new UtenteResponse(utente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/ruoli")
    public ResponseEntity<UtenteResponse> aggiungiRuoloPiattaforma(
            @PathVariable Long id,
            @RequestParam RuoloPiattaforma ruolo) {

        try {
            DefaultUtente utente = utenteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));

            utente.addRuolo(ruolo);
            DefaultUtente utenteAggiornato = utenteRepository.save(utente);

            return ResponseEntity.ok(new UtenteResponse(utenteAggiornato));
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante l'aggiunta del ruolo: " + e.getMessage());
        }
    }

    @PostMapping("/{utenteId}/affiliazioni")
    public ResponseEntity<Void> aggiungiAffiliazione(
            @PathVariable Long utenteId,
            @RequestParam Long aziendaId,
            @RequestParam RuoloAziendale ruolo) {

        try {
            userService.aggiungiAffiliazione(utenteId, aziendaId, ruolo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante l'aggiunta dell'affiliazione: " + e.getMessage());
        }
    }

    @PutMapping("/{utenteId}/affiliazioni/{aziendaId}")
    public ResponseEntity<Void> aggiornaRuoloAffiliazione(
            @PathVariable Long utenteId,
            @PathVariable Long aziendaId,
            @RequestParam RuoloAziendale nuovoRuolo) {

        try {
            if (userService instanceof com.filiera.facile.application.services.DefaultUtenteService defaultService) {
                defaultService.aggiornaRuoloAffiliazione(utenteId, aziendaId, nuovoRuolo);
                return ResponseEntity.ok().build();
            }
            throw new RuntimeException("Servizio utente non supportato");
        } catch (RuntimeException e) {
            throw new RuntimeException("Errore durante l'aggiornamento del ruolo: " + e.getMessage());
        }
    }

    // TODO: Endpoint ricerca utenti - dipende da repository method non implementato
    // @GetMapping("/cerca")
}