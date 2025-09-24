package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAssegnatoreAutomatico;
import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultValidazioneService implements ValidazioneService {
    private final CodaValidazione codaValidazione;
    private final CuratoreStatusTracker statusTracker;
    private final DefaultAssegnatoreAutomatico assegnatore;
    private final Map<Long, DefaultPraticaValidazione> praticheInCorso;

    @Autowired
    public DefaultValidazioneService(CodaValidazione codaValidazione, CuratoreStatusTracker statusTracker) {
        this.codaValidazione = Objects.requireNonNull(codaValidazione, "La coda di validazione non può essere null");
        this.statusTracker = Objects.requireNonNull(statusTracker, "Il tracker degli stati non può essere null");
        this.assegnatore = new DefaultAssegnatoreAutomatico(codaValidazione, statusTracker);
        this.praticheInCorso = new HashMap<>();
    }

    @Override
    public Long sottomettiPerValidazione(Validabile contenuto, Long richiedenteId) {
        Objects.requireNonNull(contenuto, "Il contenuto da validare non può essere null");
        Objects.requireNonNull(richiedenteId, "L'ID del richiedente non può essere null");

        DefaultPraticaValidazione pratica = new DefaultPraticaValidazione(contenuto, richiedenteId);
        praticheInCorso.put(pratica.getId(), pratica);

        assegnatore.onNuovaRichiestaValidazione(pratica);

        return pratica.getId();
    }

    @Override
    public void registraCuratore(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        statusTracker.registraCuratore(curatoreId);
    }

    @Override
    public void rimuoviCuratore(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        statusTracker.rimuoviCuratore(curatoreId);
    }

    @Override
    public void approvaPratica(Long praticaId, Long curatoreId, String noteValutazione) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.approva(noteValutazione);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public void respingiPratica(Long praticaId, Long curatoreId, String motivazioneRifiuto) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.respingi(motivazioneRifiuto);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public void richiedeModifiche(Long praticaId, Long curatoreId, String noteModifiche) {
        DefaultPraticaValidazione pratica = validaEOttieniPratica(praticaId, curatoreId);

        StatoValidazione vecchioStato = pratica.getStatoCorrente();
        pratica.richiedeModifiche(noteModifiche);

        assegnatore.onCambioStato(pratica, vecchioStato, pratica.getStatoCorrente());
        rimuoviPraticaCompletata(praticaId);
    }

    @Override
    public Optional<DefaultPraticaValidazione> getPraticaById(Long praticaId) {
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
    public List<DefaultPraticaValidazione> getPratichePerCuratore(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");

        return praticheInCorso.values().stream()
                .filter(pratica -> curatoreId.equals(pratica.getCuratoreAssegnato()))
                .filter(pratica -> !pratica.isCompletata())
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCuratoriLiberi() {
        return statusTracker.getCuratoriLiberi();
    }

    @Override
    public void liberaCuratore(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        assegnatore.onCuratoreLibero(curatoreId);
    }

    private DefaultPraticaValidazione validaEOttieniPratica(Long praticaId, Long curatoreId) {
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

    private void rimuoviPraticaCompletata(Long praticaId) {
        praticheInCorso.remove(praticaId);
    }
}