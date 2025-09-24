package com.filiera.facile.dto.request;

import com.filiera.facile.model.enums.TipoAzienda;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class CreaAziendaRequest {

    @NotBlank(message = "La ragione sociale è obbligatoria")
    @Size(max = 255, message = "La ragione sociale non può superare i 255 caratteri")
    private String ragioneSociale;

    @NotBlank(message = "La partita IVA è obbligatoria")
    @Size(min = 11, max = 11, message = "La partita IVA deve essere di 11 caratteri")
    private String partitaIva;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(max = 500, message = "L'indirizzo non può superare i 500 caratteri")
    private String indirizzo;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Size(max = 255, message = "L'email non può superare i 255 caratteri")
    private String email;

    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @Size(max = 20, message = "Il numero di telefono non può superare i 20 caratteri")
    private String numeroTelefono;

    @Size(max = 255, message = "Il sito web non può superare i 255 caratteri")
    private String sitoWeb;

    private Float latitudine;
    private Float longitudine;

    @NotNull(message = "Deve essere specificato almeno un tipo azienda")
    private Set<TipoAzienda> tipiAzienda;

    public CreaAziendaRequest() {}

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public void setSitoWeb(String sitoWeb) {
        this.sitoWeb = sitoWeb;
    }

    public Float getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Float latitudine) {
        this.latitudine = latitudine;
    }

    public Float getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Float longitudine) {
        this.longitudine = longitudine;
    }

    public Set<TipoAzienda> getTipiAzienda() {
        return tipiAzienda;
    }

    public void setTipiAzienda(Set<TipoAzienda> tipiAzienda) {
        this.tipiAzienda = tipiAzienda;
    }
}