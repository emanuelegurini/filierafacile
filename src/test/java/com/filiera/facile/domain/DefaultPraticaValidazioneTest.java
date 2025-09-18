package com.filiera.facile.domain;

import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.interfaces.Validabile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPraticaValidazioneTest {

    private DefaultPraticaValidazione pratica;
    private UUID richiedenteId;
    private Validabile contenutoMock;

    @BeforeEach
    void setUp() {
        richiedenteId = UUID.randomUUID();
        contenutoMock = new ValidabileMock();
        pratica = new DefaultPraticaValidazione(contenutoMock, richiedenteId);
    }

    @Test
    void testCreazionePratica() {
        assertNotNull(pratica.getId());
        assertEquals(richiedenteId, pratica.getRichiedenteId());
        assertEquals(contenutoMock, pratica.getContenutoSottomesso());
        assertEquals(StatoValidazione.IN_ATTESA_DI_APPROVAZIONE, pratica.getStatoCorrente());
        assertNotNull(pratica.getDataCreazione());
        assertNull(pratica.getCuratoreAssegnato());
        assertFalse(pratica.isAssegnata());
        assertFalse(pratica.isCompletata());
    }

    @Test
    void testAssegnaCuratore() {
        UUID curatoreId = UUID.randomUUID();

        pratica.assegnaCuratore(curatoreId);

        assertEquals(curatoreId, pratica.getCuratoreAssegnato());
        assertEquals(StatoValidazione.IN_REVISIONE, pratica.getStatoCorrente());
        assertEquals(StatoValidazione.IN_REVISIONE, contenutoMock.getStatoValidazione());
        assertNotNull(pratica.getDataAssegnazione());
        assertTrue(pratica.isAssegnata());
        assertFalse(pratica.isCompletata());
    }

    @Test
    void testApprovaPratica() {
        UUID curatoreId = UUID.randomUUID();
        String note = "Prodotto conforme alle specifiche";

        pratica.assegnaCuratore(curatoreId);
        pratica.approva(note);

        assertEquals(StatoValidazione.APPROVATO, pratica.getStatoCorrente());
        assertEquals(StatoValidazione.APPROVATO, contenutoMock.getStatoValidazione());
        assertEquals(note, pratica.getNoteValutazione());
        assertNotNull(pratica.getDataCompletamento());
        assertTrue(pratica.isCompletata());
    }

    @Test
    void testRespingiPratica() {
        UUID curatoreId = UUID.randomUUID();
        String motivazione = "Documentazione insufficiente";

        pratica.assegnaCuratore(curatoreId);
        pratica.respingi(motivazione);

        assertEquals(StatoValidazione.RESPINTO, pratica.getStatoCorrente());
        assertEquals(StatoValidazione.RESPINTO, contenutoMock.getStatoValidazione());
        assertEquals(motivazione, pratica.getMotivazioneRifiuto());
        assertNotNull(pratica.getDataCompletamento());
        assertTrue(pratica.isCompletata());
    }

    @Test
    void testRichiedeModifiche() {
        UUID curatoreId = UUID.randomUUID();
        String noteModifiche = "Aggiungere certificazione biologica";

        pratica.assegnaCuratore(curatoreId);
        pratica.richiedeModifiche(noteModifiche);

        assertEquals(StatoValidazione.MODIFICHE_RICHIESTE, pratica.getStatoCorrente());
        assertEquals(StatoValidazione.MODIFICHE_RICHIESTE, contenutoMock.getStatoValidazione());
        assertEquals(noteModifiche, pratica.getNoteValutazione());
        assertNotNull(pratica.getDataCompletamento());
        assertTrue(pratica.isCompletata());
    }

    @Test
    void testApprovazioneSenonAssegnata() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                pratica.approva("test"));

        assertTrue(exception.getMessage().contains("deve essere in revisione"));
    }

    @Test
    void testAssegnaCuratoreNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                pratica.assegnaCuratore(null));

        assertTrue(exception.getMessage().contains("non può essere null"));
    }

    @Test
    void testRespingiSenzaMotivazione() {
        UUID curatoreId = UUID.randomUUID();
        pratica.assegnaCuratore(curatoreId);

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                pratica.respingi(null));

        assertTrue(exception.getMessage().contains("obbligatoria"));
    }

    @Test
    void testCreazioneConParametriNull() {
        assertThrows(NullPointerException.class, () ->
                new DefaultPraticaValidazione(null, richiedenteId));

        assertThrows(NullPointerException.class, () ->
                new DefaultPraticaValidazione(contenutoMock, null));
    }

    private static class ValidabileMock implements Validabile {
        private StatoValidazione stato = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;

        @Override
        public Map<String, String> getDatiPerValidazione() {
            Map<String, String> dati = new HashMap<>();
            dati.put("tipo", "Prodotto Test");
            dati.put("nome", "Prodotto Mock");
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