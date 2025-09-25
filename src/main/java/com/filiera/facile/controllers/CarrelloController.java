package com.filiera.facile.controllers;

import com.filiera.facile.dto.request.AggiungiAlCarrelloRequest;
import com.filiera.facile.dto.response.CarrelloResponse;
import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.model.interfaces.CarrelloService;
import com.filiera.facile.repositories.CarrelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    private final CarrelloService carrelloService;
    private final CarrelloRepository carrelloRepository;

    @Autowired
    public CarrelloController(CarrelloService carrelloService, CarrelloRepository carrelloRepository) {
        this.carrelloService = carrelloService;
        this.carrelloRepository = carrelloRepository;
    }

    @GetMapping("/{utenteId}")
    public ResponseEntity<CarrelloResponse> getCarrello(@PathVariable Long utenteId) {
        DefaultCarrello carrello = carrelloService.getCarrelloPerUtente(utenteId);
        return ResponseEntity.ok(new CarrelloResponse(carrello));
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

    // TODO: Endpoint totale carrello - dipende da calcolaTotale() non implementato
    // @GetMapping("/{utenteId}/totale")

    // TODO: Endpoint righe carrello - dipende da getRighe().stream() non implementato
    // @GetMapping("/{utenteId}/righe")

    @PutMapping("/{utenteId}/articoli/{articoloId}")
    public ResponseEntity<Void> aggiornaQuantita(
            @PathVariable Long utenteId,
            @PathVariable Long articoloId,
            @RequestParam Integer nuovaQuantita) {

        if (nuovaQuantita <= 0) {
            carrelloService.rimuoviArticoloDalCarrello(utenteId, articoloId);
        } else {
            DefaultCarrello cart = carrelloService.getCarrelloPerUtente(utenteId);
            cart.aggiornaQuantita(articoloId, nuovaQuantita);
            carrelloRepository.save(cart);
        }
        return ResponseEntity.ok().build();
    }

    // TODO: Endpoint count articoli - dipende da getRighe().stream() non implementato
    // @GetMapping("/{utenteId}/count")

    @GetMapping
    public ResponseEntity<List<DefaultCarrello>> getAllCarrelli() {
        List<DefaultCarrello> carrelli = carrelloRepository.findAll();
        return ResponseEntity.ok(carrelli);
    }

    // TODO: Endpoint checkout - dipende da calcolaTotale() non implementato
    // @PostMapping("/{utenteId}/checkout")
}