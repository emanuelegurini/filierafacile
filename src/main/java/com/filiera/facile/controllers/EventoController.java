package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaEventoRequest;
import com.filiera.facile.dto.response.EventoResponse;
import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.entities.DefaultCoordinate;
import com.filiera.facile.model.interfaces.EventoService;
import com.filiera.facile.repositories.EventoRepository;
import com.filiera.facile.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@Tag(name = "Eventi", description = "API per la gestione degli eventi")
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    @Autowired
    public EventoController(EventoService eventoService, EventoRepository eventoRepository, UtenteRepository utenteRepository) {
        this.eventoService = eventoService;
        this.eventoRepository = eventoRepository;
        this.utenteRepository = utenteRepository;
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo evento", description = "Crea un nuovo evento con i dettagli forniti")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati richiesta non validi"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<EventoResponse> creaEvento(
            @Valid @RequestBody @Parameter(description = "Dati per la creazione dell'evento") CreaEventoRequest request,
            @RequestParam @Parameter(description = "ID dell'organizzatore dell'evento") Long organizzatoreId) {

        try {
            DefaultUtente organizzatore = utenteRepository.findById(organizzatoreId)
                    .orElseThrow(() -> new RuntimeException("Utente organizzatore non trovato"));

            DefaultCoordinate coordinate = new DefaultCoordinate(45.4642f, 9.1900f); // Milano coordinates as default

            DefaultEvento nuovoEvento = new DefaultEvento(
                    request.getNome(),
                    request.getDescrizione(),
                    request.getDataInizio(),
                    request.getDataFine(),
                    organizzatore,
                    request.getLuogo(),
                    coordinate
            );

            DefaultEvento eventoSalvato = eventoService.creaNuovoEvento(organizzatoreId, nuovoEvento);

            return ResponseEntity.ok(new EventoResponse(eventoSalvato));
        } catch (SecurityException e) {
            throw new RuntimeException("Accesso negato: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione dell'evento: " + e.getMessage());
        }
    }

    @PostMapping("/{eventoId}/aziende/{aziendaId}")
    @Operation(summary = "Aggiungi azienda all'evento", description = "Associa un'azienda partecipante all'evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Azienda aggiunta con successo"),
        @ApiResponse(responseCode = "404", description = "Evento o azienda non trovati")
    })
    public ResponseEntity<Void> aggiungiAzienda(
            @PathVariable @Parameter(description = "ID dell'evento") Long eventoId,
            @PathVariable @Parameter(description = "ID dell'azienda") Long aziendaId) {

        eventoService.aggiungiAziendaAdEvento(eventoId, aziendaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventoId}/aziende/{aziendaId}")
    @Operation(summary = "Rimuovi azienda dall'evento", description = "Rimuove un'azienda partecipante dall'evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Azienda rimossa con successo"),
        @ApiResponse(responseCode = "404", description = "Evento o azienda non trovati")
    })
    public ResponseEntity<Void> rimuoviAzienda(
            @PathVariable @Parameter(description = "ID dell'evento") Long eventoId,
            @PathVariable @Parameter(description = "ID dell'azienda") Long aziendaId) {

        eventoService.rimuoviAziendaDaEvento(eventoId, aziendaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/azienda/{aziendaId}")
    @Operation(summary = "Eventi per azienda", description = "Recupera tutti gli eventi a cui partecipa un'azienda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista eventi recuperata con successo"),
        @ApiResponse(responseCode = "404", description = "Azienda non trovata")
    })
    public ResponseEntity<List<EventoResponse>> getEventiPerAzienda(
            @PathVariable @Parameter(description = "ID dell'azienda") Long aziendaId) {
        List<DefaultEvento> eventi = eventoService.trovaEventiPerAzienda(aziendaId);
        List<EventoResponse> eventiResponse = eventi.stream()
                .map(EventoResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(eventiResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dettagli evento", description = "Recupera i dettagli di un evento specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento trovato"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<EventoResponse> getEvento(
            @PathVariable @Parameter(description = "ID dell'evento") Long id) {
        return eventoRepository.findById(id)
                .map(evento -> ResponseEntity.ok(new EventoResponse(evento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Lista tutti gli eventi", description = "Recupera la lista di tutti gli eventi")
    @ApiResponse(responseCode = "200", description = "Lista eventi recuperata con successo")
    public ResponseEntity<List<EventoResponse>> getAllEventi() {
        List<DefaultEvento> eventi = eventoRepository.findAll();
        List<EventoResponse> eventiResponse = eventi.stream()
                .map(EventoResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(eventiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> aggiornaEvento(
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
                    return ResponseEntity.ok(new EventoResponse(eventoAggiornato));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina evento", description = "Elimina un evento dal sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<Void> eliminaEvento(
            @PathVariable @Parameter(description = "ID dell'evento da eliminare") Long id) {
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
    @Operation(summary = "Posti disponibili", description = "Recupera il numero di posti disponibili per un evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero posti recuperato con successo"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<Integer> getPostiDisponibili(
            @PathVariable @Parameter(description = "ID dell'evento") Long id) {
        return eventoRepository.findById(id)
                .map(evento -> ResponseEntity.ok(evento.getPostiDisponibili()))
                .orElse(ResponseEntity.notFound().build());
    }

}