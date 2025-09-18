package com.filiera.facile.domain;

import com.filiera.facile.model.interfaces.ArticoloVendibile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultCarrello {

    private final UUID id;
    private final UUID utenteId;
    private final Map<UUID, DefaultRigaCarrello> righe;

    public DefaultCarrello(UUID utenteId) {
        this.id = UUID.randomUUID();
        this.utenteId = utenteId;
        this.righe = new HashMap<>();
    }

    public UUID getUtenteId() {
        return this.utenteId;
    }

    public void aggiungiArticolo(ArticoloVendibile articolo, int quantita) {
        UUID articoloId = articolo.getId();

        if (righe.containsKey(articoloId)) {
            righe.get(articoloId).incrementaQuantita(quantita);
        } else {
            righe.put(articoloId, new DefaultRigaCarrello(articolo, quantita));
        }
    }

    public void rimuoviArticolo(UUID articoloId) {
        righe.remove(articoloId);
    }

    public void aggiornaQuantita(UUID articoloId, int nuovaQuantita) {
        if (righe.containsKey(articoloId)) {
            righe.get(articoloId).setQuantita(nuovaQuantita);
        }
    }

    public double getTotaleComplessivo() {
        return righe.values().stream()
                .mapToDouble(DefaultRigaCarrello::getPrezzoTotaleRiga)
                .sum();
    }

    public Map<UUID, DefaultRigaCarrello> getRighe() {
        return righe;
    }

    public void svuota() {
        righe.clear();
    }
}
