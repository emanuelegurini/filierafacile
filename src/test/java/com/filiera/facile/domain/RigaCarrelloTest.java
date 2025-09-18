package com.filiera.facile.domain;

import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RigaCarrelloTest {

    private DefaultProdotto prodotto;

    @BeforeEach
    void setUp() {
        DefaultAzienda azienda = new DefaultAzienda("Test", "123", "Via Test", "test@test.it", "123", "test.it", new DefaultCoordinate(1f, 1f));
        prodotto = new DefaultProdotto("Mela", "Descrizione", 2.50, UnitaMisura.KG, azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA);
    }

    @Test
    void getPrezzoTotaleRiga_calcolaCorrettamenteIlSubtotale() {
        DefaultRigaCarrello riga = new DefaultRigaCarrello(prodotto, 3); // 3 kg di mele a 2.50€/kg
        assertEquals(7.50, riga.getPrezzoTotaleRiga());
    }

    @Test
    void costruttore_conQuantitaNegativa_lanciaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DefaultRigaCarrello(prodotto, -1);
        });
    }
}