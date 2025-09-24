package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultAzienda;

import java.util.Optional;
import java.util.UUID;

public interface AziendaRepository {
    void save(DefaultAzienda defaultAzienda);

    Optional<DefaultAzienda> findById(UUID id);

    Optional<DefaultAzienda> findByPartitaIva(String partitaIva);
}
