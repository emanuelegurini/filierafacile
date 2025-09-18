package com.filiera.facile.domain;

import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.CodaValidazione;
import com.filiera.facile.model.interfaces.CuratoreStatusTracker;
import com.filiera.facile.model.interfaces.ValidazioneListener;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class DefaultAssegnatoreAutomatico implements ValidazioneListener {
    private final CodaValidazione codaValidazione;
    private final CuratoreStatusTracker statusTracker;

    public DefaultAssegnatoreAutomatico(CodaValidazione codaValidazione, CuratoreStatusTracker statusTracker) {
        this.codaValidazione = Objects.requireNonNull(codaValidazione, "La coda di validazione non può essere null");
        this.statusTracker = Objects.requireNonNull(statusTracker, "Il tracker degli stati non può essere null");
    }

    @Override
    public void onNuovaRichiestaValidazione(DefaultPraticaValidazione pratica) {
        Objects.requireNonNull(pratica, "La pratica non può essere null");

        codaValidazione.aggiungiPratica(pratica);

        tentaAssegnazioneAutomatica();
    }

    @Override
    public void onCuratoreLibero(UUID curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");

        statusTracker.setCuratoreLibero(curatoreId);

        tentaAssegnazioneAutomatica();
    }

    @Override
    public void onCambioStato(DefaultPraticaValidazione pratica, StatoValidazione vecchioStato, StatoValidazione nuovoStato) {
        if (StatoValidazione.IN_REVISIONE.equals(vecchioStato) &&
            (StatoValidazione.APPROVATO.equals(nuovoStato) ||
             StatoValidazione.RESPINTO.equals(nuovoStato) ||
             StatoValidazione.MODIFICHE_RICHIESTE.equals(nuovoStato))) {

            liberaCuratore(pratica.getCuratoreAssegnato());
        }
    }

    private void tentaAssegnazioneAutomatica() {
        Optional<UUID> curatoreLibero = statusTracker.getPrimoCuratoreLibero();

        if (curatoreLibero.isPresent()) {
            Optional<DefaultPraticaValidazione> prossimaPratica = codaValidazione.ottieniProssimaPratica();

            if (prossimaPratica.isPresent()) {
                UUID curatoreId = curatoreLibero.get();
                DefaultPraticaValidazione pratica = prossimaPratica.get();

                assegnaPratica(pratica, curatoreId);
            }
        }
    }

    private void assegnaPratica(DefaultPraticaValidazione pratica, UUID curatoreId) {
        pratica.assegnaCuratore(curatoreId);
        statusTracker.setCuratoreOccupato(curatoreId);

        System.out.println("Pratica " + pratica.getId() + " assegnata automaticamente al curatore " + curatoreId);
    }

    private void liberaCuratore(UUID curatoreId) {
        if (curatoreId != null) {
            statusTracker.setCuratoreLibero(curatoreId);

            tentaAssegnazioneAutomatica();
        }
    }
}