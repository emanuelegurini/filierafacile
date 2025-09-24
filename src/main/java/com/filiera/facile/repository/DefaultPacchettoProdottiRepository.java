package com.filiera.facile.repository;

import com.filiera.facile.entities.DefaultPacchettoProdotti;
import com.filiera.facile.repositories.PacchettoProdottiJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DefaultPacchettoProdottiRepository {

    private final PacchettoProdottiJpaRepository pacchettoProdottiJpaRepository;

    @Autowired
    public DefaultPacchettoProdottiRepository(PacchettoProdottiJpaRepository pacchettoProdottiJpaRepository) {
        this.pacchettoProdottiJpaRepository = pacchettoProdottiJpaRepository;
    }

    public DefaultPacchettoProdotti save(DefaultPacchettoProdotti pacchetto) {
        System.out.println("INFO: Salvataggio del pacchetto '" + pacchetto.getNomeArticolo() + "' nel database.");
        return pacchettoProdottiJpaRepository.save(pacchetto);
    }

    public Optional<DefaultPacchettoProdotti> findById(UUID id) {
        return pacchettoProdottiJpaRepository.findById(id);
    }

    public List<DefaultPacchettoProdotti> findAll() {
        return pacchettoProdottiJpaRepository.findAll();
    }

    public void deleteById(UUID id) {
        pacchettoProdottiJpaRepository.deleteById(id);
    }
}
