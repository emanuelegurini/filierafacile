package com.filiera.facile.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pacchetto_prodotti_inclusi")
public class ProdottoPacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacchetto_id", nullable = false)
    private DefaultPacchettoProdotti pacchetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articolo_id", nullable = false)
    private ArticoloCatalogo articolo;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    protected ProdottoPacchetto() {}

    public ProdottoPacchetto(DefaultPacchettoProdotti pacchetto, ArticoloCatalogo articolo, Integer quantita) {
        this.pacchetto = Objects.requireNonNull(pacchetto);
        this.articolo = Objects.requireNonNull(articolo);
        this.quantita = Objects.requireNonNull(quantita);
    }

    public Long getId() { return id; }
    public DefaultPacchettoProdotti getPacchetto() { return pacchetto; }
    public ArticoloCatalogo getArticolo() { return articolo; }
    public Integer getQuantita() { return quantita; }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdottoPacchetto)) return false;
        ProdottoPacchetto that = (ProdottoPacchetto) o;
        return Objects.equals(pacchetto.getId(), that.pacchetto.getId()) &&
               Objects.equals(articolo.getId(), that.articolo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pacchetto.getId(), articolo.getId());
    }
}