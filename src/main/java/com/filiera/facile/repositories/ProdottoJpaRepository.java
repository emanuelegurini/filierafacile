package com.filiera.facile.repositories;

import com.filiera.facile.entities.DefaultProdotto;
import com.filiera.facile.entities.DefaultAzienda;
import com.filiera.facile.model.enums.CategoriaProdotto;
import com.filiera.facile.model.enums.StatoValidazione;
import com.filiera.facile.model.enums.TipoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProdottoJpaRepository extends JpaRepository<DefaultProdotto, UUID> {

    /**
     * Trova prodotti per azienda produttrice.
     */
    List<DefaultProdotto> findByAziendaDiRiferimento(DefaultAzienda azienda);

    /**
     * Trova prodotti per categoria.
     */
    List<DefaultProdotto> findByCategoriaProdotto(CategoriaProdotto categoria);

    /**
     * Trova prodotti per tipo (materia prima vs trasformato).
     */
    List<DefaultProdotto> findByTipoProdotto(TipoProdotto tipo);

    /**
     * Trova prodotti per stato di validazione.
     */
    List<DefaultProdotto> findByStato(StatoValidazione stato);

    /**
     * Trova prodotti per nome (ricerca parziale case-insensitive).
     */
    List<DefaultProdotto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Trova prodotti approvati di una specifica azienda.
     */
    @Query("SELECT p FROM DefaultProdotto p WHERE p.aziendaDiRiferimento = :azienda AND p.stato = 'APPROVATO'")
    List<DefaultProdotto> findProdottiApprovatiByAzienda(@Param("azienda") DefaultAzienda azienda);

    /**
     * Trova materie prime approvate per la creazione di prodotti trasformati.
     */
    @Query("SELECT p FROM DefaultProdotto p WHERE p.tipoProdotto = 'MATERIA_PRIMA' AND p.stato = 'APPROVATO'")
    List<DefaultProdotto> findMateriePrimeApprovate();

    /**
     * Conta prodotti per azienda.
     */
    long countByAziendaDiRiferimento(DefaultAzienda azienda);
}