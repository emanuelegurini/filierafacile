package com.filiera.facile.entities;

import com.filiera.facile.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "prodotto")
@DiscriminatorValue("Prodotto")
public class DefaultProdotto extends ArticoloCatalogo {
    @Enumerated(EnumType.STRING)
    @Column(name = "unita_misura", length = 50)
    private UnitaMisura unitaDiMisura;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_prodotto", length = 50)
    private CategoriaProdotto categoriaProdotto;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "prodotto_certificazioni",
                     joinColumns = @JoinColumn(name = "prodotto_id"))
    @Column(name = "certificazione")
    private List<String> certificazioni;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "prodotto_ingredienti",
               joinColumns = @JoinColumn(name = "prodotto_id"),
               inverseJoinColumns = @JoinColumn(name = "ingrediente_id"))
    @JsonIgnore
    private final List<ArticoloCatalogo> ingredienti;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_prodotto", length = 50, nullable = false)
    private final TipoProdotto tipoProdotto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_coltivazione", length = 50)
    private MetodoColtivazione metodoColtivazione;

    @Column(name = "metodo_trasformazione", length = 255)
    private String metodoTrasformazione;

    public DefaultProdotto(
            String nome,
            String descrizione,
            double prezzoUnitario,
            UnitaMisura unitaDiMisura,
            DefaultAzienda defaultAziendaProduttrice,
            TipoProdotto tipoProdotto,
            CategoriaProdotto categoriaProdotto
    ) {
        super(nome, descrizione, prezzoUnitario, defaultAziendaProduttrice);
        this.unitaDiMisura = unitaDiMisura;
        this.tipoProdotto = Objects.requireNonNull(tipoProdotto, "Il tipo di prodotto non può essere nullo");
        this.ingredienti = new ArrayList<>();
        this.certificazioni = new ArrayList<>();
        this.categoriaProdotto = Objects.requireNonNull(categoriaProdotto, "La categoria non può essere nulla");
    }

    @Override
    public double getPrezzoVendita() {
        return this.getPrezzoUnitario();
    }

    @Override
    public Map<String, String> getDatiPerValidazione() {
        Map<String, String> dati = new LinkedHashMap<>();

        dati.put("ID Prodotto", this.id.toString());
        dati.put("Nome Prodotto", this.nome);
        dati.put("Azienda", this.aziendaDiRiferimento.getRagioneSociale());
        dati.put("Categoria", this.categoriaProdotto.toString());
        dati.put("Tipo Prodotto", this.tipoProdotto.toString());

        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            dati.put("Metodo di Coltivazione", this.metodoColtivazione != null ? this.metodoColtivazione.toString() : "Non specificato");
        } else {
            dati.put("Metodo di Trasformazione", this.metodoTrasformazione != null ? this.metodoTrasformazione : "Non specificato");

            String listaIngredienti = this.ingredienti.isEmpty()
                    ? "Nessuno specificato"
                    : this.ingredienti.stream()
                    .map(ArticoloCatalogo::getNomeArticolo)
                    .collect(Collectors.joining(", "));
            dati.put("Ingredienti", listaIngredienti);
        }

        dati.put("Descrizione", this.descrizione);
        dati.put("Prezzo", String.format("%.2f € / %s", this.getPrezzoUnitario(), this.unitaDiMisura));

        return dati;
    }


    /**
     * Aggiunge un ingrediente a questo prodotto. Può essere qualsiasi {@link ArticoloCatalogo},
     * permettendo una tracciabilità complessa.
     * @param ingrediente L'articolo da aggiungere come ingrediente.
     */
    public void aggiungiIngrediente(ArticoloCatalogo ingrediente) {
        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            throw new IllegalStateException("Non è possibile aggiungere ingredienti a una materia prima.");
        }
        if (ingrediente != null) {
            this.ingredienti.add(ingrediente);
        }
    }

    public List<ArticoloCatalogo> getIngredienti() {
        return Collections.unmodifiableList(this.ingredienti);
    }

    public UnitaMisura getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public void setUnitaDiMisura(UnitaMisura unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public CategoriaProdotto getCategoriaProdotto() {
        return categoriaProdotto;
    }

    public void setCategoriaProdotto(CategoriaProdotto categoriaProdotto) {
        this.categoriaProdotto = categoriaProdotto;
    }

    public List<String> getCertificazioni() {
        return Collections.unmodifiableList(this.certificazioni);
    }

    public void setCertificazioni(List<String> certificazioni) {
        this.certificazioni = (certificazioni == null) ? new ArrayList<>() : new ArrayList<>(certificazioni);
    }

    public DefaultAzienda getAziendaProduttrice() {
        return this.aziendaDiRiferimento;
    }

    public TipoProdotto getTipoProdotto() {
        return this.tipoProdotto;
    }

    public MetodoColtivazione getMetodoColtivazione() {
        return metodoColtivazione;
    }

    public void setMetodoColtivazione(MetodoColtivazione metodoColtivazione) {
        if (this.tipoProdotto == TipoProdotto.TRASFORMATO) {
            throw new IllegalStateException("Non è possibile definire un metodo di coltivazione per un prodotto trasformato.");
        }
        this.metodoColtivazione = metodoColtivazione;
    }

    public String getMetodoTrasformazione() {
        return metodoTrasformazione;
    }

    public void setMetodoTrasformazione(String metodoTrasformazione) {
        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            throw new IllegalStateException("Non è possibile definire un metodo di trasformazione per una materia prima.");
        }
        this.metodoTrasformazione = metodoTrasformazione;
    }
}