package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultBiglietto;
import com.filiera.facile.model.enums.StatoBiglietto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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

}