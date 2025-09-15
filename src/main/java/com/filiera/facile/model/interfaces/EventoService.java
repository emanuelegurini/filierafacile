package com.filiera.facile.model.interfaces;

import com.filiera.facile.application.dto.CreazioneEventoDTO;
import com.filiera.facile.domain.DefaultEvento;

import java.util.List;
import java.util.UUID;

public interface EventoService {

    DefaultEvento creaNuovoEvento(UUID organizzatoreId, CreazioneEventoDTO datiEvento);

    void aggiungiAziendaAdEvento(UUID eventoId, UUID aziendaId);

    void rimuoviAziendaDaEvento(UUID eventoId, UUID aziendaId);

    List<DefaultEvento> trovaEventiPerAzienda(UUID aziendaId);
}
