package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;

import java.time.LocalDateTime;
import java.util.Set;

public class AziendaResponse {

    private Long id;
    private String nome;
    private String partitaIva;
    private String descrizione;
    private Set<TipoAzienda> tipiAzienda;
    private LocalDateTime dataCreazione;

    public AziendaResponse() {}

    public AziendaResponse(DefaultAzienda azienda) {
        this.id = azienda.getId();
        this.nome = azienda.getRagioneSociale();
        this.partitaIva = azienda.getPartitaIva();
        // Mappare i campi disponibili dall'entità
        this.descrizione = "Azienda specializzata nella filiera alimentare"; // Campo non presente nell'entità
        this.tipiAzienda = azienda.getTipoAzienda(); // Metodo corretto (singolare)
        this.dataCreazione = null; // Getter mancante nell'entità, TODO: aggiungere
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}