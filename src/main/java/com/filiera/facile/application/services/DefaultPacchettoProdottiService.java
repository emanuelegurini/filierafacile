package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAffiliazione;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultPacchettoProdotti;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.repositories.AffiliazionRepository;
import com.filiera.facile.repositories.AziendaRepository;
import com.filiera.facile.repositories.PacchettoProdottiRepository;
import com.filiera.facile.repositories.ProdottoRepository;
import com.filiera.facile.repositories.UtenteRepository;
import com.filiera.facile.utils.ScorteInsufficientiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class DefaultPacchettoProdottiService {

    private final PacchettoProdottiRepository pacchettoRepository;
    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;
    private final AffiliazionRepository affiliazionRepository;

    @Autowired
    public DefaultPacchettoProdottiService(
            PacchettoProdottiRepository pacchettoRepository,
            ProdottoRepository prodottoRepository,
            UtenteRepository utenteRepository,
            AziendaRepository aziendaRepository,
            AffiliazionRepository affiliazionRepository
    ) {
        this.pacchettoRepository = Objects.requireNonNull(pacchettoRepository);
        this.prodottoRepository = Objects.requireNonNull(prodottoRepository);
        this.utenteRepository = Objects.requireNonNull(utenteRepository);
        this.aziendaRepository = Objects.requireNonNull(aziendaRepository);
        this.affiliazionRepository = Objects.requireNonNull(affiliazionRepository);
    }

    /**
     * Crea un nuovo pacchetto vuoto associato a un'azienda.
     */
    public DefaultPacchettoProdotti creaNuovoPacchetto(Long utenteId, Long aziendaId, String nome, String descrizione) {
        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, aziendaId);
        DefaultPacchettoProdotti nuovoPacchetto = new DefaultPacchettoProdotti(nome, descrizione, azienda);
        return pacchettoRepository.save(nuovoPacchetto);
    }

    /**
     * Aggiunge una data quantità di un prodotto a un pacchetto.
     * Controlla la disponibilità nel magazzino e aggiorna le scorte dell'azienda.
     */
    public DefaultPacchettoProdotti aggiungiProdottoAlPacchetto(Long utenteId, Long pacchettoId, Long prodottoId, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva.");
        }

        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaAggiungere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        // Validazione: solo prodotti approvati possono essere aggiunti ai pacchetti
        if (prodottoDaAggiungere.getStatoValidazione() != StatoValidazione.APPROVATO) {
            throw new IllegalStateException(
                "Impossibile aggiungere il prodotto '" + prodottoDaAggiungere.getNomeArticolo() +
                "' al pacchetto. Il prodotto deve essere approvato da un curatore prima di poter essere incluso in un pacchetto. " +
                "Stato attuale: " + prodottoDaAggiungere.getStatoValidazione()
            );
        }

        pacchetto.aggiungiProdotto(prodottoDaAggiungere, quantita);

        aziendaRepository.save(azienda);
        return pacchettoRepository.save(pacchetto);
    }

    /**
     * Rimuove un prodotto da un pacchetto e restituisce le scorte al magazzino dell'azienda.
     */
    public DefaultPacchettoProdotti rimuoviProdottoDalPacchetto(Long utenteId, Long pacchettoId, Long prodottoId) {
        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaRimuovere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        int quantitaDaRestituire = pacchetto.getProdottiInclusi().stream()
                .filter(pp -> pp.getArticolo().getId().equals(prodottoId))
                .mapToInt(pp -> pp.getQuantita())
                .findFirst()
                .orElse(0);

        if (quantitaDaRestituire > 0) {
            pacchetto.rimuoviProdotto(prodottoDaRimuovere);

            azienda.aggiungiScorte(prodottoDaRimuovere, quantitaDaRestituire);

            aziendaRepository.save(azienda);
            return pacchettoRepository.save(pacchetto);
        }

        return pacchetto;
    }

    /**
     * Controlla che l'utente abbia il ruolo GESTORE_PRODOTTI per l'azienda specificata.
     */
    private DefaultAzienda checkUserPermissionAndGetAzienda(Long utenteId, Long aziendaId) {
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new SecurityException("Utente non trovato con ID: " + utenteId));

        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new NoSuchElementException("Azienda non trovata con ID: " + aziendaId));

        // Controlla direttamente nel repository delle affiliazioni invece che nell'entità utente
        boolean isAuthorized = affiliazionRepository.findByDefaultUtenteAndDefaultAzienda(utente, azienda)
                .map(aff -> aff.getRuoloAziendale() == RuoloAziendale.GESTORE_PRODOTTI || aff.getRuoloAziendale() == RuoloAziendale.ADMIN)
                .orElse(false);

        if (!isAuthorized) {
            throw new SecurityException("L'utente " + utenteId + " non ha i permessi per gestire i prodotti dell'azienda " + aziendaId);
        }

        return azienda;
    }
}