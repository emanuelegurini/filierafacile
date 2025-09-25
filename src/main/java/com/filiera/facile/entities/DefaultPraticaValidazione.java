package com.filiera.facile.entities;

import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.Validabile;

import java.time.LocalDateTime;
import java.util.Objects;

public class DefaultPraticaValidazione {
    private final Long id;
    private final Validabile contenutoSottomesso;
    private final Long richiedenteId;
    private final LocalDateTime dataCreazione;

    private Long curatoreAssegnato;
    private LocalDateTime dataAssegnazione;
    private LocalDateTime dataCompletamento;
    private String noteValutazione;
    private String motivazioneRifiuto;
    private StatoValidazione statoCorrente;

    public DefaultPraticaValidazione(Validabile contenutoSottomesso, Long richiedenteId) {
        this.id = null;
        this.contenutoSottomesso = Objects.requireNonNull(contenutoSottomesso, "Il contenuto da validare non può essere null");
        this.richiedenteId = Objects.requireNonNull(richiedenteId, "L'ID del richiedente non può essere null");
        this.dataCreazione = LocalDateTime.now();
        this.statoCorrente = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;
    }

    public Long getId() {
        return id;
    }

    public Validabile getContenutoSottomesso() {
        return contenutoSottomesso;
    }

    public Long getRichiedenteId() {
        return richiedenteId;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public Long getCuratoreAssegnato() {
        return curatoreAssegnato;
    }

    public void assegnaCuratore(Long curatoreId) {
        this.curatoreAssegnato = Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        this.dataAssegnazione = LocalDateTime.now();
        this.statoCorrente = StatoValidazione.IN_REVISIONE;
        this.contenutoSottomesso.setStatoValidazione(StatoValidazione.IN_REVISIONE);
    }

    public LocalDateTime getDataAssegnazione() {
        return dataAssegnazione;
    }

    public LocalDateTime getDataCompletamento() {
        return dataCompletamento;
    }

    public String getNoteValutazione() {
        return noteValutazione;
    }

    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public StatoValidazione getStatoCorrente() {
        return statoCorrente;
    }

    public void approva(String noteValutazione) {
        validaStatoPerCompletamento();
        this.noteValutazione = noteValutazione;
        this.dataCompletamento = LocalDateTime.now();
        this.statoCorrente = StatoValidazione.APPROVATO;
        this.contenutoSottomesso.setStatoValidazione(StatoValidazione.APPROVATO);
    }

    public void respingi(String motivazioneRifiuto) {
        validaStatoPerCompletamento();
        this.motivazioneRifiuto = Objects.requireNonNull(motivazioneRifiuto, "La motivazione del rifiuto è obbligatoria");
        this.dataCompletamento = LocalDateTime.now();
        this.statoCorrente = StatoValidazione.RESPINTO;
        this.contenutoSottomesso.setStatoValidazione(StatoValidazione.RESPINTO);
    }

    public void richiedeModifiche(String noteModifiche) {
        validaStatoPerCompletamento();
        this.noteValutazione = Objects.requireNonNull(noteModifiche, "Le note per le modifiche richieste sono obbligatorie");
        this.dataCompletamento = LocalDateTime.now();
        this.statoCorrente = StatoValidazione.MODIFICHE_RICHIESTE;
        this.contenutoSottomesso.setStatoValidazione(StatoValidazione.MODIFICHE_RICHIESTE);
    }

    public boolean isAssegnata() {
        return curatoreAssegnato != null;
    }

    public boolean isCompletata() {
        return dataCompletamento != null;
    }

    private void validaStatoPerCompletamento() {
        if (!StatoValidazione.IN_REVISIONE.equals(statoCorrente)) {
            throw new IllegalStateException("La pratica deve essere in revisione per poter essere completata");
        }
        if (curatoreAssegnato == null) {
            throw new IllegalStateException("La pratica deve essere assegnata a un curatore per poter essere completata");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultPraticaValidazione that = (DefaultPraticaValidazione) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}