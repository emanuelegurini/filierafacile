package com.filiera.facile.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AggiungiProdottoPacchettoRequest {

    @NotNull(message = "L'ID utente è obbligatorio")
    @Positive(message = "L'ID utente deve essere positivo")
    private Long utenteId;

    @NotNull(message = "L'ID prodotto è obbligatorio")
    @Positive(message = "L'ID prodotto deve essere positivo")
    private Long prodottoId;

    @NotNull(message = "La quantità è obbligatoria")
    @Positive(message = "La quantità deve essere positiva")
    private Integer quantita;

    public AggiungiProdottoPacchettoRequest() {}

    public AggiungiProdottoPacchettoRequest(Long utenteId, Long prodottoId, Integer quantita) {
        this.utenteId = utenteId;
        this.prodottoId = prodottoId;
        this.quantita = quantita;
    }

}