package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloPiattaforma;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.model.interfaces.EventoRepository;
import com.filiera.facile.model.interfaces.EventoService;
import com.filiera.facile.model.interfaces.UtenteRepository;

import java.util.List;
import java.util.UUID;

public class DefaultEventoService implements EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

    public DefaultEventoService(EventoRepository er, UtenteRepository ur, AziendaRepository ar) {
        this.eventoRepository = er;
        this.utenteRepository = ur;
        this.aziendaRepository = ar;
    }

    @Override
    public DefaultEvento creaNuovoEvento(UUID organizzatoreId, DefaultEvento evento) {
        DefaultUtente organizzatore = utenteRepository.findById(organizzatoreId)
                .orElseThrow(() -> new RuntimeException("Utente organizzatore non trovato."));

        if (!organizzatore.getRuoli().contains(RuoloPiattaforma.ANIMATORE_FILIERA)) {
            throw new SecurityException("L'utente non ha i permessi per creare un evento.");
        }

        eventoRepository.save(evento);
        return evento;
    }

    @Override
    public void aggiungiAziendaAdEvento(UUID eventoId, UUID aziendaId) {
        DefaultEvento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));
        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata."));

        evento.aggiungiAziendaPartecipante(azienda);
        eventoRepository.save(evento);
    }

    @Override
    public void rimuoviAziendaDaEvento(UUID eventoId, UUID aziendaId) {
        DefaultEvento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));
        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata."));

        evento.rimuoviAziendaPartecipante(azienda);
        eventoRepository.save(evento);
    }

    @Override
    public List<DefaultEvento> trovaEventiPerAzienda(UUID aziendaId) {
        return eventoRepository.findByAziendaPartecipante(aziendaId);
    }
}
