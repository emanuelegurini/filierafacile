package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultEvento;
import com.filiera.facile.entities.DefaultAzienda;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // Getters and Setters
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

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public UtenteResponse getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(UtenteResponse organizzatore) {
        this.organizzatore = organizzatore;
    }

    public List<AziendaResponse> getAziendePartecipanti() {
        return aziendePartecipanti;
    }

    public void setAziendePartecipanti(List<AziendaResponse> aziendePartecipanti) {
        this.aziendePartecipanti = aziendePartecipanti;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public double getCostoPartecipazione() {
        return costoPartecipazione;
    }

    public void setCostoPartecipazione(double costoPartecipazione) {
        this.costoPartecipazione = costoPartecipazione;
    }
}