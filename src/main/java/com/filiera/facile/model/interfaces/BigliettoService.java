package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultBiglietto;

import java.util.List;
import java.util.UUID;

public interface BigliettoService {

    DefaultBiglietto acquistaBiglietto(Long utenteId, Long eventoId);

    void annullaBiglietto(Long bigliettoId);

    List<DefaultBiglietto> trovaBigliettiPerEvento(Long eventoId);
}
