package com.filiera.facile.model.interfaces;

import com.filiera.facile.model.enums.StatoCuratore;

import java.util.List;
import java.util.Optional;

public interface CuratoreStatusTracker {

    void registraCuratore(Long curatoreId);

    void rimuoviCuratore(Long curatoreId);

    void setCuratoreOccupato(Long curatoreId);

    void setCuratoreLibero(Long curatoreId);

    void setCuratoreNonDisponibile(Long curatoreId);

    StatoCuratore getStatoCuratore(Long curatoreId);

    List<Long> getCuratoriLiberi();

    Optional<Long> getPrimoCuratoreLibero();

    List<Long> getCuratoriOccupati();

    boolean isCuratoreLibero(Long curatoreId);

    boolean hasCuratoriLiberi();

    int getNumeroTotaleCuratori();
}