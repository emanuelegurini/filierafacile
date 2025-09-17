package application.services;

import com.filiera.facile.application.services.DefaultAziendaService;
import com.filiera.facile.domain.DefaultAzienda;
import com.filiera.facile.domain.DefaultCoordinate;
import com.filiera.facile.repository.DefaultAziendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultAziendaServiceTest {

    private DefaultAziendaService aziendaService;

    @BeforeEach
    void setUp() {
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();
        aziendaService = new DefaultAziendaService(aziendaRepository);
    }

    @Test
    void creaNuovaAzienda_conPartitaIvaDuplicata_lanciaIllegalArgumentException() {
        var azienda1 = new DefaultAzienda(
                "Azienda 1", "P_IVA_UNICA", "Indirizzo 1", "email1@test.it", "111", "sito1.it", new DefaultCoordinate(1f, 1f)
        );
        aziendaService.creaNuovaAzienda(azienda1);

        var azienda2 = new DefaultAzienda(
                "Azienda 2", "P_IVA_UNICA", "Indirizzo 2", "email2@test.it", "222", "sito2.it", new DefaultCoordinate(2f, 2f)
        );

        assertThrows(IllegalArgumentException.class, () -> aziendaService.creaNuovaAzienda(azienda2));
    }
}
