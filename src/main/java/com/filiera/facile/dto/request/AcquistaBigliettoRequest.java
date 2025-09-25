package com.filiera.facile.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}