package com.filiera.facile.model.interfaces;

import com.filiera.facile.entities.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;

public interface UserService {

    DefaultUtente registraNuovoUtente(DefaultUtente utente);

    void aggiungiAffiliazione(Long utenteId, Long aziendaId, RuoloAziendale ruolo);
}
