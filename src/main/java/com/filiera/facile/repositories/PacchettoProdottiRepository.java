package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultPacchettoProdotti;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.StatoValidazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PacchettoProdottiRepository extends JpaRepository<DefaultPacchettoProdotti, UUID> {

    /**
     * Trova pacchetti per azienda distributrice.
     */
    List<DefaultPacchettoProdotti> findByAziendaDiRiferimento(DefaultAzienda azienda);

    /**
     * Trova pacchetti per stato di validazione.
     */
    List<DefaultPacchettoProdotti> findByStato(StatoValidazione stato);

    /**
     * Trova pacchetti per nome (ricerca parziale case-insensitive).
     */
    List<DefaultPacchettoProdotti> findByNomeContainingIgnoreCase(String nome);

    /**
     * Trova pacchetti approvati.
     */
    @Query("SELECT p FROM DefaultPacchettoProdotti p WHERE p.stato = 'APPROVATO'")
    List<DefaultPacchettoProdotti> findPacchettiApprovati();

    /**
     * Conta pacchetti per azienda.
     */
    long countByAziendaDiRiferimento(DefaultAzienda azienda);
}