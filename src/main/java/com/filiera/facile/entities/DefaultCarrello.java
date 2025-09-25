package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.ArticoloVendibile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "carrello")
public class DefaultCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utente_id", nullable = false)
    private Long utenteId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carrello", orphanRemoval = true)
    public List<DefaultRigaCarrello> righeLista;

    public DefaultCarrello() {
        this.righeLista = new ArrayList<>();
    }

    public DefaultCarrello(Long utenteId) {
        this.utenteId = utenteId;
        this.righeLista = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Long getUtenteId() {
        return this.utenteId;
    }

    public void aggiungiArticolo(ArticoloCatalogo articolo, int quantita) {
        Long id = articolo.getId();
        DefaultRigaCarrello existing = righeLista.stream()
                .filter(r -> r.getArticolo().getId().equals(id))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.incrementaQuantita(quantita);
        } else {
            DefaultRigaCarrello newRow = new DefaultRigaCarrello(articolo, quantita);
            newRow.setCarrello(this);
            righeLista.add(newRow);
        }
    }

    public void rimuoviArticolo(Long articoloId) {
        righeLista.removeIf(r -> r.getArticolo().getId().equals(articoloId));
    }

    public void aggiornaQuantita(Long articoloId, int nuovaQuantita) {
        righeLista.stream()
                .filter(r -> r.getArticolo().getId().equals(articoloId))
                .findFirst()
                .ifPresent(r -> r.setQuantita(nuovaQuantita));
    }

    public double getTotaleComplessivo() {
        return righeLista.stream()
                .mapToDouble(DefaultRigaCarrello::getPrezzoTotaleRiga)
                .sum();
    }

    public void svuota() {
        righeLista.clear();
    }

    public List<DefaultRigaCarrello> getRigheLista() {
        return righeLista;
    }
}
