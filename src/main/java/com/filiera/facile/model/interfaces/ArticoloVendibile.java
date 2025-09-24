package com.filiera.facile.model.interfaces;

import java.util.UUID;

/**
 * Definisce il contratto per qualsiasi oggetto che possa essere venduto
 * sulla piattaforma.
 *
 * Questa astrazione permette di gestire in modo polimorfico diversi tipi di articoli
 * (es. Prodotto, PacchettoEsperienziale) all'interno del carrello, degli ordini
 * e del catalogo vendite.
 */
public interface ArticoloVendibile {

    /**
     */
    Long getId();

    /**
     * Restituisce il nome dell'articolo da mostrare all'utente.
     * @return Il nome visualizzato.
     */
    String getNomeArticolo();

    /**
     *  TODO: ADD COMMENT
     */
    void setNomeArticolo(String nomeArticolo);


    /**
     * Restituisce la descrizione dell'articolo.
     * @return La descrizione dettagliata.
     */
    String getDescrizioneArticolo();

    /**
     * Restituisce la descrizione dell'articolo.
     */
    void setDescrizioneArticolo(String descrizioneArticolo);

    /**
     * Restituisce il prezzo finale di vendita dell'articolo.
     * Per un prodotto semplice, sarà il suo prezzo unitario. Per un pacchetto,
     * potrebbe essere una somma o un prezzo scontato.
     * @return Il prezzo di vendita.
     */
    double getPrezzoVendita();
}
