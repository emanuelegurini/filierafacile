package com.filiera.facile.dto.request;

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
    private BigDecimal prezzo;

    @NotNull(message = "L'ID dell'azienda è obbligatorio")
    private Long aziendaId;

    public CreaProdottoRequest() {}

    public CreaProdottoRequest(String nome, String descrizione, BigDecimal prezzo, Long aziendaId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.aziendaId = aziendaId;
    }

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

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Long getAziendaId() {
        return aziendaId;
    }

    public void setAziendaId(Long aziendaId) {
        this.aziendaId = aziendaId;
    }
}