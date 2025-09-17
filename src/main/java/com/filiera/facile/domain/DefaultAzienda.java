package com.filiera.facile.domain;

import com.filiera.facile.model.enums.TipoAzienda;

import java.time.LocalDateTime;
import java.util.*;

import static com.filiera.facile.utils.UtilsValidazione.validateEmail;

public class DefaultAzienda {
    protected UUID id;
    protected String ragioneSociale;
    protected String partitaIva;
    protected String indirizzo;
    protected String email;
    protected String numeroTelefono;
    protected String sitoWeb;
    protected LocalDateTime registrationDate;
    protected DefaultCoordinate coordinate;
    private final Map<DefaultProdotto, Integer> magazzino;


    /**
     * Insieme dei ruoli che l'azienda ricopre nella filiera.
     * Determina le funzionalità a cui ha accesso (es. creare prodotti trasformati).
     */
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
