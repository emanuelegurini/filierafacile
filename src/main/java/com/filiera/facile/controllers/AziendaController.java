package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.CreaAziendaRequest;
import com.filiera.facile.dto.response.AziendaResponse;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultCoordinate;
import com.filiera.facile.model.enums.TipoAzienda;
import com.filiera.facile.model.interfaces.AziendaService;
import com.filiera.facile.repositories.AziendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/aziende")
public class AziendaController {

    private final AziendaService aziendaService;
    private final AziendaRepository aziendaRepository;

    @Autowired
    public AziendaController(AziendaService aziendaService, AziendaRepository aziendaRepository) {
        this.aziendaService = aziendaService;
        this.aziendaRepository = aziendaRepository;
    }

    @PostMapping
    public ResponseEntity<AziendaResponse> creaAzienda(@Valid @RequestBody CreaAziendaRequest request) {
        try {
            // Crea coordinate con valori forniti o default
            Float lat = request.getLatitudine() != null ? request.getLatitudine() : 0.0f;
            Float lon = request.getLongitudine() != null ? request.getLongitudine() : 0.0f;
            DefaultCoordinate coordinate = new DefaultCoordinate(lat, lon);

            DefaultAzienda nuovaAzienda = new DefaultAzienda(
                request.getRagioneSociale(),
                request.getPartitaIva(),
                request.getIndirizzo(),
                request.getEmail(),
                request.getNumeroTelefono(),
                request.getSitoWeb() != null ? request.getSitoWeb() : "",
                coordinate
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

    @GetMapping("/{id}")
    public ResponseEntity<AziendaResponse> getAzienda(@PathVariable Long id) {
        return aziendaRepository.findById(id)
                .map(azienda -> ResponseEntity.ok(new AziendaResponse(azienda)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AziendaResponse>> getAllAziende() {
        List<AziendaResponse> aziende = aziendaRepository.findAll().stream()
                .map(AziendaResponse::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(aziende);
    }
}