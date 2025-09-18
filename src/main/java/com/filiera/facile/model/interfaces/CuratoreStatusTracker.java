package com.filiera.facile.model.interfaces;

import com.filiera.facile.model.enums.StatoCuratore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuratoreStatusTracker {

    void registraCuratore(UUID curatoreId);

    void rimuoviCuratore(UUID curatoreId);

    void setCuratoreOccupato(UUID curatoreId);

    void setCuratoreLibero(UUID curatoreId);

    void setCuratoreNonDisponibile(UUID curatoreId);

    StatoCuratore getStatoCuratore(UUID curatoreId);

    List<UUID> getCuratoriLiberi();

    Optional<UUID> getPrimoCuratoreLibero();

    List<UUID> getCuratoriOccupati();

    boolean isCuratoreLibero(UUID curatoreId);

    boolean hasCuratoriLiberi();

    int getNumeroTotaleCuratori();
}