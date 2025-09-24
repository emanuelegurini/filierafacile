package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultUtente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<DefaultUtente, Long> {

    /**
     * Trova un utente per email.
     * Spring Data JPA genera automaticamente l'implementazione.
     */
    Optional<DefaultUtente> findByEmail(String email);

    /**
     * Verifica se esiste un utente con l'email specificata.
     * Utile per validazioni di unicità.
     */
    boolean existsByEmail(String email);

    /**
     * Query custom per trovare utenti per nome e cognome.
     * Esempio di @Query personalizzata se necessario.
     */
    @Query("SELECT u FROM DefaultUtente u WHERE u.nome = :nome AND u.cognome = :cognome")
    Optional<DefaultUtente> findByNomeAndCognome(@Param("nome") String nome, @Param("cognome") String cognome);
}