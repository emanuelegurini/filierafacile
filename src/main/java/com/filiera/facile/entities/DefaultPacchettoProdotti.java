package com.filiera.facile.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rappresenta un pacchetto o "bundle" di prodotti, venduto come un unico articolo.
 * Tipicamente creato da un Distributore, raggruppa più {@link ArticoloCatalogo}.
 * Il suo prezzo è calcolato dinamicamente sulla base dei prodotti contenuti.
 */
@Entity
@Table(name = "pacchetto_prodotti")
@DiscriminatorValue("PacchettoProdotti")
public class DefaultPacchettoProdotti extends ArticoloCatalogo {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pacchetto_prodotti_inclusi",
                     joinColumns = @JoinColumn(name = "pacchetto_id"))
    @MapKeyJoinColumn(name = "articolo_id")
    @Column(name = "quantita")
    @JsonIgnore
    private Map<ArticoloCatalogo, Integer> prodottiInclusi;

    protected DefaultPacchettoProdotti() {
        super();
        this.prodottiInclusi = new HashMap<>();
    }

    public DefaultPacchettoProdotti(
            String nome,
            String descrizione,
            DefaultAzienda aziendaDistributrice
    ) {
        super(nome, descrizione, 0, aziendaDistributrice);
        this.prodottiInclusi = new HashMap<>();
    }

    @Override
    public double getPrezzoVendita() {
        double prezzoTotale = prodottiInclusi.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrezzoVendita() * entry.getValue())
                .sum();

        this.setPrezzoUnitario(prezzoTotale);
        return prezzoTotale;
    }

    @Override
    public Map<String, String> getDatiPerValidazione() {
        Map<String, String> dati = new LinkedHashMap<>();
        dati.put("ID Pacchetto", this.id.toString());
        dati.put("Nome Pacchetto", this.nome);
        dati.put("Azienda (Distributore)", this.aziendaDiRiferimento.getRagioneSociale());
        dati.put("Descrizione", this.descrizione);

        String listaProdotti = this.prodottiInclusi.isEmpty()
                ? "Nessun prodotto incluso"
                : this.prodottiInclusi.entrySet().stream()
                .map(entry -> String.format("%dx %s (da %s)",
                        entry.getValue(),
                        entry.getKey().getNomeArticolo(),
                        entry.getKey().getAziendaDiRiferimento().getRagioneSociale()))
                .collect(Collectors.joining("; "));
        dati.put("Prodotti Inclusi", listaProdotti);

        dati.put("Prezzo Calcolato", String.format("%.2f €", getPrezzoVendita()));

        return dati;
    }

    public void aggiungiProdotto(ArticoloCatalogo articolo, int quantita) {
        if (articolo != null && quantita > 0) {
            this.prodottiInclusi.merge(articolo, quantita, Integer::sum);
        }
    }

    public void rimuoviProdotto(ArticoloCatalogo articolo) {
        this.prodottiInclusi.remove(articolo);
    }


    public Map<ArticoloCatalogo, Integer> getProdottiInclusi() {
        return Collections.unmodifiableMap(prodottiInclusi);
    }
}