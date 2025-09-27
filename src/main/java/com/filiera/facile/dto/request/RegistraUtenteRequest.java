package com.filiera.facile.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}