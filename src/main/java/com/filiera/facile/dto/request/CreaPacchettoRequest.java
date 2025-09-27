package com.filiera.facile.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreaPacchettoRequest {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Positive(message = "L'ID utente deve essere positivo")
    private Long utenteId;

    @NotNull(message = "L'ID azienda è obbligatorio")
    @Positive(message = "L'ID azienda deve essere positivo")
    private Long aziendaId;

    @NotBlank(message = "Il nome del pacchetto è obbligatorio")
    @Size(min = 3, max = 100, message = "Il nome deve essere tra 3 e 100 caratteri")
    private String nome;

    @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
    private String descrizione;

    public CreaPacchettoRequest() {}

    public CreaPacchettoRequest(Long utenteId, Long aziendaId, String nome, String descrizione) {
        this.utenteId = utenteId;
        this.aziendaId = aziendaId;
        this.nome = nome;
        this.descrizione = descrizione;
    }

}