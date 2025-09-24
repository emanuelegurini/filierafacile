package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.model.interfaces.ProdottoRepository;
import com.filiera.facile.model.interfaces.ProdottoService;
import com.filiera.facile.model.interfaces.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//@Service
@Transactional
public class DefaultProdottoService implements ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

    @Autowired
    public DefaultProdottoService(
            ProdottoRepository pr,
            UtenteRepository ur,
            AziendaRepository ar
    ) {
        this.prodottoRepository = pr;
        this.utenteRepository = ur;
        this.aziendaRepository = ar;
    }

    public DefaultProdotto creaNuovoProdotto(
            UUID idUtente,
            UUID idAzienda,
            DefaultProdotto prodotto
    ) {

        DefaultUtente utente = utenteRepository.findById(idUtente).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        DefaultAzienda azienda = aziendaRepository.findById(idAzienda).orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        verificaPermessoCreazione(utente, azienda);

        prodottoRepository.save(prodotto);
        System.out.println("INFO: Creato nuovo prodotto '" + prodotto.getNomeArticolo() + "' per azienda " + azienda.getRagioneSociale());
        return prodotto;
    }


    /*
     * Questa funzione verifica se l'utente ha effettivamente i permessi per creare un prodotto.
     * Solo gli ADMIN e i GESTORE_PRODOTTI di un azienda possono creare prodotti.
     */
    private void verificaPermessoCreazione(DefaultUtente utente, DefaultAzienda azienda) {
        boolean haPermesso = utente.getAffiliazioni().stream()
                .anyMatch(aff ->
                        aff.getAzienda().getId().equals(azienda.getId()) &&
                                (aff.getRuoloAziendale() == RuoloAziendale.ADMIN ||
                                        aff.getRuoloAziendale() == RuoloAziendale.GESTORE_PRODOTTI)
                );

        if (!haPermesso) {
            throw new SecurityException("L'utente " + utente.getNome() + " non ha i permessi per creare prodotti per l'azienda " + azienda.getRagioneSociale());
        }
    }
}
