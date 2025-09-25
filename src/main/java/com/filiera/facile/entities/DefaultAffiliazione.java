package com.filiera.facile.entities;

import com.filiera.facile.model.enums.RuoloAziendale;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "affiliazione")
public class DefaultAffiliazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private DefaultUtente defaultUtente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    private DefaultAzienda defaultAzienda;

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo_aziendale", length = 50, nullable = false)
    private RuoloAziendale ruoloAziendale;

    @Column(name = "data_affiliazione", nullable = false)
    private LocalDateTime dataAffiliazione;

    // Costruttore vuoto richiesto da JPA
    protected DefaultAffiliazione() {}

    public DefaultAffiliazione(
            DefaultUtente defaultUtente,
            DefaultAzienda defaultAzienda,
            RuoloAziendale ruoloAziendale
    ) {
        this.defaultUtente = Objects.requireNonNull(defaultUtente, "L'utente non può essere nullo.");
        this.defaultAzienda = Objects.requireNonNull(defaultAzienda, "L'azienda non può essere nulla.");
        this.ruoloAziendale = Objects.requireNonNull(ruoloAziendale, "Il ruolo aziendale non può essere nullo.");
        this.dataAffiliazione = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public DefaultUtente getUtente() {
        return defaultUtente;
    }

    public DefaultAzienda getAzienda() {
        return defaultAzienda;
    }

    public RuoloAziendale getRuoloAziendale() {
        return ruoloAziendale;
    }

    public void setRuoloAziendale(RuoloAziendale ruoloAziendale) {
        this.ruoloAziendale = ruoloAziendale;
    }

    public LocalDateTime getDataAffiliazione() {
        return dataAffiliazione;
    }

    // Setter per JPA
    protected void setId(Long id) {
        this.id = id;
    }

    protected void setUtente(DefaultUtente defaultUtente) {
        this.defaultUtente = defaultUtente;
    }

    protected void setAzienda(DefaultAzienda defaultAzienda) {
        this.defaultAzienda = defaultAzienda;
    }

    protected void setDataAffiliazione(LocalDateTime dataAffiliazione) {
        this.dataAffiliazione = dataAffiliazione;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAffiliazione that = (DefaultAffiliazione) o;
        return Objects.equals(defaultUtente, that.defaultUtente)
                && Objects.equals(defaultAzienda, that.defaultAzienda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultUtente, defaultAzienda);
    }
}