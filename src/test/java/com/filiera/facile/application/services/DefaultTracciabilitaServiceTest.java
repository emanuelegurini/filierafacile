package com.filiera.facile.application.services;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.entities.DefaultCoordinate;
import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.MetodoColtivazione;
import com.filiera.facile.model.enums.TipoAzienda;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;
import com.filiera.facile.repository.DefaultProdottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTracciabilitaServiceTest {

    private DefaultTracciabilitaService tracciabilitaService;
    private DefaultProdottoRepository prodottoRepository;

    private DefaultAzienda aziendaProduttore;
    private DefaultAzienda aziendaTrasformatore;
    private DefaultAzienda aziendaDistributore;

    private DefaultProdotto uva;
    private DefaultProdotto zucchero;
    private DefaultProdotto vino;
    private DefaultProdotto vinoInvecchiato;

    @BeforeEach
    void setUp() {
        prodottoRepository = new DefaultProdottoRepository();
        tracciabilitaService = new DefaultTracciabilitaService(prodottoRepository);

        setupAziende();
        setupProdotti();
        setupFiliera();
        salvaProdutti();
    }

    private void setupAziende() {
        DefaultCoordinate coordinate = new DefaultCoordinate(45.4642f, 9.1900f);

        aziendaProduttore = new DefaultAzienda(
                "Vigneto Rossi",
                "IT12345678901",
                "Via Vigneto 1",
                "info@vignetorossi.it",
                "123456789",
                "www.vignetorossi.it",
                coordinate
        );
        aziendaProduttore.aggiungiTipoAzienda(TipoAzienda.PRODUTTORE);

        aziendaTrasformatore = new DefaultAzienda(
                "Cantina Bianchi",
                "IT98765432109",
                "Via Cantina 2",
                "info@cantinabianchi.it",
                "987654321",
                "www.cantinabianchi.it",
                coordinate
        );
        aziendaTrasformatore.aggiungiTipoAzienda(TipoAzienda.TRASFORMATORE);

        aziendaDistributore = new DefaultAzienda(
                "Enoteca Verdi",
                "IT11223344556",
                "Via Enoteca 3",
                "info@enotecaverdi.it",
                "112233445",
                "www.enotecaverdi.it",
                coordinate
        );
        aziendaDistributore.aggiungiTipoAzienda(TipoAzienda.DISTRIBUTORE);
    }

    private void setupProdotti() {
        // Materie prime
        uva = new DefaultProdotto(
                "Uva Sangiovese",
                "Uva biologica da vigna storica",
                5.50,
                UnitaMisura.KG,
                aziendaProduttore,
                TipoProdotto.MATERIA_PRIMA,
                CategoriaProdotto.ORTOFRUTTA
        );
        uva.setMetodoColtivazione(MetodoColtivazione.BIOLOGICO);

        zucchero = new DefaultProdotto(
                "Zucchero di Canna",
                "Zucchero biologico",
                2.00,
                UnitaMisura.KG,
                aziendaProduttore,
                TipoProdotto.MATERIA_PRIMA,
                CategoriaProdotto.ALTRO
        );

        // Prodotti trasformati
        vino = new DefaultProdotto(
                "Vino Sangiovese",
                "Vino rosso della tradizione",
                15.00,
                UnitaMisura.LT,
                aziendaTrasformatore,
                TipoProdotto.TRASFORMATO,
                CategoriaProdotto.ALTRO
        );
        vino.setMetodoTrasformazione("Fermentazione tradizionale");

        vinoInvecchiato = new DefaultProdotto(
                "Vino Sangiovese Riserva",
                "Vino invecchiato 3 anni",
                35.00,
                UnitaMisura.LT,
                aziendaDistributore,
                TipoProdotto.TRASFORMATO,
                CategoriaProdotto.ALTRO
        );
        vinoInvecchiato.setMetodoTrasformazione("Invecchiamento in botti di rovere");
    }

    private void setupFiliera() {
        // Filiera: Uva + Zucchero → Vino → Vino Invecchiato
        vino.aggiungiIngrediente(uva);
        vino.aggiungiIngrediente(zucchero);

        vinoInvecchiato.aggiungiIngrediente(vino);
    }

    private void salvaProdutti() {
        prodottoRepository.save(uva);
        prodottoRepository.save(zucchero);
        prodottoRepository.save(vino);
        prodottoRepository.save(vinoInvecchiato);
    }

    @Test
    void testCalcolaFilieraMateriaPrima() {
        List<DefaultProdotto> filiera = tracciabilitaService.calcolaFilieraCompleta(uva.getId());

        assertEquals(1, filiera.size());
        assertTrue(filiera.contains(uva));
    }

    @Test
    void testCalcolaFilieraProdottoTrasformato() {
        List<DefaultProdotto> filiera = tracciabilitaService.calcolaFilieraCompleta(vino.getId());

        assertEquals(3, filiera.size());
        assertTrue(filiera.contains(vino));
        assertTrue(filiera.contains(uva));
        assertTrue(filiera.contains(zucchero));
    }

    @Test
    void testCalcolaFilieraComplessa() {
        List<DefaultProdotto> filiera = tracciabilitaService.calcolaFilieraCompleta(vinoInvecchiato.getId());

        assertEquals(4, filiera.size());
        assertTrue(filiera.contains(vinoInvecchiato));
        assertTrue(filiera.contains(vino));
        assertTrue(filiera.contains(uva));
        assertTrue(filiera.contains(zucchero));
    }

    @Test
    void testGetAziendeCoinvolte() {
        List<DefaultAzienda> aziende = tracciabilitaService.getAziendeCoinvolte(vinoInvecchiato.getId());

        assertEquals(3, aziende.size());
        assertTrue(aziende.contains(aziendaProduttore));
        assertTrue(aziende.contains(aziendaTrasformatore));
        assertTrue(aziende.contains(aziendaDistributore));
    }

    @Test
    void testGetProdottiOrigine() {
        List<DefaultProdotto> prodottiOrigine = tracciabilitaService.getProdottiOrigine(vinoInvecchiato.getId());

        assertEquals(2, prodottiOrigine.size());
        assertTrue(prodottiOrigine.contains(uva));
        assertTrue(prodottiOrigine.contains(zucchero));
    }

    @Test
    void testIsMateriaPrima() {
        assertTrue(tracciabilitaService.isMateriaPrima(uva.getId()));
        assertFalse(tracciabilitaService.isMateriaPrima(vino.getId()));
    }

    @Test
    void testGetProfonditaFiliera() {
        assertEquals(1, tracciabilitaService.getProfonditaFiliera(uva.getId()));
        assertEquals(2, tracciabilitaService.getProfonditaFiliera(vino.getId()));
        assertEquals(3, tracciabilitaService.getProfonditaFiliera(vinoInvecchiato.getId()));
    }

    @Test
    void testProdottoNonTrovato() {
        UUID idInesistente = UUID.randomUUID();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tracciabilitaService.calcolaFilieraCompleta(idInesistente));

        assertTrue(exception.getMessage().contains("Prodotto non trovato"));
    }

    @Test
    void testParametriNull() {
        assertThrows(NullPointerException.class, () ->
                tracciabilitaService.calcolaFilieraCompleta(null));

        assertThrows(NullPointerException.class, () ->
                tracciabilitaService.getAziendeCoinvolte(null));

        assertThrows(NullPointerException.class, () ->
                tracciabilitaService.isMateriaPrima(null));
    }

    @Test
    void testFilieraCircolarePrevenzione() {
        // Questo test verifica che non ci siano loop infiniti
        // se per errore ci fosse una dipendenza circolare
        DefaultProdotto prodottoA = new DefaultProdotto(
                "Prodotto A", "Test", 1.0, UnitaMisura.KG,
                aziendaProduttore, TipoProdotto.TRASFORMATO, CategoriaProdotto.ALTRO
        );

        DefaultProdotto prodottoB = new DefaultProdotto(
                "Prodotto B", "Test", 1.0, UnitaMisura.KG,
                aziendaProduttore, TipoProdotto.TRASFORMATO, CategoriaProdotto.ALTRO
        );

        // Creiamo un ciclo: A dipende da B, B dipende da A (caso irreale ma da testare)
        prodottoA.aggiungiIngrediente(prodottoB);
        prodottoB.aggiungiIngrediente(prodottoA);

        prodottoRepository.save(prodottoA);
        prodottoRepository.save(prodottoB);

        // Il metodo deve terminare senza loop infinito
        List<DefaultProdotto> filiera = tracciabilitaService.calcolaFilieraCompleta(prodottoA.getId());

        // Deve contenere entrambi i prodotti una sola volta
        assertEquals(2, filiera.size());
        assertTrue(filiera.contains(prodottoA));
        assertTrue(filiera.contains(prodottoB));
    }

    @Test
    void testProdottoSenzaIngredienti() {
        DefaultProdotto prodottoSemplice = new DefaultProdotto(
                "Prodotto Semplice", "Test", 1.0, UnitaMisura.PEZZO,
                aziendaProduttore, TipoProdotto.TRASFORMATO, CategoriaProdotto.ALTRO
        );
        prodottoRepository.save(prodottoSemplice);

        List<DefaultProdotto> filiera = tracciabilitaService.calcolaFilieraCompleta(prodottoSemplice.getId());

        assertEquals(1, filiera.size());
        assertTrue(filiera.contains(prodottoSemplice));
    }
}