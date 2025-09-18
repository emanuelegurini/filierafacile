package com.filiera.facile.repository;

import com.filiera.facile.domain.DefaultCarrello;
import com.filiera.facile.model.interfaces.CarrelloRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCarrelloRepository implements CarrelloRepository {

    // La mappa ora vive qui, nel repository, non più nel service.
    private final Map<UUID, DefaultCarrello> database = new ConcurrentHashMap<>();

    @Override
    public void save(DefaultCarrello carrello) {
        database.put(carrello.getUtenteId(), carrello);
    }

    @Override
    public Optional<DefaultCarrello> findByUtenteId(UUID utenteId) {
        return Optional.ofNullable(database.get(utenteId));
    }
}