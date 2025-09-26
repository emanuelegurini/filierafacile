package application.services;

import com.filiera.facile.application.services.DefaultProdottoService;
import com.filiera.facile.entities.*;
import com.filiera.facile.model.enums.*;
import com.filiera.facile.repository.DefaultAziendaRepository;
import com.filiera.facile.repository.DefaultProdottoRepository;
import com.filiera.facile.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultProdottoServiceTest {

    private DefaultProdottoService prodottoService;
    private DefaultProdottoRepository prodottoRepository;

    private DefaultUtente utenteConPermessi;
    private DefaultUtente utenteSenzaPermessi;
    private DefaultAzienda azienda;

    @BeforeEach
    void setUp() {
        prodottoRepository = new DefaultProdottoRepository();
        DefaultUtenteRepository utenteRepository = new DefaultUtenteRepository();
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();

        prodottoService = new DefaultProdottoService(prodottoRepository, utenteRepository, aziendaRepository);

        azienda = new DefaultAzienda("Azienda Test", "111", "Via Test", "az@test.it", "123", "test.it", new DefaultCoordinate(1f, 1f));
        aziendaRepository.save(azienda);

        utenteConPermessi = new DefaultUtente("Mario", "Rossi", "mario@test.it", "Via Roma", "333", "pass");
        DefaultAffiliazione affiliazioneCorretta = new DefaultAffiliazione(utenteConPermessi, azienda, RuoloAziendale.GESTORE_PRODOTTI);
        utenteConPermessi.addAffiliazione(affiliazioneCorretta);
        utenteRepository.save(utenteConPermessi);

        utenteSenzaPermessi = new DefaultUtente("Luigi", "Verdi", "luigi@test.it", "Via Milano", "444", "pass");
        utenteRepository.save(utenteSenzaPermessi);
    }

    @Test
    void creaNuovoProdotto_conUtenteAutorizzato_creaCorrettamenteIlProdotto() {
        var prodotto = new DefaultProdotto(
                "Mela Golden", "Descrizione mela", 1.5,
                UnitaMisura.KG, azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA
        );
        prodotto.setMetodoColtivazione(MetodoColtivazione.BIOLOGICO);

        var nuovoProdotto = prodottoService
                .creaNuovoProdotto(
                        utenteConPermessi.getId(),
                        azienda.getId(),
                        prodotto
                );

        assertNotNull(nuovoProdotto);

        var prodottoSalvatoOpt = prodottoRepository.findById(nuovoProdotto.getId());
        assertTrue(prodottoSalvatoOpt.isPresent());

        var prodottoSalvato = prodottoSalvatoOpt.get();
        assertEquals("Mela Golden", prodottoSalvato.getNomeArticolo());
        assertEquals(1.5, prodottoSalvato.getPrezzoUnitario());
        assertEquals(TipoProdotto.MATERIA_PRIMA, prodottoSalvato.getTipoProdotto());
        assertEquals(MetodoColtivazione.BIOLOGICO, prodottoSalvato.getMetodoColtivazione());
        assertTrue(prodottoSalvato.getIngredienti().isEmpty());
    }

    @Test
    void creaNuovoProdotto_conUtenteNonAutorizzato_lanciaSecurityException() {
        var prodotto = new DefaultProdotto("Pera", "Descrizione pera", 1.2, UnitaMisura.KG, azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA);

        assertThrows(SecurityException.class, () -> prodottoService.creaNuovoProdotto(utenteSenzaPermessi.getId(), azienda.getId(), prodotto));
    }


    @Test
    void creaNuovoProdotto_trasformatoConIngredienti_creaCorrettamente() {
        var pomodoro = new DefaultProdotto(
                "Pomodoro San Marzano", "Ingrediente base per sughi", 2.0, UnitaMisura.KG,
                azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA
        );
        prodottoRepository.save(pomodoro);

        var passata = new DefaultProdotto(
                "Passata di Pomodoro", "Fatta con pomodori locali", 3.5,
                UnitaMisura.LT, azienda, TipoProdotto.TRASFORMATO, CategoriaProdotto.CONSERVE_E_MARMELLATE
        );
        passata.setMetodoTrasformazione("Cottura lenta e invasettamento a caldo");
        passata.aggiungiIngrediente(pomodoro);

        var passataCreata = prodottoService.creaNuovoProdotto(
                utenteConPermessi.getId(),
                azienda.getId(),
                passata
        );

        assertNotNull(passataCreata);
        assertEquals("Passata di Pomodoro", passataCreata.getNomeArticolo());
        assertEquals(TipoProdotto.TRASFORMATO, passataCreata.getTipoProdotto());
        assertEquals("Cottura lenta e invasettamento a caldo", passataCreata.getMetodoTrasformazione());

        assertFalse(passataCreata.getIngredienti().isEmpty());
        assertEquals(1, passataCreata.getIngredienti().size());
        assertEquals(pomodoro.getId(), passataCreata.getIngredienti().getFirst().getId());
        assertEquals("Pomodoro San Marzano", passataCreata.getIngredienti().getFirst().getNomeArticolo());
    }
}
