package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultEvento;

import java.util.List;
import java.util.UUID;

public interface EventoService {

    DefaultEvento creaNuovoEvento(Long organizzatoreId, DefaultEvento evento);

    void aggiungiAziendaAdEvento(Long eventoId, Long aziendaId);

    void rimuoviAziendaDaEvento(Long eventoId, Long aziendaId);

    List<DefaultEvento> trovaEventiPerAzienda(Long aziendaId);
}
