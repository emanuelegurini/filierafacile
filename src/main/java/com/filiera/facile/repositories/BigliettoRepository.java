package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultBiglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BigliettoRepository extends JpaRepository<DefaultBiglietto, UUID> {

    /**
     * Trova biglietti per evento.
     */
    List<DefaultBiglietto> findByEventoId(UUID eventoId);

    /**
     * Trova biglietti per utente.
     */
    List<DefaultBiglietto> findByUtenteId(UUID utenteId);

    /**
     * Conta biglietti per evento.
     */
    long countByEventoId(UUID eventoId);
}