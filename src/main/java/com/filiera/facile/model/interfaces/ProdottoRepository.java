package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultProdotto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdottoRepository {
    void save(DefaultProdotto defaultProdotto);

    Optional<DefaultProdotto> findById(UUID id);

    List<DefaultProdotto> findAll();
}
