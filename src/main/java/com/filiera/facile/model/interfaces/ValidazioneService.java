package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultPraticaValidazione;
import com.filiera.facile.model.interfaces.Validabile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ValidazioneService {

    UUID sottomettiPerValidazione(Validabile contenuto, UUID richiedenteId);

    void registraCuratore(UUID curatoreId);

    void rimuoviCuratore(UUID curatoreId);

    void approvaPratica(UUID praticaId, UUID curatoreId, String noteValutazione);

    void respingiPratica(UUID praticaId, UUID curatoreId, String motivazioneRifiuto);

    void richiedeModifiche(UUID praticaId, UUID curatoreId, String noteModifiche);

    Optional<DefaultPraticaValidazione> getPraticaById(UUID praticaId);

    List<DefaultPraticaValidazione> getPratichePendenti();

    List<DefaultPraticaValidazione> getPratichePerCuratore(UUID curatoreId);

    List<UUID> getCuratoriLiberi();

    void liberaCuratore(UUID curatoreId);
}