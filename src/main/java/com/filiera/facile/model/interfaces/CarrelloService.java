package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultCarrello;

import java.util.UUID;

public interface CarrelloService {
    DefaultCarrello getCarrelloPerUtente(Long utenteId);
    void aggiungiArticoloAlCarrello(Long utenteId, Long articoloId, int quantita);
    void rimuoviArticoloDalCarrello(Long utenteId, Long articoloId);
    void svuotaCarrello(Long utenteId);
}