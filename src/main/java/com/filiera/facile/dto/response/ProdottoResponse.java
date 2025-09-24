package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultProdotto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private Long aziendaId;
    private String nomeAzienda;
    private LocalDateTime dataCreazione;

    public ProdottoResponse() {}

    public ProdottoResponse(DefaultProdotto prodotto) {
        this.id = prodotto.getId();
        // TODO: verificare i nomi corretti dei getter nell'entità DefaultProdotto
        this.nome = prodotto.getNomeArticolo();
        this.descrizione = null; // TODO: verificare campo descrizione nell'entità
        this.prezzo = BigDecimal.valueOf(prodotto.getPrezzoUnitario());
        this.aziendaId = prodotto.getAziendaProduttrice() != null ? prodotto.getAziendaProduttrice().getId() : null;
        this.nomeAzienda = prodotto.getAziendaProduttrice() != null ? prodotto.getAziendaProduttrice().getRagioneSociale() : null;
        this.dataCreazione = null; // TODO: aggiungere campo dataCreazione
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

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}