package com.filiera.facile.application.services;

import com.filiera.facile.domain.DefaultCodaValidazione;
import com.filiera.facile.domain.DefaultCuratoreStatusTracker;
import com.filiera.facile.domain.DefaultPraticaValidazione;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.Validabile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultValidazioneServiceTest {

    private DefaultValidazioneService validazioneService;
    private DefaultCodaValidazione codaValidazione;
    private DefaultCuratoreStatusTracker statusTracker;
    private UUID richiedente;
    private UUID curatore1;
    private UUID curatore2;
    private Validabile contenutoMock;

    @BeforeEach
    void setUp() {
        codaValidazione = new DefaultCodaValidazione();
        statusTracker = new DefaultCuratoreStatusTracker();
        validazioneService = new DefaultValidazioneService(codaValidazione, statusTracker);

        richiedente = UUID.randomUUID();
        curatore1 = UUID.randomUUID();
        curatore2 = UUID.randomUUID();
        contenutoMock = new ValidabileMock();

        validazioneService.registraCuratore(curatore1);
        validazioneService.registraCuratore(curatore2);
    }

    @Test
    void testSottomettiPerValidazioneConCuratoreLibero() {
        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);

        assertNotNull(praticaId);
        assertTrue(validazioneService.getPraticaById(praticaId).isPresent());

        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();
        assertTrue(pratica.isAssegnata());
        assertEquals(StatoValidazione.IN_REVISIONE, pratica.getStatoCorrente());
        assertEquals(0, validazioneService.getPratichePendenti().size());
    }

    @Test
    void testSottomettiPerValidazioneSenzaCuratoriLiberi() {
        statusTracker.setCuratoreOccupato(curatore1);
        statusTracker.setCuratoreOccupato(curatore2);

        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);

        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();
        assertFalse(pratica.isAssegnata());
        assertEquals(StatoValidazione.IN_ATTESA_DI_APPROVAZIONE, pratica.getStatoCorrente());
        assertEquals(1, validazioneService.getPratichePendenti().size());
    }

    @Test
    void testApprovaPratica() {
        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);
        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();
        UUID curatoreAssegnato = pratica.getCuratoreAssegnato();

        validazioneService.approvaPratica(praticaId, curatoreAssegnato, "Approvato");

        pratica = validazioneService.getPraticaById(praticaId).orElse(null);
        assertNull(pratica); // Rimossa dalle pratiche in corso dopo completamento
        assertEquals(StatoValidazione.APPROVATO, contenutoMock.getStatoValidazione());
        assertTrue(statusTracker.isCuratoreLibero(curatoreAssegnato));
    }

    @Test
    void testRespingiPratica() {
        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);
        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();
        UUID curatoreAssegnato = pratica.getCuratoreAssegnato();

        validazioneService.respingiPratica(praticaId, curatoreAssegnato, "Non conforme");

        assertEquals(StatoValidazione.RESPINTO, contenutoMock.getStatoValidazione());
        assertTrue(statusTracker.isCuratoreLibero(curatoreAssegnato));
    }

    @Test
    void testRichiedeModifiche() {
        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);
        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();
        UUID curatoreAssegnato = pratica.getCuratoreAssegnato();

        validazioneService.richiedeModifiche(praticaId, curatoreAssegnato, "Servono più dettagli");

        assertEquals(StatoValidazione.MODIFICHE_RICHIESTE, contenutoMock.getStatoValidazione());
        assertTrue(statusTracker.isCuratoreLibero(curatoreAssegnato));
    }

    @Test
    void testLiberaCuratoreAssegnaAutomaticamente() {
        statusTracker.setCuratoreOccupato(curatore1);
        statusTracker.setCuratoreOccupato(curatore2);

        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);
        DefaultPraticaValidazione pratica = validazioneService.getPraticaById(praticaId).get();

        assertFalse(pratica.isAssegnata());
        assertEquals(1, validazioneService.getPratichePendenti().size());

        validazioneService.liberaCuratore(curatore1);

        assertEquals(0, validazioneService.getPratichePendenti().size());
        assertTrue(pratica.isAssegnata());
        assertEquals(curatore1, pratica.getCuratoreAssegnato());
    }

    @Test
    void testGetPratichePerCuratore() {
        UUID pratica1 = validazioneService.sottomettiPerValidazione(new ValidabileMock(), richiedente);
        UUID pratica2 = validazioneService.sottomettiPerValidazione(new ValidabileMock(), richiedente);

        DefaultPraticaValidazione p1 = validazioneService.getPraticaById(pratica1).get();
        DefaultPraticaValidazione p2 = validazioneService.getPraticaById(pratica2).get();

        UUID curatoreP1 = p1.getCuratoreAssegnato();
        UUID curatoreP2 = p2.getCuratoreAssegnato();

        List<DefaultPraticaValidazione> praticheC1 = validazioneService.getPratichePerCuratore(curatoreP1);
        List<DefaultPraticaValidazione> praticheC2 = validazioneService.getPratichePerCuratore(curatoreP2);

        assertEquals(1, praticheC1.size());
        assertEquals(1, praticheC2.size());
        assertEquals(pratica1, praticheC1.get(0).getId());
        assertEquals(pratica2, praticheC2.get(0).getId());
    }

    @Test
    void testValidazioneConCuratoreNonAssegnato() {
        UUID praticaId = validazioneService.sottomettiPerValidazione(contenutoMock, richiedente);
        UUID curatoreNonAssegnato = UUID.randomUUID();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validazioneService.approvaPratica(praticaId, curatoreNonAssegnato, "test"));

        assertTrue(exception.getMessage().contains("non è assegnato"));
    }

    @Test
    void testValidazionePraticaInesistente() {
        UUID praticaInesistente = UUID.randomUUID();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validazioneService.approvaPratica(praticaInesistente, curatore1, "test"));

        assertTrue(exception.getMessage().contains("non trovata"));
    }

    @Test
    void testParametriNull() {
        assertThrows(NullPointerException.class, () ->
                validazioneService.sottomettiPerValidazione(null, richiedente));

        assertThrows(NullPointerException.class, () ->
                validazioneService.sottomettiPerValidazione(contenutoMock, null));

        assertThrows(NullPointerException.class, () ->
                validazioneService.registraCuratore(null));
    }

    private static class ValidabileMock implements Validabile {
        private StatoValidazione stato = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;

        @Override
        public Map<String, String> getDatiPerValidazione() {
            Map<String, String> dati = new HashMap<>();
            dati.put("tipo", "Test");
            return dati;
        }

        @Override
        public void setStatoValidazione(StatoValidazione stato) {
            this.stato = stato;
        }

        @Override
        public StatoValidazione getStatoValidazione() {
            return stato;
        }

        @Override
        public void sottomettiPerValidazione() {
            // Mock implementation
        }
    }
}