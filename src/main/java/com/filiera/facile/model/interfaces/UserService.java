package com.filiera.facile.model.interfaces;

import com.filiera.facile.application.dto.RegistrazioneUtenteDTO;
import com.filiera.facile.domain.DefaultUtente;
import com.filiera.facile.model.enums.RuoloAziendale;

import java.util.UUID;

public interface UserService {

    DefaultUtente registraNuovoUtente(RegistrazioneUtenteDTO registrazioneUtenteDTO);

    void aggiungiAffiliazione(UUID utenteId, UUID aziendaId, RuoloAziendale ruolo);
}
