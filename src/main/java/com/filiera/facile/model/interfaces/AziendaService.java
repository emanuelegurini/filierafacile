package com.filiera.facile.model.interfaces;

import com.filiera.facile.application.dto.CreazioneAziendaDTO;
import com.filiera.facile.domain.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;

import java.util.UUID;

public interface AziendaService {

    DefaultAzienda creaNuovaAzienda(CreazioneAziendaDTO datiAzienda);

    void aggiungiTipoAzienda(UUID aziendaId, TipoAzienda tipoAzienda);
}

