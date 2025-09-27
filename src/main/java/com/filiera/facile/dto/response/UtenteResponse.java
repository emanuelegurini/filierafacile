package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloPiattaforma;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
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

}