package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultProdotto;

import java.util.List;

public interface TracciabilitaService {

    List<DefaultProdotto> calcolaFilieraCompleta(Long prodottoId);

    List<DefaultAzienda> getAziendeCoinvolte(Long prodottoId);

    List<DefaultProdotto> getProdottiOrigine(Long prodottoId);

    boolean isMateriaPrima(Long prodottoId);

    int getProfonditaFiliera(Long prodottoId);
}