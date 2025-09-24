package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.ArticoloVendibile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultCarrello {

    private final Long id;
    private final Long utenteId;
    private final Map<Long, DefaultRigaCarrello> righe;

    public DefaultCarrello(Long utenteId) {
        this.id = null;
        this.utenteId = utenteId;
        this.righe = new HashMap<>();
    }

    public Long getUtenteId() {
        return this.utenteId;
    }

    public void aggiungiArticolo(ArticoloVendibile articolo, int quantita) {
        Long articoloId = articolo.getId();

        if (righe.containsKey(articoloId)) {
            righe.get(articoloId).incrementaQuantita(quantita);
        } else {
            righe.put(articoloId, new DefaultRigaCarrello(articolo, quantita));
        }
    }

    public void rimuoviArticolo(Long articoloId) {
        righe.remove(articoloId);
    }

    public void aggiornaQuantita(Long articoloId, int nuovaQuantita) {
        if (righe.containsKey(articoloId)) {
            righe.get(articoloId).setQuantita(nuovaQuantita);
        }
    }

    public double getTotaleComplessivo() {
        return righe.values().stream()
                .mapToDouble(DefaultRigaCarrello::getPrezzoTotaleRiga)
                .sum();
    }

    public Map<Long, DefaultRigaCarrello> getRighe() {
        return righe;
    }

    public void svuota() {
        righe.clear();
    }
}
