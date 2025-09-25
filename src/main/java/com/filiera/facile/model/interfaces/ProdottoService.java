package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultProdotto;

public interface ProdottoService {
    DefaultProdotto creaNuovoProdotto(Long idUtente, Long idAzienda, DefaultProdotto prodotto);
}
