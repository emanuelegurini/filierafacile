package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultUtente;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository {
    void save(DefaultUtente utente);

    Optional<DefaultUtente> findById(UUID id);

    Optional<DefaultUtente> findByEmail(String email);
}
