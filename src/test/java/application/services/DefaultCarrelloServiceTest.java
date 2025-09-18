package application.services;

import com.filiera.facile.application.services.DefaultCarrelloService;
import com.filiera.facile.domain.DefaultAzienda;
import com.filiera.facile.domain.DefaultCarrello;
import com.filiera.facile.domain.DefaultCoordinate;
import com.filiera.facile.domain.DefaultProdotto;
import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;
import com.filiera.facile.repository.DefaultCarrelloRepository;
import com.filiera.facile.repository.DefaultProdottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCarrelloServiceTest {

    private DefaultCarrelloService carrelloService;
    private DefaultCarrelloRepository carrelloRepository;
    private DefaultProdottoRepository prodottoRepository;

    private UUID utenteId = UUID.randomUUID();
    private DefaultProdotto prodotto;

    @BeforeEach
    void setUp() {
        carrelloRepository = new DefaultCarrelloRepository();
        prodottoRepository = new DefaultProdottoRepository();
        carrelloService = new DefaultCarrelloService(carrelloRepository, prodottoRepository);

        DefaultAzienda azienda = new DefaultAzienda("Test", "123", "Addr", "test@test.it", "456", "test.it", new DefaultCoordinate(1f, 1f));
        prodotto = new DefaultProdotto("Uva", "Desc",  4.0, UnitaMisura.KG, azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA);
        prodottoRepository.save(prodotto);
    }

    @Test
    void getCarrelloPerUtente_seNonEsiste_neCreaUnoNuovo() {
        DefaultCarrello carrello = carrelloService.getCarrelloPerUtente(utenteId);

        assertNotNull(carrello);
        assertTrue(carrello.getRighe().isEmpty());
        assertTrue(carrelloRepository.findByUtenteId(utenteId).isPresent());
    }

    @Test
    void aggiungiArticoloAlCarrello_aggiornaCorrettamenteIlCarrello() {
        DefaultCarrello carrelloIniziale = carrelloService.getCarrelloPerUtente(utenteId);
        assertEquals(0.0, carrelloIniziale.getTotaleComplessivo());

        carrelloService.aggiungiArticoloAlCarrello(utenteId, prodotto.getId(), 2);

        DefaultCarrello carrelloAggiornato = carrelloRepository.findByUtenteId(utenteId).orElseThrow();
        assertEquals(8.0, carrelloAggiornato.getTotaleComplessivo());
        assertEquals(1, carrelloAggiornato.getRighe().size());
    }
}
