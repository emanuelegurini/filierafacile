package com.filiera.facile.model.interfaces;

import com.filiera.facile.domain.DefaultCarrello;

import java.util.Optional;
import java.util.UUID;

public interface CarrelloRepository {
    void save(DefaultCarrello carrello);
    Optional<DefaultCarrello> findByUtenteId(UUID utenteId);
}
