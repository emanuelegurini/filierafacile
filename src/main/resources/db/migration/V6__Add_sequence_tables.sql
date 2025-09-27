-- Create sequence tables for entities using GenerationType.AUTO
-- MySQL uses tables to simulate sequences for GenerationType.AUTO

CREATE TABLE utente_seq (
    next_val BIGINT
);

INSERT INTO utente_seq VALUES (1);