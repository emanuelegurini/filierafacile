package com.filiera.facile.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreaEventoRequest {

    @NotBlank(message = "Il nome dell'evento è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare i 255 caratteri")
    private String nome;

    @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
    private String descrizione;

    @NotNull(message = "La data di inizio è obbligatoria")
    @Future(message = "La data di inizio deve essere futura")
    private LocalDateTime dataInizio;

    @NotNull(message = "La data di fine è obbligatoria")
    @Future(message = "La data di fine deve essere futura")
    private LocalDateTime dataFine;

    @NotBlank(message = "Il luogo è obbligatorio")
    @Size(max = 500, message = "Il luogo non può superare i 500 caratteri")
    private String luogo;

    public CreaEventoRequest() {}

    public CreaEventoRequest(String nome, String descrizione, LocalDateTime dataInizio,
                            LocalDateTime dataFine, String luogo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.luogo = luogo;
    }

}