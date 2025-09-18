package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultCarrello;

import java.util.UUID;

public interface CarrelloService {
    DefaultCarrello getCarrelloPerUtente(UUID utenteId);
    void aggiungiArticoloAlCarrello(UUID utenteId, UUID articoloId, int quantita);
    void rimuoviArticoloDalCarrello(UUID utenteId, UUID articoloId);
    void svuotaCarrello(UUID utenteId);
}