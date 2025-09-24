package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.enums.StatoValidazione;


public interface ValidazioneListener {

    void onNuovaRichiestaValidazione(DefaultPraticaValidazione pratica);

    void onCuratoreLibero(Long curatoreId);

    void onCambioStato(DefaultPraticaValidazione pratica, StatoValidazione vecchioStato, StatoValidazione nuovoStato);
}