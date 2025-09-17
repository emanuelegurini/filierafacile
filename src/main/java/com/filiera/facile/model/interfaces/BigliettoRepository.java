package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultBiglietto;
import com.filiera.facile.domain.DefaultEvento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BigliettoRepository {

    void save(DefaultBiglietto defaultBiglietto);

    Optional<DefaultBiglietto> findById(UUID id);

    List<DefaultBiglietto> findByEventoId(UUID eventoId);

    void delete(UUID id);
}