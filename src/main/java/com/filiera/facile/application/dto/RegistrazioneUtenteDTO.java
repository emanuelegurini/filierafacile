package com.filiera.facile.application.dto;

public record RegistrazioneUtenteDTO(
        String nome,
        String cognome,
        String email,
        String indirizzo,
        String telefono,
        String password
) {}
