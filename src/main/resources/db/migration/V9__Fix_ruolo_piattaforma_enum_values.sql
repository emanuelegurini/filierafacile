-- Fix ruolo enum values in utente_ruoli table to match Java enum RuoloPiattaforma
-- Available values: ACQUIRENTE, CURATORE, ANIMATORE_FILIERA, GESTORE_PIATTAFORMA

-- Replace PRODUTTORE with ANIMATORE_FILIERA (closest business logic match)
UPDATE utente_ruoli
SET ruolo = 'ANIMATORE_FILIERA'
WHERE ruolo = 'PRODUTTORE';

-- Replace DISTRIBUTORE with ANIMATORE_FILIERA (closest business logic match)
UPDATE utente_ruoli
SET ruolo = 'ANIMATORE_FILIERA'
WHERE ruolo = 'DISTRIBUTORE';