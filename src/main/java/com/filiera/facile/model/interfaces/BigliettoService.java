package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultBiglietto;

import java.util.List;
import java.util.UUID;

public interface BigliettoService {

    DefaultBiglietto acquistaBiglietto(UUID utenteId, UUID eventoId);

    void annullaBiglietto(UUID bigliettoId);

    List<DefaultBiglietto> trovaBigliettiPerEvento(UUID eventoId);
}
