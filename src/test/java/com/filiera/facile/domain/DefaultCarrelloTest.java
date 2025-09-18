package com.filiera.facile.domain;

import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCarrelloTest {

    private DefaultCarrello carrello;
    private DefaultProdotto mela;
    private DefaultProdotto pera;

    @BeforeEach
    void setUp() {
        carrello = new DefaultCarrello(UUID.randomUUID());
        DefaultAzienda azienda = new DefaultAzienda("Test", "123", "Via Test", "test@test.it", "123", "test.it", new DefaultCoordinate(1f, 1f));
        mela = new DefaultProdotto("Mela", "Descrizione", 2.0, UnitaMisura.KG, azienda,  TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA);
        pera = new DefaultProdotto("Pera", "Descrizione", 3.0, UnitaMisura.KG, azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA);
    }

    @Test
    void aggiungiArticolo_e_getTotaleComplessivo_funzionanoCorrettamente() {
        assertTrue(carrello.getRighe().isEmpty());
        assertEquals(0.0, carrello.getTotaleComplessivo());

        carrello.aggiungiArticolo(mela, 2);
        assertEquals(4.0, carrello.getTotaleComplessivo());
        assertEquals(1, carrello.getRighe().size());

        carrello.aggiungiArticolo(pera, 1);
        assertEquals(7.0, carrello.getTotaleComplessivo());
        assertEquals(2, carrello.getRighe().size());

        carrello.aggiungiArticolo(mela, 1);
        assertEquals(9.0, carrello.getTotaleComplessivo());
        assertEquals(2, carrello.getRighe().size());
    }
}