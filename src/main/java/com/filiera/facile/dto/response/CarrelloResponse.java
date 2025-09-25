package com.filiera.facile.dto.response;

import com.filiera.facile.entities.DefaultCarrello;
import com.filiera.facile.entities.DefaultRigaCarrello;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CarrelloResponse {

    private Long id;
    private Long utenteId;
    private List<RigaCarrelloResponse> righe;
    private BigDecimal totaleComplessivo;

    public CarrelloResponse() {}

    public CarrelloResponse(DefaultCarrello carrello) {
        this.id = carrello.getId();
        this.utenteId = carrello.getUtenteId();
        // Use the persistent collection directly
        this.righe = carrello.getRigheLista().stream()
                .map(RigaCarrelloResponse::new)
                .collect(Collectors.toList());
        this.totaleComplessivo = BigDecimal.valueOf(
            carrello.getRigheLista().stream()
                .mapToDouble(DefaultRigaCarrello::getPrezzoTotaleRiga)
                .sum()
        );
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUtenteId() { return utenteId; }
    public void setUtenteId(Long utenteId) { this.utenteId = utenteId; }

    public List<RigaCarrelloResponse> getRighe() { return righe; }
    public void setRighe(List<RigaCarrelloResponse> righe) { this.righe = righe; }

    public BigDecimal getTotaleComplessivo() { return totaleComplessivo; }
    public void setTotaleComplessivo(BigDecimal totaleComplessivo) { this.totaleComplessivo = totaleComplessivo; }

    public static class RigaCarrelloResponse {
        private Long articoloId;
        private String nomeArticolo;
        private int quantita;
        private BigDecimal prezzoUnitario;
        private BigDecimal prezzoTotaleRiga;

        public RigaCarrelloResponse() {}

        public RigaCarrelloResponse(DefaultRigaCarrello riga) {
            this.articoloId = riga.getArticolo().getId();
            this.nomeArticolo = riga.getArticolo().getNomeArticolo();
            this.quantita = riga.getQuantita();
            this.prezzoUnitario = BigDecimal.valueOf(riga.getArticolo().getPrezzoUnitario());
            this.prezzoTotaleRiga = BigDecimal.valueOf(riga.getPrezzoTotaleRiga());
        }

        // Getters and setters
        public Long getArticoloId() { return articoloId; }
        public void setArticoloId(Long articoloId) { this.articoloId = articoloId; }

        public String getNomeArticolo() { return nomeArticolo; }
        public void setNomeArticolo(String nomeArticolo) { this.nomeArticolo = nomeArticolo; }

        public int getQuantita() { return quantita; }
        public void setQuantita(int quantita) { this.quantita = quantita; }

        public BigDecimal getPrezzoUnitario() { return prezzoUnitario; }
        public void setPrezzoUnitario(BigDecimal prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }

        public BigDecimal getPrezzoTotaleRiga() { return prezzoTotaleRiga; }
        public void setPrezzoTotaleRiga(BigDecimal prezzoTotaleRiga) { this.prezzoTotaleRiga = prezzoTotaleRiga; }
    }
}