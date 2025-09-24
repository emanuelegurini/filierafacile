package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.model.interfaces.AziendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//@Service
@Transactional
public class DefaultAziendaService implements AziendaService {

    private final AziendaRepository aziendaRepository;

    @Autowired
    public DefaultAziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    @Override
    public DefaultAzienda creaNuovaAzienda(DefaultAzienda azienda) {

        aziendaRepository.findByPartitaIva(azienda.getPartitaIva()).ifPresent(a -> {
            throw new IllegalArgumentException("Un'azienda con questa Partita IVA è già registrata.");
        });

        aziendaRepository.save(azienda);
        System.out.println("INFO: Nuova azienda registrata con ID: " + azienda.getId() + ": " + azienda.getRagioneSociale());

        return azienda;
    }

    @Override
    public void aggiungiTipoAzienda(UUID aziendaId, TipoAzienda tipoAzienda) {
        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + aziendaId));

        azienda.aggiungiTipoAzienda(tipoAzienda);

        aziendaRepository.save(azienda);
        System.out.println("INFO: Aggiunto tipo " + tipoAzienda + " ad azienda " + aziendaId);
    }

}
