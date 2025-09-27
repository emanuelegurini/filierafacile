package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.model.enums.StatoValidazione;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private Long aziendaId;
    private String nomeAzienda;
    private LocalDateTime dataCreazione;
    private StatoValidazione statoValidazione;

    public ProdottoResponse() {}

    public ProdottoResponse(DefaultProdotto prodotto) {
        this.id = prodotto.getId();
        // Mappare correttamente i campi dall'entità
        this.nome = prodotto.getNomeArticolo();
        this.descrizione = prodotto.getDescrizioneArticolo(); // Ora mappato correttamente
        this.prezzo = BigDecimal.valueOf(prodotto.getPrezzoUnitario());
        this.aziendaId = prodotto.getAziendaProduttrice() != null ? prodotto.getAziendaProduttrice().getId() : null;
        this.nomeAzienda = prodotto.getAziendaProduttrice() != null ? prodotto.getAziendaProduttrice().getRagioneSociale() : null;
        this.dataCreazione = null; // Campo non presente nell'entità DefaultProdotto
        this.statoValidazione = prodotto.getStatoValidazione();
    }

}