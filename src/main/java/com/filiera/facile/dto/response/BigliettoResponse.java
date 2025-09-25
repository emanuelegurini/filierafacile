package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultBiglietto;
import com.filiera.facile.model.enums.StatoBiglietto;

import java.time.LocalDateTime;

public class BigliettoResponse {

    private Long id;
    private Long eventoId;
    private String nomeEvento;
    private Long utenteId;
    private String nomeUtente;
    private StatoBiglietto stato;
    private LocalDateTime dataEmissione;

    public BigliettoResponse() {}

    public BigliettoResponse(DefaultBiglietto biglietto) {
        this.id = biglietto.getId();
        this.eventoId = biglietto.getEvento().getId();
        this.nomeEvento = biglietto.getEvento().getNome();
        // TODO: Implementare getUtente() in DefaultBiglietto
        this.utenteId = null; // biglietto.getUtente().getId();
        this.nomeUtente = "Utente"; // biglietto.getUtente().getNome() + " " + biglietto.getUtente().getCognome();
        this.stato = biglietto.getStato();
        this.dataEmissione = biglietto.getDataEmissione();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public StatoBiglietto getStato() {
        return stato;
    }

    public void setStato(StatoBiglietto stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(LocalDateTime dataEmissione) {
        this.dataEmissione = dataEmissione;
    }
}