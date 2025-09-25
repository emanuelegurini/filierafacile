package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<DefaultEvento, Long> {

    /**
     * Trova eventi per azienda organizzatrice.
     */
    List<DefaultEvento> findByOrganizzatoreId(Long organizzatoreId);

    /**
     * Trova eventi futuri.
     */
    @Query("SELECT e FROM DefaultEvento e WHERE e.dataOraInizio > :now")
    List<DefaultEvento> findEventiInProgramma(@Param("now") LocalDateTime now);

    /**
     * Trova eventi per nome (ricerca parziale).
     */
    List<DefaultEvento> findByNomeContainingIgnoreCase(String nome);

    /**
     * Trova eventi dove l'azienda partecipa.
     */
    @Query("SELECT e FROM DefaultEvento e JOIN e.aziendePartecipanti a WHERE a.id = :aziendaId")
    List<DefaultEvento> findByAziendaPartecipante(@Param("aziendaId") Long aziendaId);
}