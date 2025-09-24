package com.filiera.facile.entities;

import com.filiera.facile.model.enums.TipoAzienda;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static com.filiera.facile.utils.UtilsValidazione.validateEmail;

@Entity
@Table(name = "azienda")
public class DefaultAzienda {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    protected UUID id;

    @Column(name = "ragione_sociale", nullable = false, length = 255)
    protected String ragioneSociale;

    @Column(name = "partita_iva", nullable = false, unique = true, length = 20)
    protected String partitaIva;

    @Column(name = "indirizzo", nullable = false, length = 500)
    protected String indirizzo;

    @Column(name = "email", nullable = false, length = 255)
    protected String email;

    @Column(name = "numero_telefono", nullable = false, length = 20)
    protected String numeroTelefono;

    @Column(name = "sito_web", length = 255)
    protected String sitoWeb;

    @Column(name = "registration_date")
    protected LocalDateTime registrationDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinate_id")
    protected DefaultCoordinate coordinate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "azienda_magazzino",
                     joinColumns = @JoinColumn(name = "azienda_id"))
    @MapKeyJoinColumn(name = "prodotto_id")
    @Column(name = "quantita")
    @JsonIgnore
    private final Map<DefaultProdotto, Integer> magazzino;

    /**
     * Insieme dei ruoli che l'azienda ricopre nella filiera.
     * Determina le funzionalità a cui ha accesso (es. creare prodotti trasformati).
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "azienda_tipi",
                     joinColumns = @JoinColumn(name = "azienda_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private final Set<TipoAzienda> tipiAzienda;

    public DefaultAzienda(
            String ragioneSociale,
            String partitaIva,
            String indirizzo,
            String email,
            String numeroTelefono,
            String sitoWeb,
            DefaultCoordinate coordinate
    ) {
        this.id = UUID.randomUUID();
        this.ragioneSociale = Objects.requireNonNull(ragioneSociale,"Company name cannot be null");
        this.partitaIva = Objects.requireNonNull(partitaIva, "VAT number cannot be null");
        this.indirizzo = Objects.requireNonNull(indirizzo, "Address cannot be null");
        this.email = validateEmail(email);
        this.numeroTelefono = Objects.requireNonNull(numeroTelefono,   "Phone number cannot be null");
        this.sitoWeb = Objects.requireNonNull(sitoWeb,   "Sito web cannot be null");
        this.coordinate = Objects.requireNonNull(coordinate, "Coordinates cannot be null");
        this.tipiAzienda = new HashSet<>();
        this.magazzino = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public void setSitoWeb(String sitoWeb) {
        this.sitoWeb = sitoWeb;
    }

    public String getCoordinate() {
        return "{lat: " + coordinate.getLat() + ", lng: " + coordinate.getLng() + "}";
    }

    public void setCoordinate(DefaultCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void aggiungiTipoAzienda(TipoAzienda tipoAzienda) {
        this.tipiAzienda.add(tipoAzienda);
    }

    public void removeTipoAzienda(TipoAzienda tipoAzienda) {
        this.tipiAzienda.remove(tipoAzienda);
    }

    public Set<TipoAzienda> getTipoAzienda() {
        return this.tipiAzienda;
    }

    public void aggiungiScorte(DefaultProdotto prodotto, int quantita) {
        if (prodotto == null || quantita <= 0) {
            return;
        }
        this.magazzino.merge(prodotto, quantita, Integer::sum);
    }



    public void rimuoviScorte(DefaultProdotto prodotto, int quantita) {
        if (prodotto == null || quantita <= 0) {
            return;
        }

        int disponibilitaAttuale = getDisponibilita(prodotto);

        if (disponibilitaAttuale >= quantita) {

            if (disponibilitaAttuale == quantita) {
                this.magazzino.remove(prodotto);
            } else {
                this.magazzino.merge(prodotto, -quantita, Integer::sum);
            }
        }
    }

    public int getDisponibilita(DefaultProdotto prodotto) {
        return this.magazzino.getOrDefault(prodotto, 0);
    }
}