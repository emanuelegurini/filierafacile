package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAffiliazione;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.repositories.AffiliazionRepository;
import com.filiera.facile.repositories.AziendaRepository;
import com.filiera.facile.model.interfaces.UserService;
import com.filiera.facile.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class DefaultUtenteService implements UserService {

    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;
    private final AffiliazionRepository affiliazionRepository;

    @Autowired
    public DefaultUtenteService(UtenteRepository utenteRepository, AziendaRepository aziendaRepository, AffiliazionRepository affiliazionRepository) {
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
        this.affiliazionRepository = affiliazionRepository;
    }

    @Override
    public DefaultUtente registraNuovoUtente(DefaultUtente utente) {

        utenteRepository.findByEmail(utente.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email già registrata.");
        });

        utenteRepository.save(utente);
        System.out.println("INFO: Nuovo utente registrato con ID: " + utente.getId() + ": " + utente.getNome());

        return utente;
    }

    @Override
    public void aggiungiAffiliazione(Long utenteId, Long aziendaId, RuoloAziendale ruolo){
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));

        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + aziendaId));


        DefaultAffiliazione affiliazione = new DefaultAffiliazione(utente, azienda, ruolo);

        affiliazionRepository.save(affiliazione);

        utente.addAffiliazione(affiliazione);

        System.out.println("INFO: Aggiunta affiliazione per utente " + utenteId + " ad azienda " + aziendaId);
    }

    public void aggiornaRuoloAffiliazione(Long utenteId, Long aziendaId, RuoloAziendale nuovoRuolo) {
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));

        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + aziendaId));

        DefaultAffiliazione affiliazioneDaAggiornare = utente.getAffiliazioni().stream()
                .filter(aff -> aff.getAzienda().getId().equals(azienda.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("L'utente non ha un'affiliazione con l'azienda specificata."));

        affiliazioneDaAggiornare.setRuoloAziendale(nuovoRuolo);
        System.out.println("INFO: Ruolo per utente " + utenteId + " aggiornato a " + nuovoRuolo + " per azienda " + azienda.getId());

        utenteRepository.save(utente);
    }
}