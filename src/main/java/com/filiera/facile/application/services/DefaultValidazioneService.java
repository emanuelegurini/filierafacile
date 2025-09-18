package com.filiera.facile.application.services;

import com.filiera.facile.domain.DefaultAssegnatoreAutomatico;
import com.filiera.facile.domain.DefaultPraticaValidazione;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultValidazioneService implements ValidazioneService {
    private final CodaValidazione codaValidazione;
    private final CuratoreStatusTracker statusTracker;
    private final DefaultAssegnatoreAutomatico assegnatore;
    private final Map<UUID, DefaultPraticaValidazione> praticheInCorso;

    public DefaultValidazioneService(CodaValidazione codaValidazione, CuratoreStatusTracker statusTracker) {
        this.codaValidazione = Objects.requireNonNull(codaValidazione, "La coda di validazione non può essere null");
        this.statusTracker = Objects.requireNonNull(statusTracker, "Il tracker degli stati non può essere null");
        this.assegnatore = new DefaultAssegnatoreAutomatico(codaValidazione, statusTracker);
        this.praticheInCorso = new HashMap<>();
    }

    @Override
    public UUID sottomettiPerValidazione(Validabile contenuto, UUID richiedenteId) {
        Objects.requireNonNull(contenuto, "Il contenuto da validare non può essere null");
        Objects.requireNonNull(richiedenteId, "L'ID del richiedente non può essere null");

        DefaultPraticaValidazione pratica = new DefaultPraticaValidazione(contenuto, richiedenteId);
        praticheInCorso.put(pratica.getId(), pratica);

        assegnatore.onNuovaRichiestaValidazione(pratica);

        return pratica.getId();
    }

    @Override
    public void registraCuratore(UUID curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        statusTracker.registraCuratore(curatoreId);
    }

    @Override
    public void rimuoviCuratore(UUID curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        statusTracker.rimuoviCuratore(curatoreId);
    }

    @Override
    public void approvaPratica(UUID praticaId, UUID curatoreId, String noteValutazione) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.approva(noteValutazione);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public void respingiPratica(UUID praticaId, UUID curatoreId, String motivazioneRifiuto) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.respingi(motivazioneRifiuto);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public void richiedeModifiche(UUID praticaId, UUID curatoreId, String noteModifiche) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.richiedeModifiche(noteModifiche);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public Optional<DefaultPraticaValidazione> getPraticaById(UUID praticaId) {
        DefaultPraticaValidazione pratica = praticheInCorso.get(praticaId);
        if (pratica != null) {
            return Optional.of(pratica);
        }
        return codaValidazione.getPraticaById(praticaId);
    }

    @Override
    public List<DefaultPraticaValidazione> getPratichePendenti() {
        return codaValidazione.getPratichePendenti();
    }

    @Override
    public List<DefaultPraticaValidazione> getPratichePerCuratore(UUID curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");

        return praticheInCorso.values().stream()
                .filter(pratica -> curatoreId.equals(pratica.getCuratoreAssegnato()))
                .filter(pratica -> !pratica.isCompletata())
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> getCuratoriLiberi() {
        return statusTracker.getCuratoriLiberi();
    }

    @Override
    public void liberaCuratore(UUID curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        assegnatore.onCuratoreLibero(curatoreId);
    }

    private DefaultPraticaValidazione validaEOttieniPratica(UUID praticaId, UUID curatoreId) {
        Objects.requireNonNull(praticaId, "L'ID della pratica non può essere null");
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");

        DefaultPraticaValidazione pratica = praticheInCorso.get(praticaId);
        if (pratica == null) {
            throw new IllegalArgumentException("Pratica non trovata: " + praticaId);
        }

        if (!curatoreId.equals(pratica.getCuratoreAssegnato())) {
            throw new IllegalArgumentException("Il curatore " + curatoreId + " non è assegnato alla pratica " + praticaId);
        }

        return pratica;
    }

    private void rimuoviPraticaCompletata(UUID praticaId) {
        praticheInCorso.remove(praticaId);
    }
}