package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.entities.ArticoloCatalogo;
import com.filiera.facile.repositories.ProdottoRepository;
import com.filiera.facile.model.interfaces.TracciabilitaService;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultTracciabilitaService implements TracciabilitaService {
    private final ProdottoRepository prodottoRepository;

    public DefaultTracciabilitaService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = Objects.requireNonNull(prodottoRepository, "Il repository dei prodotti non può essere null");
    }

    @Override
    public List<DefaultProdotto> calcolaFilieraCompleta(Long prodottoId) {
        Objects.requireNonNull(prodottoId, "L'ID del prodotto non può essere null");

        DefaultProdotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        return calcolaFilieraCompleta(prodotto);
    }

    @Override
    public List<DefaultAzienda> getAziendeCoinvolte(Long prodottoId) {
        List<DefaultProdotto> filiera = calcolaFilieraCompleta(prodottoId);

        return filiera.stream()
                .map(DefaultProdotto::getAziendaProduttrice)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultProdotto> getProdottiOrigine(Long prodottoId) {
        List<DefaultProdotto> filiera = calcolaFilieraCompleta(prodottoId);

        return filiera.stream()
                .filter(prodotto -> prodotto.getTipoProdotto() == TipoProdotto.MATERIA_PRIMA)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isMateriaPrima(Long prodottoId) {
        Objects.requireNonNull(prodottoId, "L'ID del prodotto non può essere null");

        DefaultProdotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        return prodotto.getTipoProdotto() == TipoProdotto.MATERIA_PRIMA;
    }

    @Override
    public int getProfonditaFiliera(Long prodottoId) {
        Objects.requireNonNull(prodottoId, "L'ID del prodotto non può essere null");

        DefaultProdotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + prodottoId));

        return calcolaProfonditaRicorsiva(prodotto);
    }

    private List<DefaultProdotto> calcolaFilieraCompleta(DefaultProdotto prodotto) {
        List<DefaultProdotto> filiera = new ArrayList<>();
        Set<Long> prodottiVisitati = new HashSet<>();

        raccogliFilieraRicorsiva(prodotto, filiera, prodottiVisitati);

        return filiera;
    }

    private void raccogliFilieraRicorsiva(DefaultProdotto prodotto, List<DefaultProdotto> filiera, Set<Long> prodottiVisitati) {
        if (prodotto == null || prodottiVisitati.contains(prodotto.getId())) {
            return;
        }

        prodottiVisitati.add(prodotto.getId());
        filiera.add(prodotto);

        for (ArticoloCatalogo ingrediente : prodotto.getIngredienti()) {
            if (ingrediente instanceof DefaultProdotto) {
                raccogliFilieraRicorsiva((DefaultProdotto) ingrediente, filiera, prodottiVisitati);
            }
        }
    }

    private int calcolaProfonditaRicorsiva(DefaultProdotto prodotto) {
        if (prodotto.getTipoProdotto() == TipoProdotto.MATERIA_PRIMA) {
            return 1;
        }

        if (prodotto.getIngredienti().isEmpty()) {
            return 1;
        }

        int profonditaMassima = 0;
        for (ArticoloCatalogo ingrediente : prodotto.getIngredienti()) {
            if (ingrediente instanceof DefaultProdotto) {
                int profonditaIngrediente = calcolaProfonditaRicorsiva((DefaultProdotto) ingrediente);
                profonditaMassima = Math.max(profonditaMassima, profonditaIngrediente);
            }
        }

        return profonditaMassima + 1;
    }
}