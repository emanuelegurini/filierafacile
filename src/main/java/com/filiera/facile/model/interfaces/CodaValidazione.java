package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultPraticaValidazione;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodaValidazione {

    void aggiungiPratica(DefaultPraticaValidazione pratica);

    Optional<DefaultPraticaValidazione> ottieniProssimaPratica();

    void rimuoviPratica(UUID praticaId);

    List<DefaultPraticaValidazione> getPratichePendenti();

    int size();

    boolean isEmpty();

    Optional<DefaultPraticaValidazione> getPraticaById(UUID praticaId);
}