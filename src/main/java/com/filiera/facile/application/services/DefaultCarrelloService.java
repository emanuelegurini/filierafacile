package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.entities.ArticoloCatalogo;
import com.filiera.facile.repositories.CarrelloRepository;
import com.filiera.facile.model.interfaces.CarrelloService;
import com.filiera.facile.repositories.ArticoloCatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCarrelloService implements CarrelloService {

    private final CarrelloRepository carrelloRepository;
    private final ArticoloCatalogoRepository articoloCatalogoRepository;

    @Autowired
    public DefaultCarrelloService(CarrelloRepository carrelloRepository, ArticoloCatalogoRepository articoloCatalogoRepository) {
        this.carrelloRepository = carrelloRepository;
        this.articoloCatalogoRepository = articoloCatalogoRepository;
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
    @Transactional
    public void aggiungiArticoloAlCarrello(Long utenteId, Long articoloId, int quantita) {
        DefaultCarrello cart = getCarrelloPerUtente(utenteId);
        ArticoloCatalogo item = trovaArticoloCatalogo(articoloId);
        cart.aggiungiArticolo(item, quantita);
        carrelloRepository.save(cart);
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

    private ArticoloCatalogo trovaArticoloCatalogo(Long articoloId) {
        return articoloCatalogoRepository.findById(articoloId)
                .orElseThrow(() -> new RuntimeException("Nessun articolo trovato con ID: " + articoloId));
    }
}