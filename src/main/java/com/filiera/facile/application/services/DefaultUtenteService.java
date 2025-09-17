package com.filiera.facile.application.services;

import com.filiera.facile.domain.DefaultAffiliazione;
import com.filiera.facile.domain.DefaultAzienda;
import com.filiera.facile.domain.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.model.interfaces.UserService;
import com.filiera.facile.model.interfaces.UtenteRepository;

import java.util.UUID;

public class DefaultUtenteService implements UserService {

    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

    public DefaultUtenteService(UtenteRepository utenteRepository, AziendaRepository aziendaRepository) {
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
    }

    @Override
    public DefaultUtente registraNuovoUtente(DefaultUtente utente) {

        /*
         * Come prima cosa, prima di registrare un nuovo utente,
         * il metodo controlla che esista già un utente registrato
         * con la medesima email.
         */
        utenteRepository.findByEmail(utente.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email già registrata.");
        });

        utenteRepository.save(utente);
        System.out.println("INFO: Nuovo utente registrato con ID: " + utente.getId() + ": " + utente.getNome());

        return utente;
    }

    @Override
    public void aggiungiAffiliazione(UUID utenteId, UUID aziendaId, RuoloAziendale ruolo){
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));

        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + aziendaId));


        DefaultAffiliazione affiliazione = new DefaultAffiliazione(utente, azienda, ruolo);
        utente.addAffiliazione(affiliazione);

        utenteRepository.save(utente);
        System.out.println("INFO: Aggiunta affiliazione per utente " + utenteId + " ad azienda " + aziendaId);
    }

    /*
     * Con questo metodo recuperiamo le affiliazioni da un utete, cerchiamo l'affiliazione a una determinata azienda
     * e la modifichiamo aggiornando il ruolo dello user per quella determinata azienda.
     */
    public void aggiornaRuoloAffiliazione(UUID utenteId, UUID aziendaId, RuoloAziendale nuovoRuolo) {
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