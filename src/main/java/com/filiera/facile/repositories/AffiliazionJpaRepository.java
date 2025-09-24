package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultAffiliazione;
import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.RuoloAziendale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliazionJpaRepository extends JpaRepository<DefaultAffiliazione, UUID> {

    /**
     * Trova affiliazioni per utente.
     */
    List<DefaultAffiliazione> findByDefaultUtente(DefaultUtente utente);

    /**
     * Trova affiliazioni per azienda.
     */
    List<DefaultAffiliazione> findByDefaultAzienda(DefaultAzienda azienda);

    /**
     * Trova affiliazione specifica utente-azienda.
     */
    Optional<DefaultAffiliazione> findByDefaultUtenteAndDefaultAzienda(DefaultUtente utente, DefaultAzienda azienda);

    /**
     * Trova affiliazioni per ruolo aziendale.
     */
    List<DefaultAffiliazione> findByRuoloAziendale(RuoloAziendale ruolo);

    /**
     * Verifica se esiste affiliazione tra utente e azienda.
     */
    boolean existsByDefaultUtenteAndDefaultAzienda(DefaultUtente utente, DefaultAzienda azienda);

    /**
     * Trova utenti affiliati ad una azienda con un ruolo specifico.
     */
    @Query("SELECT a.defaultUtente FROM DefaultAffiliazione a WHERE a.defaultAzienda = :azienda AND a.ruoloAziendale = :ruolo")
    List<DefaultUtente> findUtentiByAziendaAndRuolo(@Param("azienda") DefaultAzienda azienda, @Param("ruolo") RuoloAziendale ruolo);

    /**
     * Conta affiliazioni per azienda.
     */
    long countByDefaultAzienda(DefaultAzienda azienda);
}