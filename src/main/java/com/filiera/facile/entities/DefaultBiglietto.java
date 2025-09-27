package com.filiera.facile.entities;

import com.filiera.facile.model.enums.StatoBiglietto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "biglietto")
public class DefaultBiglietto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private DefaultEvento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intestatario_id", nullable = false)
    private DefaultUtente intestatario;

    @Column(nullable = false)
    private LocalDateTime dataEmissione;

    @Column(nullable = false)
    private double prezzoPagato;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private StatoBiglietto stato;

    public DefaultBiglietto() {
    }

    public DefaultBiglietto(DefaultEvento evento, DefaultUtente intestatario) {
        this.evento = Objects.requireNonNull(evento, "L'evento non può essere nullo.");
        this.intestatario = Objects.requireNonNull(intestatario, "L'intestatario non può essere nullo.");

        this.dataEmissione = LocalDateTime.now();
        this.prezzoPagato = evento.getCostoPartecipazione();
        this.stato = StatoBiglietto.VALIDO;
    }

    public void setStato(StatoBiglietto stato) {
        this.stato = stato;
    }
}