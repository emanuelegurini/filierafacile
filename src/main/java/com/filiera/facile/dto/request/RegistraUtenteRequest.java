package com.filiera.facile.dto.request;

import jakarta.validation.constraints.*;

public class RegistraUtenteRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve essere tra 2 e 50 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il cognome deve essere tra 2 e 50 caratteri")
    private String cognome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(max = 500, message = "L'indirizzo non può superare i 500 caratteri")
    private String address;

    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Numero di telefono non valido")
    private String phoneNumber;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, max = 100, message = "La password deve essere tra 8 e 100 caratteri")
    private String password;

    public RegistraUtenteRequest() {}

    public RegistraUtenteRequest(String nome, String cognome, String email, String address, String phoneNumber, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}