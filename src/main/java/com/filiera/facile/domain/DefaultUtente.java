package com.filiera.facile.domain;

import com.filiera.facile.model.enums.RuoloPiattaforma;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.filiera.facile.utils.ValidationUtils.validateEmail;

public class DefaultUtente {
    private final UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String address;
    private String phoneNumber;
    private String passwordHash;

    private final Set<RuoloPiattaforma> ruoli;

    public DefaultUtente(
            String nome,
            String cognome,
            String email,
            String address,
            String phoneNumber,
            String password
    ) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome non può essere null");
        this.cognome = Objects.requireNonNull(cognome, "Cognome non può essere null");
        this.address = Objects.requireNonNull(address, "L'indirizzo non può essere null");
        this.email = validateEmail(email);
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Il numero di telefono non può essere null");
        setPassword(password);

        this.ruoli = new HashSet<>();
        this.ruoli.add(RuoloPiattaforma.ACQUIRENTE);

    }

    public UUID getId() {
        return id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        Objects.requireNonNull(password, "Password non può essere null");
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setPasswordHash(String password) {
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<RuoloPiattaforma> getRuoli() {
        return Collections.unmodifiableSet(this.ruoli);
    }

    public boolean addRuolo(RuoloPiattaforma ruolo) {
        return this.ruoli.add(ruolo);
    }
}
