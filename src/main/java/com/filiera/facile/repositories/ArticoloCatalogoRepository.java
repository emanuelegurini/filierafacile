package com.filiera.facile.repositories;

import com.filiera.facile.entities.ArticoloCatalogo;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.StatoValidazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticoloCatalogoRepository extends JpaRepository<ArticoloCatalogo, Long> {

    /**
     * Trova tutti gli articoli (prodotti + pacchetti) per azienda.
     * Sfrutta il polimorfismo della gerarchia JOINED.
     */
    List<ArticoloCatalogo> findByAziendaDiRiferimento(DefaultAzienda azienda);

    /**
     * Trova articoli per stato di validazione.
     */
    List<ArticoloCatalogo> findByStato(StatoValidazione stato);

    /**
     * Trova articoli per nome (ricerca parziale case-insensitive).
     */
    List<ArticoloCatalogo> findByNomeContainingIgnoreCase(String nome);

    /**
     * Query custom per trovare solo prodotti (non pacchetti).
     * Utilizza il discriminator per filtrare per tipo.
     */
    @Query("SELECT a FROM ArticoloCatalogo a WHERE TYPE(a) = DefaultProdotto")
    List<ArticoloCatalogo> findAllProdotti();

    /**
     * Query custom per trovare solo pacchetti prodotti.
     */
    @Query("SELECT a FROM ArticoloCatalogo a WHERE TYPE(a) = DefaultPacchettoProdotti")
    List<ArticoloCatalogo> findAllPacchetti();

    /**
     * Trova articoli approvati per una azienda specifica.
     */
    @Query("SELECT a FROM ArticoloCatalogo a WHERE a.aziendaDiRiferimento = :azienda AND a.stato = 'APPROVATO'")
    List<ArticoloCatalogo> findArticoliApprovatiByAzienda(@Param("azienda") DefaultAzienda azienda);
}