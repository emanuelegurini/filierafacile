package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultPacchettoProdotti;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
public class PacchettoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private Long aziendaId;
    private String nomeAzienda;
    private BigDecimal prezzoTotale;
    private Map<String, Integer> prodottiInclusi;

    public PacchettoResponse() {}

    public PacchettoResponse(DefaultPacchettoProdotti pacchetto) {
        this.id = pacchetto.getId();
        this.nome = pacchetto.getNomeArticolo();
        this.descrizione = pacchetto.getDescrizioneArticolo();
        this.aziendaId = pacchetto.getAziendaDiRiferimento().getId();
        this.nomeAzienda = pacchetto.getAziendaDiRiferimento().getRagioneSociale();
        this.prezzoTotale = java.math.BigDecimal.valueOf(pacchetto.getPrezzoVendita());
        this.prodottiInclusi = pacchetto.getProdottiInclusi().stream()
                .collect(java.util.stream.Collectors.toMap(
                    pp -> pp.getArticolo().getNomeArticolo(),
                    pp -> pp.getQuantita()
                ));
    }

}