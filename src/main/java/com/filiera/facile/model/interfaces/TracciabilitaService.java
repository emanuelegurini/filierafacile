package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultProdotto;

import java.util.List;
import java.util.UUID;

public interface TracciabilitaService {

    List<DefaultProdotto> calcolaFilieraCompleta(UUID prodottoId);

    List<DefaultAzienda> getAziendeCoinvolte(UUID prodottoId);

    List<DefaultProdotto> getProdottiOrigine(UUID prodottoId);

    boolean isMateriaPrima(UUID prodottoId);

    int getProfonditaFiliera(UUID prodottoId);
}