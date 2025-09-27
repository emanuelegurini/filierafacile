-- Create biglietto table
CREATE TABLE biglietto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    intestatario_id BIGINT NOT NULL,
    data_emissione DATETIME NOT NULL,
    prezzo_pagato DOUBLE NOT NULL,
    stato INT NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (intestatario_id) REFERENCES utente(id)
);