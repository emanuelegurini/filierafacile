package com.filiera.facile.model.interfaces;

import com.filiera.facile.application.dto.CreazioneProdottoDTO;
import com.filiera.facile.domain.DefaultProdotto;

import java.util.UUID;

public interface ProdottoService {
    DefaultProdotto creaNuovoProdotto(UUID idAttivo, UUID idAzienda, CreazioneProdottoDTO datiProdotto);
}
