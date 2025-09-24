package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.model.interfaces.ArticoloVendibile;
import com.filiera.facile.model.interfaces.CarrelloRepository;
import com.filiera.facile.model.interfaces.CarrelloService;
import com.filiera.facile.model.interfaces.ProdottoRepository;

import java.util.UUID;

public class DefaultCarrelloService implements CarrelloService {

    private final CarrelloRepository carrelloRepository;
    private final ProdottoRepository prodottoRepository;

    public DefaultCarrelloService(CarrelloRepository carrelloRepository, ProdottoRepository prodottoRepository) {
        this.carrelloRepository = carrelloRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    public DefaultCarrello getCarrelloPerUtente(UUID utenteId) {
        return carrelloRepository.findByUtenteId(utenteId)
                .orElseGet(() -> {
                    DefaultCarrello nuovoCarrello = new DefaultCarrello(utenteId);
                    carrelloRepository.save(nuovoCarrello);
                    return nuovoCarrello;
                });
    }

    @Override
    public void aggiungiArticoloAlCarrello(UUID utenteId, UUID articoloId, int quantita) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        ArticoloVendibile articoloDaAggiungere = trovaArticoloVendibile(articoloId);

        carrello.aggiungiArticolo(articoloDaAggiungere, quantita);

        carrelloRepository.save(carrello);
    }

    @Override
    public void rimuoviArticoloDalCarrello(UUID utenteId, UUID articoloId) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        carrello.rimuoviArticolo(articoloId);
        carrelloRepository.save(carrello);
    }

    @Override
    public void svuotaCarrello(UUID utenteId) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        carrello.svuota();
        carrelloRepository.save(carrello);
    }

    private ArticoloVendibile trovaArticoloVendibile(UUID articoloId) {
        return prodottoRepository.findById(articoloId)
                .orElseThrow(() -> new RuntimeException("Nessun articolo vendibile trovato con ID: " + articoloId));
    }
}