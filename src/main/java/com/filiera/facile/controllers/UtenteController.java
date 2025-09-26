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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utenti")
@Tag(name = "Utenti", description = "API per la gestione degli utenti")
public class UtenteController {

    private final UserService userService;
    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteController(UserService userService, UtenteRepository utenteRepository) {
        this.userService = userService;
        this.utenteRepository = utenteRepository;
    }

    @PostMapping("/registrazione")
    @Operation(summary = "Registra nuovo utente", description = "Registra un nuovo utente nel sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utente registrato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati richiesta non validi"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<UtenteResponse> registraUtente(
            @Valid @RequestBody @Parameter(description = "Dati per la registrazione dell'utente") RegistraUtenteRequest request) {
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
    @Operation(summary = "Dettagli utente", description = "Recupera i dettagli di un utente specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utente trovato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<UtenteResponse> getUtente(
            @PathVariable @Parameter(description = "ID dell'utente") Long id) {
        return utenteRepository.findById(id)
                .map(utente -> ResponseEntity.ok(new UtenteResponse(utente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Lista tutti gli utenti", description = "Recupera la lista di tutti gli utenti registrati")
    @ApiResponse(responseCode = "200", description = "Lista utenti recuperata con successo")
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

    @GetMapping("/test-hot-reload")
    @Operation(summary = "Test hot reload", description = "Endpoint di test per verificare l'hot reload di DevTools")
    public ResponseEntity<String> testHotReload() {
        return ResponseEntity.ok("🔥 Hot reload funziona! Timestamp: " + System.currentTimeMillis());
    }
}