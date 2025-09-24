package com.filiera.facile.entities;

import com.filiera.facile.model.enums.StatoCuratore;
import com.filiera.facile.model.interfaces.CuratoreStatusTracker;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultCuratoreStatusTracker implements CuratoreStatusTracker {
    private final Map<Long, StatoCuratore> statoCuratori;

    public DefaultCuratoreStatusTracker() {
        this.statoCuratori = new ConcurrentHashMap<>();
    }

    @Override
    public void registraCuratore(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        statoCuratori.put(curatoreId, StatoCuratore.LIBERO);
    }

    @Override
    public void rimuoviCuratore(Long curatoreId) {
        statoCuratori.remove(curatoreId);
    }

    @Override
    public void setCuratoreOccupato(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        if (!statoCuratori.containsKey(curatoreId)) {
            throw new IllegalArgumentException("Curatore non registrato nel sistema: " + curatoreId);
        }
        statoCuratori.put(curatoreId, StatoCuratore.OCCUPATO);
    }

    @Override
    public void setCuratoreLibero(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        if (!statoCuratori.containsKey(curatoreId)) {
            throw new IllegalArgumentException("Curatore non registrato nel sistema: " + curatoreId);
        }
        statoCuratori.put(curatoreId, StatoCuratore.LIBERO);
    }

    @Override
    public void setCuratoreNonDisponibile(Long curatoreId) {
        Objects.requireNonNull(curatoreId, "L'ID del curatore non può essere null");
        if (!statoCuratori.containsKey(curatoreId)) {
            throw new IllegalArgumentException("Curatore non registrato nel sistema: " + curatoreId);
        }
        statoCuratori.put(curatoreId, StatoCuratore.NON_DISPONIBILE);
    }

    @Override
    public StatoCuratore getStatoCuratore(Long curatoreId) {
        return statoCuratori.get(curatoreId);
    }

    @Override
    public List<Long> getCuratoriLiberi() {
        return statoCuratori.entrySet().stream()
                .filter(entry -> StatoCuratore.LIBERO.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Long> getPrimoCuratoreLibero() {
        return getCuratoriLiberi().stream().findFirst();
    }

    @Override
    public List<Long> getCuratoriOccupati() {
        return statoCuratori.entrySet().stream()
                .filter(entry -> StatoCuratore.OCCUPATO.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCuratoreLibero(Long curatoreId) {
        return StatoCuratore.LIBERO.equals(statoCuratori.get(curatoreId));
    }

    @Override
    public boolean hasCuratoriLiberi() {
        return !getCuratoriLiberi().isEmpty();
    }

    @Override
    public int getNumeroTotaleCuratori() {
        return statoCuratori.size();
    }
}