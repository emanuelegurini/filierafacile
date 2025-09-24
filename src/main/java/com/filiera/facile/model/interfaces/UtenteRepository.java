package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultUtente;

import java.util.Optional;

public interface UtenteRepository {
    void save(DefaultUtente utente);

    Optional<DefaultUtente> findById(Long id);

    Optional<DefaultUtente> findByEmail(String email);
}
