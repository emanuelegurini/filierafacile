package com.filiera.facile.entities;

import com.filiera.facile.model.interfaces.CodaValidazione;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DefaultCodaValidazione implements CodaValidazione {
    private final Queue<DefaultPraticaValidazione> codaPendenti;
    private final Map<Long, DefaultPraticaValidazione> indicePerID;

    public DefaultCodaValidazione() {
        this.codaPendenti = new ConcurrentLinkedQueue<>();
        this.indicePerID = new HashMap<>();
    }

    @Override
    public synchronized void aggiungiPratica(DefaultPraticaValidazione pratica) {
        Objects.requireNonNull(pratica, "La pratica non può essere null");
        if (indicePerID.containsKey(pratica.getId())) {
            throw new IllegalArgumentException("Pratica già presente nella coda: " + pratica.getId());
        }

        codaPendenti.offer(pratica);
        indicePerID.put(pratica.getId(), pratica);
    }

    @Override
    public synchronized Optional<DefaultPraticaValidazione> ottieniProssimaPratica() {
        DefaultPraticaValidazione pratica = codaPendenti.poll();
        if (pratica != null) {
            indicePerID.remove(pratica.getId());
        }
        return Optional.ofNullable(pratica);
    }

    @Override
    public synchronized void rimuoviPratica(Long praticaId) {
        Objects.requireNonNull(praticaId, "L'ID della pratica non può essere null");
        DefaultPraticaValidazione pratica = indicePerID.remove(praticaId);
        if (pratica != null) {
            codaPendenti.remove(pratica);
        }
    }

    @Override
    public synchronized List<DefaultPraticaValidazione> getPratichePendenti() {
        return new ArrayList<>(codaPendenti);
    }

    @Override
    public int size() {
        return codaPendenti.size();
    }

    @Override
    public boolean isEmpty() {
        return codaPendenti.isEmpty();
    }

    @Override
    public Optional<DefaultPraticaValidazione> getPraticaById(Long praticaId) {
        return Optional.ofNullable(indicePerID.get(praticaId));
    }
}