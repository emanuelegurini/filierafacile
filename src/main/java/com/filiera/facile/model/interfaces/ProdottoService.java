package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultProdotto;

import java.util.UUID;

public interface ProdottoService {
    DefaultProdotto creaNuovoProdotto(UUID idUtente, UUID idAzienda, DefaultProdotto prodotto);
}
