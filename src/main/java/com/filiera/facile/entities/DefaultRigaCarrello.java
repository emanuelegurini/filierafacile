package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.ArticoloVendibile;

import java.util.Objects;

public class DefaultRigaCarrello {

    private final ArticoloVendibile articolo;
    private int quantita;

    public DefaultRigaCarrello(ArticoloVendibile articolo, int quantita) {
       this.articolo = Objects.requireNonNull(articolo);
       if (quantita <= 0) {
           throw new IllegalArgumentException("La quantità deve essere positiva.");
       }
       this.quantita = quantita;
    }

    public ArticoloVendibile getArticolo() {
        return articolo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva.");
        }
        this.quantita = quantita;
    }

    public void incrementaQuantita(int aggiunta) {
        this.quantita += aggiunta;
    }

    public double getPrezzoTotaleRiga() {
        return articolo.getPrezzoVendita() * quantita;
    }

}
