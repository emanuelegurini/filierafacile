package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.model.interfaces.ArticoloVendibile;
import com.filiera.facile.repositories.CarrelloRepository;
import com.filiera.facile.model.interfaces.CarrelloService;
import com.filiera.facile.repositories.ProdottoRepository;



public class DefaultCarrelloService implements CarrelloService {

    private final CarrelloRepository carrelloRepository;
    private final ProdottoRepository prodottoRepository;

    public DefaultCarrelloService(CarrelloRepository carrelloRepository, ProdottoRepository prodottoRepository) {
        this.carrelloRepository = carrelloRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    public DefaultCarrello getCarrelloPerUtente(Long utenteId) {
        return carrelloRepository.findByUtenteId(utenteId)
                .orElseGet(() -> {
                    DefaultCarrello nuovoCarrello = new DefaultCarrello(utenteId);
                    carrelloRepository.save(nuovoCarrello);
                    return nuovoCarrello;
                });
    }

    @Override
    public void aggiungiArticoloAlCarrello(Long utenteId, Long articoloId, int quantita) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        ArticoloVendibile articoloDaAggiungere = trovaArticoloVendibile(articoloId);

        carrello.aggiungiArticolo(articoloDaAggiungere, quantita);

        carrelloRepository.save(carrello);
    }

    @Override
    public void rimuoviArticoloDalCarrello(Long utenteId, Long articoloId) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        carrello.rimuoviArticolo(articoloId);
        carrelloRepository.save(carrello);
    }

    @Override
    public void svuotaCarrello(Long utenteId) {
        DefaultCarrello carrello = getCarrelloPerUtente(utenteId);
        carrello.svuota();
        carrelloRepository.save(carrello);
    }

    private ArticoloVendibile trovaArticoloVendibile(Long articoloId) {
        return prodottoRepository.findById(articoloId)
                .orElseThrow(() -> new RuntimeException("Nessun articolo vendibile trovato con ID: " + articoloId));
    }
}