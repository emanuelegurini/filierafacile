package com.filiera.facile.application.services;

import com.filiera.facile.application.dto.CreazioneProdottoDTO;
import com.filiera.facile.domain.DefaultAzienda;
import com.filiera.facile.domain.DefaultProdotto;
import com.filiera.facile.domain.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.interfaces.AziendaRepository;
import com.filiera.facile.model.interfaces.ProdottoRepository;
import com.filiera.facile.model.interfaces.ProdottoService;
import com.filiera.facile.model.interfaces.UtenteRepository;

import java.util.List;
import java.util.UUID;

public class DefaultProdottoService implements ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

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
            CreazioneProdottoDTO dati
    ) {

        validaInputProdotto(dati);

        DefaultUtente utente = utenteRepository.findById(idUtente).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        DefaultAzienda azienda = aziendaRepository.findById(idAzienda).orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        verificaPermessoCreazione(utente, azienda);

        DefaultProdotto nuovoProdotto = new DefaultProdotto(
                dati.nome(), dati.descrizione(), dati.prezzoUnitario(),
                dati.unitaDiMisura(), azienda, dati.tipoProdotto(), dati.categoria()
        );

        configuraProdottoPerTipo(nuovoProdotto, dati);

        prodottoRepository.save(nuovoProdotto);
        System.out.println("INFO: Creato nuovo prodotto '" + nuovoProdotto.getNomeArticolo() + "' per azienda " + azienda.getRagioneSociale());
        return nuovoProdotto;
    }

    private void validaInputProdotto(CreazioneProdottoDTO dati) {
        if (dati.tipoProdotto() == TipoProdotto.MATERIA_PRIMA && dati.ingredientiIds() != null && !dati.ingredientiIds().isEmpty()) {
            throw new IllegalArgumentException("Una materia prima non può avere ingredienti.");
        }
    }

    /*
     * Nonostante la classe DefaultProdotto implementi dei controlli sul tipo di prodotto (MATERIA_PRIMA o TRASFORMATO),
     * in quanto la classe è essa stessa responsabile di proteggere la propria coerenza,
     * il servizio implementa comunque degli ulteriori controlli:
     * - se il prodotto è una materia prima, allora non ha ingredienti. Ma specifichiamo il metodo di coltivazione
     * - se il prodotto è un trasformato, allora specifichiamo la lista degli ingredienti, ossia altri prodotti
     *   che possono essere sia materie prime che traformati
     */
    private void configuraProdottoPerTipo(DefaultProdotto prodotto, CreazioneProdottoDTO dati) {
        if (prodotto.getTipoProdotto() == TipoProdotto.MATERIA_PRIMA) {
            prodotto.setMetodoColtivazione(dati.metodoColtivazione());
        } else if (prodotto.getTipoProdotto() == TipoProdotto.TRASFORMATO) {
            prodotto.setMetodoTrasformazione(dati.metodoTrasformazione());
            aggiungiIngredientiSePresenti(prodotto, dati.ingredientiIds());
        }
    }

    private void aggiungiIngredientiSePresenti(DefaultProdotto prodotto, List<UUID> ingredientiIds) {
        if (ingredientiIds != null && !ingredientiIds.isEmpty()) {
            ingredientiIds.stream()
                    .map(ingId -> prodottoRepository.findById(ingId)
                            .orElseThrow(() -> new RuntimeException("Ingrediente non trovato con ID: " + ingId)))
                    .forEach(prodotto::aggiungiIngrediente);
        }
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
