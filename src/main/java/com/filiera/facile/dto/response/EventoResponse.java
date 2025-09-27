package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.entities.DefaultAzienda;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class EventoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String luogo;
    private UtenteResponse organizzatore;
    private List<AziendaResponse> aziendePartecipanti;
    private int postiDisponibili;
    private double costoPartecipazione;

    public EventoResponse() {}

    public EventoResponse(DefaultEvento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.descrizione = evento.getDescrizione();
        this.dataOraInizio = evento.getDataOraInizio();
        this.dataOraFine = evento.getDataOraFine();
        this.luogo = evento.getIndirizzo();
        this.organizzatore = new UtenteResponse(evento.getOrganizzatore());
        this.aziendePartecipanti = evento.getAziendePartecipanti().stream()
                .map(AziendaResponse::new)
                .collect(Collectors.toList());
        this.postiDisponibili = evento.getPostiDisponibili();
        this.costoPartecipazione = evento.getCostoPartecipazione();
    }

}