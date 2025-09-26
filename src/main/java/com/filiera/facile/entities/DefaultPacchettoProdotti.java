package com.filiera.facile.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
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

    @OneToMany(mappedBy = "pacchetto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<ProdottoPacchetto> prodottiInclusi;

    protected DefaultPacchettoProdotti() {
        super();
        this.prodottiInclusi = new ArrayList<>();
    }

    public DefaultPacchettoProdotti(
            String nome,
            String descrizione,
            DefaultAzienda aziendaDistributrice
    ) {
        super(nome, descrizione, 0, aziendaDistributrice);
        this.prodottiInclusi = new ArrayList<>();
    }

    @Override
    public double getPrezzoVendita() {
        double prezzoTotale = prodottiInclusi.stream()
                .mapToDouble(pp -> pp.getArticolo().getPrezzoVendita() * pp.getQuantita())
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
                : this.prodottiInclusi.stream()
                .map(pp -> String.format("%dx %s (da %s)",
                        pp.getQuantita(),
                        pp.getArticolo().getNomeArticolo(),
                        pp.getArticolo().getAziendaDiRiferimento().getRagioneSociale()))
                .collect(Collectors.joining("; "));
        dati.put("Prodotti Inclusi", listaProdotti);

        dati.put("Prezzo Calcolato", String.format("%.2f €", getPrezzoVendita()));

        return dati;
    }

    public void aggiungiProdotto(ArticoloCatalogo articolo, int quantita) {
        if (articolo != null && quantita > 0) {
            Optional<ProdottoPacchetto> existing = prodottiInclusi.stream()
                    .filter(pp -> pp.getArticolo().getId().equals(articolo.getId()))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().setQuantita(existing.get().getQuantita() + quantita);
            } else {
                prodottiInclusi.add(new ProdottoPacchetto(this, articolo, quantita));
            }
        }
    }

    public void rimuoviProdotto(ArticoloCatalogo articolo) {
        prodottiInclusi.removeIf(pp -> pp.getArticolo().getId().equals(articolo.getId()));
    }

    public List<ProdottoPacchetto> getProdottiInclusi() {
        return Collections.unmodifiableList(prodottiInclusi);
    }

    public Map<ArticoloCatalogo, Integer> getProdottiInclusaAsMap() {
        return prodottiInclusi.stream()
                .collect(Collectors.toMap(
                    ProdottoPacchetto::getArticolo,
                    ProdottoPacchetto::getQuantita
                ));
    }
}