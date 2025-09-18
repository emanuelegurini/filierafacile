package com.filiera.facile.domain;

import com.filiera.facile.model.enums.StatoCuratore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCuratoreStatusTrackerTest {

    private DefaultCuratoreStatusTracker tracker;
    private UUID curatore1;
    private UUID curatore2;
    private UUID curatore3;

    @BeforeEach
    void setUp() {
        tracker = new DefaultCuratoreStatusTracker();
        curatore1 = UUID.randomUUID();
        curatore2 = UUID.randomUUID();
        curatore3 = UUID.randomUUID();
    }

    @Test
    void testRegistraCuratore() {
        tracker.registraCuratore(curatore1);

        assertEquals(StatoCuratore.LIBERO, tracker.getStatoCuratore(curatore1));
        assertEquals(1, tracker.getNumeroTotaleCuratori());
        assertTrue(tracker.getCuratoriLiberi().contains(curatore1));
    }

    @Test
    void testSetCuratoreOccupato() {
        tracker.registraCuratore(curatore1);

        tracker.setCuratoreOccupato(curatore1);

        assertEquals(StatoCuratore.OCCUPATO, tracker.getStatoCuratore(curatore1));
        assertFalse(tracker.isCuratoreLibero(curatore1));
        assertTrue(tracker.getCuratoriOccupati().contains(curatore1));
    }

    @Test
    void testSetCuratoreLibero() {
        tracker.registraCuratore(curatore1);
        tracker.setCuratoreOccupato(curatore1);

        tracker.setCuratoreLibero(curatore1);

        assertEquals(StatoCuratore.LIBERO, tracker.getStatoCuratore(curatore1));
        assertTrue(tracker.isCuratoreLibero(curatore1));
        assertTrue(tracker.getCuratoriLiberi().contains(curatore1));
    }

    @Test
    void testSetCuratoreNonDisponibile() {
        tracker.registraCuratore(curatore1);

        tracker.setCuratoreNonDisponibile(curatore1);

        assertEquals(StatoCuratore.NON_DISPONIBILE, tracker.getStatoCuratore(curatore1));
        assertFalse(tracker.isCuratoreLibero(curatore1));
        assertFalse(tracker.getCuratoriLiberi().contains(curatore1));
    }

    @Test
    void testGetPrimoCuratoreLibero() {
        tracker.registraCuratore(curatore1);
        tracker.registraCuratore(curatore2);
        tracker.setCuratoreOccupato(curatore1);

        Optional<UUID> primoCuratoreLibero = tracker.getPrimoCuratoreLibero();

        assertTrue(primoCuratoreLibero.isPresent());
        assertEquals(curatore2, primoCuratoreLibero.get());
    }

    @Test
    void testGetPrimoCuratoreLiberoQuandoNessunLibero() {
        tracker.registraCuratore(curatore1);
        tracker.setCuratoreOccupato(curatore1);

        Optional<UUID> primoCuratoreLibero = tracker.getPrimoCuratoreLibero();

        assertFalse(primoCuratoreLibero.isPresent());
    }

    @Test
    void testHasCuratoriLiberi() {
        assertFalse(tracker.hasCuratoriLiberi());

        tracker.registraCuratore(curatore1);
        assertTrue(tracker.hasCuratoriLiberi());

        tracker.setCuratoreOccupato(curatore1);
        assertFalse(tracker.hasCuratoriLiberi());
    }

    @Test
    void testRimuoviCuratore() {
        tracker.registraCuratore(curatore1);
        tracker.registraCuratore(curatore2);

        tracker.rimuoviCuratore(curatore1);

        assertNull(tracker.getStatoCuratore(curatore1));
        assertEquals(1, tracker.getNumeroTotaleCuratori());
        assertFalse(tracker.getCuratoriLiberi().contains(curatore1));
    }

    @Test
    void testMultipliCuratori() {
        tracker.registraCuratore(curatore1);
        tracker.registraCuratore(curatore2);
        tracker.registraCuratore(curatore3);

        tracker.setCuratoreOccupato(curatore1);
        tracker.setCuratoreNonDisponibile(curatore2);

        List<UUID> curatoriLiberi = tracker.getCuratoriLiberi();
        List<UUID> curatoriOccupati = tracker.getCuratoriOccupati();

        assertEquals(1, curatoriLiberi.size());
        assertEquals(1, curatoriOccupati.size());
        assertTrue(curatoriLiberi.contains(curatore3));
        assertTrue(curatoriOccupati.contains(curatore1));
        assertEquals(3, tracker.getNumeroTotaleCuratori());
    }

    @Test
    void testOperazioniSuCuratoreNonRegistrato() {
        UUID curatoreNonRegistrato = UUID.randomUUID();

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () ->
                tracker.setCuratoreOccupato(curatoreNonRegistrato));

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () ->
                tracker.setCuratoreLibero(curatoreNonRegistrato));

        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () ->
                tracker.setCuratoreNonDisponibile(curatoreNonRegistrato));

        assertTrue(exception1.getMessage().contains("non registrato"));
        assertTrue(exception2.getMessage().contains("non registrato"));
        assertTrue(exception3.getMessage().contains("non registrato"));
    }

    @Test
    void testParametriNull() {
        assertThrows(NullPointerException.class, () ->
                tracker.registraCuratore(null));

        assertThrows(NullPointerException.class, () ->
                tracker.setCuratoreOccupato(null));

        assertThrows(NullPointerException.class, () ->
                tracker.setCuratoreLibero(null));
    }
}