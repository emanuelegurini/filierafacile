package com.filiera.facile.entities;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.ArticoloVendibile;
import com.filiera.facile.model.interfaces.Validabile;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "articolo_catalogo")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class ArticoloCatalogo implements ArticoloVendibile, Validabile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected final Long id;

    @Column(name = "nome", nullable = false, length = 255)
    protected String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    protected String descrizione;

    @Column(name = "prezzo_unitario")
    private double prezzoUnitario;

    @Column(name = "quantita_disponibile")
    protected double quantitaDisponibile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    protected final DefaultAzienda aziendaDiRiferimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", length = 50)
    protected StatoValidazione stato;

    public ArticoloCatalogo(String nome, String descrizione, double prezzoUnitario, DefaultAzienda azienda) {
        this.id = null;
        this.nome = Objects.requireNonNull(nome, "Il nome non può essere nullo.");
        this.descrizione = descrizione;
        this.prezzoUnitario = prezzoUnitario;
        this.aziendaDiRiferimento = Objects.requireNonNull(azienda, "L'azienda di riferimento non può essere nulla.");
        this.stato = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;
        this.quantitaDisponibile = 0;
    }

    @Override
    public String getNomeArticolo() {
        return this.nome;
    }

    @Override
    public void setNomeArticolo(String nomeArticolo) {
        this.nome = nomeArticolo;
    }

    @Override
    public String getDescrizioneArticolo() {
        return this.descrizione;
    }

    @Override
    public void setDescrizioneArticolo(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public void setStatoValidazione(StatoValidazione stato) {
        this.stato = stato;
    }

    @Override
    public StatoValidazione getStatoValidazione() {
        return this.stato;
    }

    @Override
    public Long getId() { return id; }

    public double getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(double quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public DefaultAzienda getAziendaDiRiferimento() {
        return aziendaDiRiferimento;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    @Override
    public void sottomettiPerValidazione() {
        System.out.println("Prodotto '" + nome + "' sottomesso per validazione.");
    }
}