package com.filiera.facile.dto.request;

import com.filiera.facile.model.enums.TipoAzienda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class CreaAziendaRequest {

    @NotBlank(message = "Il nome dell'azienda è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare i 255 caratteri")
    private String nome;

    @NotBlank(message = "La partita IVA è obbligatoria")
    @Size(min = 11, max = 11, message = "La partita IVA deve essere di 11 caratteri")
    private String partitaIva;

    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri")
    private String descrizione;

    @NotNull(message = "Deve essere specificato almeno un tipo azienda")
    private Set<TipoAzienda> tipiAzienda;

    public CreaAziendaRequest() {}

    public CreaAziendaRequest(String nome, String partitaIva, String descrizione, Set<TipoAzienda> tipiAzienda) {
        this.nome = nome;
        this.partitaIva = partitaIva;
        this.descrizione = descrizione;
        this.tipiAzienda = tipiAzienda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Set<TipoAzienda> getTipiAzienda() {
        return tipiAzienda;
    }

    public void setTipiAzienda(Set<TipoAzienda> tipiAzienda) {
        this.tipiAzienda = tipiAzienda;
    }
}