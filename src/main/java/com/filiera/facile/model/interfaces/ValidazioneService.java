package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.interfaces.Validabile;

import java.util.List;
import java.util.Optional;

public interface ValidazioneService {

    Long sottomettiPerValidazione(Validabile contenuto, Long richiedenteId);

    void registraCuratore(Long curatoreId);

    void rimuoviCuratore(Long curatoreId);

    void approvaPratica(Long praticaId, Long curatoreId, String noteValutazione);

    void respingiPratica(Long praticaId, Long curatoreId, String motivazioneRifiuto);

    void richiedeModifiche(Long praticaId, Long curatoreId, String noteModifiche);

    Optional<DefaultPraticaValidazione> getPraticaById(Long praticaId);

    List<DefaultPraticaValidazione> getPratichePendenti();

    List<DefaultPraticaValidazione> getPratichePerCuratore(Long curatoreId);

    List<Long> getCuratoriLiberi();

    void liberaCuratore(Long curatoreId);
}