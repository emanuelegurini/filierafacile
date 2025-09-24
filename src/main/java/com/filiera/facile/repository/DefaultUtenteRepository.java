package com.filiera.facile.repository;

import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.interfaces.UtenteRepository;
import com.filiera.facile.repositories.UtenteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultUtenteRepository implements UtenteRepository {

    private final UtenteJpaRepository utenteJpaRepository;

    @Autowired
    public DefaultUtenteRepository(UtenteJpaRepository utenteJpaRepository) {
        this.utenteJpaRepository = utenteJpaRepository;
    }

    @Override
    public void save(DefaultUtente utente) {
        boolean isUpdate = utente.getId() != null && utenteJpaRepository.existsById(utente.getId());

        if (isUpdate) {
            System.out.println("INFO: Aggiornamento dell'utente '" + utente.getEmail() + "' nel database.");
        } else {
            System.out.println("INFO: Creazione del nuovo utente '" + utente.getEmail() + "' nel database.");
        }

        utenteJpaRepository.save(utente);
    }

    @Override
    public Optional<DefaultUtente> findById(Long id) {
        return utenteJpaRepository.findById(id);
    }

    @Override
    public Optional<DefaultUtente> findByEmail(String email) {
        return utenteJpaRepository.findByEmail(email);
    }
}
