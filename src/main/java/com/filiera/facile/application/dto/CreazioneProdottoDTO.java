package com.filiera.facile.application.dto;

import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.MetodoColtivazione;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;

import java.util.List;
import java.util.UUID;

public record CreazioneProdottoDTO(
        String nome,
        String descrizione,
        double prezzoUnitario,
        UnitaMisura unitaDiMisura,
        CategoriaProdotto categoria,
        TipoProdotto tipoProdotto,

        MetodoColtivazione metodoColtivazione, // Solo per MATERIA_PRIMA
        String metodoTrasformazione,          // Solo per TRASFORMATO
        List<UUID> ingredientiIds             // Solo per TRASFORMATO
) {}
