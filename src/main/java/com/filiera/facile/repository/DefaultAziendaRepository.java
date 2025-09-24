package com.filiera.facile.repository;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.repositories.AziendaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultAziendaRepository implements AziendaRepository {

    private final AziendaJpaRepository aziendaJpaRepository;

    @Autowired
    public DefaultAziendaRepository(AziendaJpaRepository aziendaJpaRepository) {
        this.aziendaJpaRepository = aziendaJpaRepository;
    }

    @Override
    public void save (DefaultAzienda defaultAzienda) {
        System.out.println("INFO: Salvataggio dell'azienda '" + defaultAzienda.getRagioneSociale() + "' nel database.");
        aziendaJpaRepository.save(defaultAzienda);
    }

    @Override
    public Optional<DefaultAzienda> findById(UUID id) {
        return aziendaJpaRepository.findById(id);
    }

    @Override
    public Optional<DefaultAzienda> findByPartitaIva(String partitaIva) {
        return aziendaJpaRepository.findByPartitaIva(partitaIva);
    }
}