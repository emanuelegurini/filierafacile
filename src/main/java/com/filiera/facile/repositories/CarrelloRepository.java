package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarrelloRepository extends JpaRepository<DefaultCarrello, Long> {

    /**
     * Trova carrello attivo per utente.
     */
    Optional<DefaultCarrello> findByUtenteId(Long utenteId);

    /**
     * Calcola il totale del carrello con una query custom.
     */
    @Query("SELECT SUM(r.quantita * p.prezzo) FROM DefaultCarrello c JOIN c.righeCarrello r JOIN r.prodotto p WHERE c.id = :carrelloId")
    Double calcolaTotaleCarrello(@Param("carrelloId") Long carrelloId);
}