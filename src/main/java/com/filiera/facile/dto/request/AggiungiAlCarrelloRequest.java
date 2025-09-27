package com.filiera.facile.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AggiungiAlCarrelloRequest {

    @NotNull(message = "L'ID dell'articolo è obbligatorio")
    private Long articoloId;

    @NotNull(message = "La quantità è obbligatoria")
    @Positive(message = "La quantità deve essere positiva")
    private Integer quantita;

    public AggiungiAlCarrelloRequest() {}

    public AggiungiAlCarrelloRequest(Long articoloId, Integer quantita) {
        this.articoloId = articoloId;
        this.quantita = quantita;
    }

}