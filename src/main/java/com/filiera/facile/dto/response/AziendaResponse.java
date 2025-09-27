package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class AziendaResponse {

    private Long id;
    private String nome;
    private String partitaIva;
    private String descrizione;
    private Set<TipoAzienda> tipiAzienda;
    private LocalDateTime dataCreazione;

    public AziendaResponse() {}

    public AziendaResponse(DefaultAzienda azienda) {
        this.id = azienda.getId();
        this.nome = azienda.getRagioneSociale();
        this.partitaIva = azienda.getPartitaIva();
        // Mappare i campi disponibili dall'entità
        this.descrizione = "Azienda specializzata nella filiera alimentare"; // Campo non presente nell'entità
        this.tipiAzienda = azienda.getTipoAzienda(); // Metodo corretto (singolare)
        this.dataCreazione = null; // Getter mancante nell'entità, TODO: aggiungere
    }

}