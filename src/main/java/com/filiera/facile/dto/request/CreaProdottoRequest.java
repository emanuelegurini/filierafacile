package com.filiera.facile.dto.request;

import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreaProdottoRequest {

    @NotBlank(message = "Il nome del prodotto è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare i 255 caratteri")
    private String nome;

    @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
    private String descrizione;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere positivo")
    private Double prezzo;

    @NotNull(message = "L'ID dell'azienda è obbligatorio")
    private Long aziendaId;

    @NotNull(message = "L'unità di misura è obbligatoria")
    private UnitaMisura unitaDiMisura;

    @NotNull(message = "Il tipo di prodotto è obbligatorio")
    private TipoProdotto tipoProdotto;

    @NotNull(message = "La categoria del prodotto è obbligatoria")
    private CategoriaProdotto categoriaProdotto;

    public CreaProdottoRequest() {}

}