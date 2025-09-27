package com.filiera.facile.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcquistaBigliettoRequest {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Positive(message = "L'ID utente deve essere positivo")
    private Long utenteId;

    @NotNull(message = "L'ID evento è obbligatorio")
    @Positive(message = "L'ID evento deve essere positivo")
    private Long eventoId;

    public AcquistaBigliettoRequest() {}

    public AcquistaBigliettoRequest(Long utenteId, Long eventoId) {
        this.utenteId = utenteId;
        this.eventoId = eventoId;
    }

}