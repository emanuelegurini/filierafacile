package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.TipoAzienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AziendaRepository extends JpaRepository<DefaultAzienda, UUID> {

    /**
     * Trova un'azienda per partita IVA.
     * Spring Data JPA genera automaticamente l'implementazione.
     */
    Optional<DefaultAzienda> findByPartitaIva(String partitaIva);

    /**
     * Verifica se esiste un'azienda con la partita IVA specificata.
     */
    boolean existsByPartitaIva(String partitaIva);

    /**
     * Trova aziende per ragione sociale (ricerca parziale case-insensitive).
     */
    List<DefaultAzienda> findByRagioneSocialeContainingIgnoreCase(String ragioneSociale);

    /**
     * Trova aziende per tipo di attività.
     * Query custom per gestire la collezione ElementCollection.
     */
    @Query("SELECT a FROM DefaultAzienda a JOIN a.tipiAzienda t WHERE t = :tipo")
    List<DefaultAzienda> findByTipoAzienda(@Param("tipo") TipoAzienda tipo);

    /**
     * Trova aziende per email.
     */
    Optional<DefaultAzienda> findByEmail(String email);
}