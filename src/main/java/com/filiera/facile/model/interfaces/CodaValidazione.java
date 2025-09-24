package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultPraticaValidazione;

import java.util.List;
import java.util.Optional;

public interface CodaValidazione {

    void aggiungiPratica(DefaultPraticaValidazione pratica);

    Optional<DefaultPraticaValidazione> ottieniProssimaPratica();

    void rimuoviPratica(Long praticaId);

    List<DefaultPraticaValidazione> getPratichePendenti();

    int size();

    boolean isEmpty();

    Optional<DefaultPraticaValidazione> getPraticaById(Long praticaId);
}