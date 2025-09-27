-- Fix enum column types to match JPA annotations
-- DefaultEvento uses EnumType.ORDINAL for stato_validazione, so it should be INT
-- DefaultBiglietto uses EnumType.ORDINAL for stato, so it should be INT

ALTER TABLE evento
MODIFY COLUMN stato_validazione INT;