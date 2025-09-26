package repository;

import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.repository.DefaultCarrelloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCarrelloRepositoryTest {

    private DefaultCarrelloRepository carrelloRepository;
    private final UUID utenteId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        carrelloRepository = new DefaultCarrelloRepository();
    }

    @Test
    void save_e_findByUtenteId_salvaERecuperaCorrettamenteIlCarrello() {
        assertTrue(carrelloRepository.findByUtenteId(utenteId).isEmpty());

        DefaultCarrello carrello = new DefaultCarrello(utenteId);
        carrelloRepository.save(carrello);

        DefaultCarrello carrelloTrovato = carrelloRepository.findByUtenteId(utenteId).orElseThrow();
        assertNotNull(carrelloTrovato);
        assertEquals(utenteId, carrelloTrovato.getUtenteId());
    }
}
