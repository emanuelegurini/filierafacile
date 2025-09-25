package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloPiattaforma;

import java.util.Set;

public class UtenteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String address;
    private String phoneNumber;
    private Set<RuoloPiattaforma> ruoli;

    public UtenteResponse() {}

    public UtenteResponse(DefaultUtente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.email = utente.getEmail();
        this.address = utente.getAddress();
        this.phoneNumber = utente.getPhoneNumber();
        this.ruoli = utente.getRuoli();
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<RuoloPiattaforma> getRuoli() {
        return ruoli;
    }

    public void setRuoli(Set<RuoloPiattaforma> ruoli) {
        this.ruoli = ruoli;
    }
}