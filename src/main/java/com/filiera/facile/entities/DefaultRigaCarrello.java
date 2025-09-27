package com.filiera.facile.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Objects;

@Entity
@Table(name = "riga_carrello")
public class DefaultRigaCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrello_id", nullable = false)
    @JsonBackReference
    private DefaultCarrello carrello;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articolo_id", nullable = false)
    private ArticoloCatalogo articolo;

    @Column(nullable = false)
    private int quantita;

    public DefaultRigaCarrello() {
    }

    public DefaultRigaCarrello(ArticoloCatalogo articolo, int quantita) {
       this.articolo = Objects.requireNonNull(articolo);
       if (quantita <= 0) {
           throw new IllegalArgumentException("La quantità deve essere positiva.");
       }
       this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public DefaultCarrello getCarrello() {
        return carrello;
    }

    public void setCarrello(DefaultCarrello carrello) {
        this.carrello = carrello;
    }

    public ArticoloCatalogo getArticolo() {
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
        return articolo.getPrezzoUnitario() * quantita;
    }

}
