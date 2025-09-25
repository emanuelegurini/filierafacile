package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultBiglietto;

import java.util.List;

public interface BigliettoService {

    DefaultBiglietto acquistaBiglietto(Long utenteId, Long eventoId);

    void annullaBiglietto(Long bigliettoId);

    List<DefaultBiglietto> trovaBigliettiPerEvento(Long eventoId);
}
