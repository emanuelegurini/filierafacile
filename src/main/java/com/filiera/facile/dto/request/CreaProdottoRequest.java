package com.filiera.facile.dto.request;

import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.TipoProdotto;
import com.filiera.facile.model.enums.UnitaMisura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Long getAziendaId() {
        return aziendaId;
    }

    public void setAziendaId(Long aziendaId) {
        this.aziendaId = aziendaId;
    }

    public UnitaMisura getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public void setUnitaDiMisura(UnitaMisura unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public TipoProdotto getTipoProdotto() {
        return tipoProdotto;
    }

    public void setTipoProdotto(TipoProdotto tipoProdotto) {
        this.tipoProdotto = tipoProdotto;
    }

    public CategoriaProdotto getCategoriaProdotto() {
        return categoriaProdotto;
    }

    public void setCategoriaProdotto(CategoriaProdotto categoriaProdotto) {
        this.categoriaProdotto = categoriaProdotto;
    }
}