package com.filiera.facile.repository;

import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.interfaces.ProdottoRepository;
import com.filiera.facile.repositories.ProdottoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultProdottoRepository implements ProdottoRepository {

    private final ProdottoJpaRepository prodottoJpaRepository;

    @Autowired
    public DefaultProdottoRepository(ProdottoJpaRepository prodottoJpaRepository) {
        this.prodottoJpaRepository = prodottoJpaRepository;
    }

    @Override
    public void save(DefaultProdotto defaultProdotto) {
        System.out.println("INFO: Salvataggio del prodotto '" + defaultProdotto.getNomeArticolo() + "' nel database.");
        prodottoJpaRepository.save(defaultProdotto);
    }

    @Override
    public Optional<DefaultProdotto> findById(UUID id) {
        return prodottoJpaRepository.findById(id);
    }

    @Override
    public List<DefaultProdotto> findAll() {
        return prodottoJpaRepository.findAll();
    }
}
