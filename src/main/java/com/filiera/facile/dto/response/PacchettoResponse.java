package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultPacchettoProdotti;

import java.math.BigDecimal;
import java.util.Map;

public class PacchettoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private Long aziendaId;
    private String nomeAzienda;
    private BigDecimal prezzoTotale;
    private Map<String, Integer> prodottiInclusi;

    public PacchettoResponse() {}

    public PacchettoResponse(DefaultPacchettoProdotti pacchetto) {
        this.id = pacchetto.getId();
        this.nome = pacchetto.getNomeArticolo();
        this.descrizione = pacchetto.getDescrizioneArticolo();
        this.aziendaId = pacchetto.getAziendaDiRiferimento().getId();
        this.nomeAzienda = pacchetto.getAziendaDiRiferimento().getRagioneSociale();
        this.prezzoTotale = java.math.BigDecimal.valueOf(pacchetto.getPrezzoVendita());
        this.prodottiInclusi = pacchetto.getProdottiInclusi().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                    entry -> entry.getKey().getNomeArticolo(),
                    Map.Entry::getValue
                ));
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getAziendaId() {
        return aziendaId;
    }

    public void setAziendaId(Long aziendaId) {
        this.aziendaId = aziendaId;
    }

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(BigDecimal prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public Map<String, Integer> getProdottiInclusi() {
        return prodottiInclusi;
    }

    public void setProdottiInclusi(Map<String, Integer> prodottiInclusi) {
        this.prodottiInclusi = prodottiInclusi;
    }
}