package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.enums.StatoValidazione;

import java.util.UUID;

public interface ValidazioneListener {

    void onNuovaRichiestaValidazione(DefaultPraticaValidazione pratica);

    void onCuratoreLibero(UUID curatoreId);

    void onCambioStato(DefaultPraticaValidazione pratica, StatoValidazione vecchioStato, StatoValidazione nuovoStato);
}