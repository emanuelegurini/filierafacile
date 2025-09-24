package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaAziendaRequest;
import com.filiera.facile.dto.response.AziendaResponse;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultCoordinate;
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
        try {
            // Per ora usiamo valori di default per i campi obbligatori non presenti nel DTO
            DefaultCoordinate coordinateDefault = new DefaultCoordinate(0.0f, 0.0f);

            DefaultAzienda nuovaAzienda = new DefaultAzienda(
                request.getNome(),
                request.getPartitaIva(),
                request.getDescrizione() != null ? request.getDescrizione() : "Indirizzo non specificato",
                "noemail@example.com", // TODO: aggiungere email al DTO
                "000-000-0000", // TODO: aggiungere telefono al DTO
                "http://www.example.com", // TODO: aggiungere sito web al DTO
                coordinateDefault
            );

            DefaultAzienda aziendaCreata = aziendaService.creaNuovaAzienda(nuovaAzienda);

            // Aggiungi i tipi azienda
            if (request.getTipiAzienda() != null) {
                for (TipoAzienda tipo : request.getTipiAzienda()) {
                    aziendaService.aggiungiTipoAzienda(aziendaCreata.getId(), tipo);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AziendaResponse(aziendaCreata));
        } catch (Exception e) {
            // Il GlobalExceptionHandler gestirà l'eccezione
            throw new RuntimeException("Errore durante la creazione dell'azienda: " + e.getMessage());
        }
    }

    @PostMapping("/{aziendaId}/tipi")
    public ResponseEntity<Void> aggiungiTipoAzienda(
            @PathVariable Long aziendaId,
            @RequestParam TipoAzienda tipo) {

        aziendaService.aggiungiTipoAzienda(aziendaId, tipo);

        return ResponseEntity.ok().build();
    }
}