package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultBiglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BigliettoRepository extends JpaRepository<DefaultBiglietto, Long> {

    /**
     * Trova biglietti per evento.
     */
    List<DefaultBiglietto> findByEventoId(Long eventoId);

    /**
     * Trova biglietti per utente.
     */
    List<DefaultBiglietto> findByIntestatarioId(Long intestatarioId);

    /**
     * Conta biglietti per evento.
     */
    long countByEventoId(Long eventoId);
}