package com.filiera.facile.application.dto;

import com.filiera.facile.model.enums.TipoAzienda;

import java.util.Set;


public record CreazioneAziendaDTO(
        String ragioneSociale,
        String partitaIva,
        String indirizzo,
        String email,
        String numeroTelefono,
        String sitoWeb,
        CoordinateDTO coordinate,
        Set<TipoAzienda> tipiAzienda
) {}
