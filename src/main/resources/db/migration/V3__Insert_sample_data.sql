-- Insert sample coordinates
INSERT INTO coordinate (latitude, longitude) VALUES
(45.4642, 9.1900),   -- Milano
(41.9028, 12.4964),  -- Roma
(45.0703, 7.6869),   -- Torino
(43.7696, 11.2558),  -- Firenze
(40.8518, 14.2681);  -- Napoli

-- Insert sample aziende
INSERT INTO azienda (ragione_sociale, partita_iva, indirizzo, email, numero_telefono, sito_web, registration_date, coordinate_id) VALUES
('Agricola Verde Srl', '12345678901', 'Via Roma 123, Milano', 'info@agricolaverde.it', '+39 02 1234567', 'https://www.agricolaverde.it', NOW(), 1),
('Prodotti del Sole Spa', '98765432109', 'Corso Vittorio 456, Roma', 'contatti@prodottidelsole.it', '+39 06 7654321', 'https://www.prodottidelsole.it', NOW(), 2),
('Distributore Alimentare Spa', '11223344556', 'Via Torino 789, Torino', 'vendite@distributore.it', '+39 011 9876543', 'https://www.distributore.it', NOW(), 3);

-- Insert azienda types
INSERT INTO azienda_tipi (azienda_id, tipo) VALUES
(1, 'PRODUTTORE'),
(2, 'PRODUTTORE'),
(3, 'DISTRIBUTORE');

-- Insert sample users
INSERT INTO utente (nome, cognome, email, address, phone_number, password_hash) VALUES
('Mario', 'Rossi', 'mario.rossi@email.it', 'Via Verdi 123, Milano', '+39 333 1234567', '$2a$10$N9qo8uLOickgx2ZMRZoMye7xQlq.4ZLyH5QEuJBqhRdNWA3qCKCla'),
('Luca', 'Bianchi', 'luca.bianchi@email.it', 'Piazza Garibaldi 456, Roma', '+39 335 7654321', '$2a$10$N9qo8uLOickgx2ZMRZoMye7xQlq.4ZLyH5QEuJBqhRdNWA3qCKCla'),
('Giuseppe', 'Verdi', 'giuseppe.verdi@email.it', 'Corso Dante 789, Torino', '+39 347 9876543', '$2a$10$N9qo8uLOickgx2ZMRZoMye7xQlq.4ZLyH5QEuJBqhRdNWA3qCKCla');

-- Insert user roles
INSERT INTO utente_ruoli (utente_id, ruolo) VALUES
(1, 'ACQUIRENTE'),
(1, 'PRODUTTORE'),
(2, 'ACQUIRENTE'),
(2, 'CURATORE'),
(3, 'ACQUIRENTE'),
(3, 'DISTRIBUTORE');

-- Insert affiliazioni
INSERT INTO affiliazione (utente_id, azienda_id, ruolo_aziendale, data_affiliazione) VALUES
(1, 1, 'PROPRIETARIO', NOW()),
(2, 2, 'PROPRIETARIO', NOW()),
(3, 3, 'PROPRIETARIO', NOW());

-- Insert sample articles in catalog
INSERT INTO articolo_catalogo (dtype, nome, descrizione, prezzo_unitario, quantita_disponibile, azienda_id, stato) VALUES
('Prodotto', 'Pomodori Bio', 'Pomodori biologici coltivati in serra', 3.50, 100.0, 1, 'APPROVATO'),
('Prodotto', 'Olio Extra Vergine', 'Olio extravergine di oliva spremuto a freddo', 15.99, 50.0, 2, 'APPROVATO'),
('Prodotto', 'Pasta Integrale', 'Pasta integrale di grano duro biologico', 2.80, 200.0, 2, 'IN_ATTESA_DI_APPROVAZIONE'),
('PacchettoProdotti', 'Bundle Pasta e Sugo', 'Pacchetto completo per un pranzo veloce', 0.0, 30.0, 3, 'APPROVATO');

-- Insert specific product data
INSERT INTO prodotto (id, unita_misura, categoria_prodotto, tipo_prodotto, metodo_coltivazione, metodo_trasformazione) VALUES
(1, 'KG', 'ORTAGGI', 'MATERIA_PRIMA', 'BIOLOGICO', NULL),
(2, 'LITRI', 'CONDIMENTI', 'TRASFORMATO', NULL, 'Spremitura a freddo'),
(3, 'KG', 'CEREALI', 'TRASFORMATO', NULL, 'Macinazione integrale');

-- Insert package products data
INSERT INTO pacchetto_prodotti (id) VALUES (4);

-- Insert products in packages
INSERT INTO pacchetto_prodotti_inclusi (pacchetto_id, articolo_id, quantita) VALUES
(4, 2, 1),  -- 1x Olio Extra Vergine
(4, 3, 2);  -- 2x Pasta Integrale

-- Insert product certifications
INSERT INTO prodotto_certificazioni (prodotto_id, certificazione) VALUES
(1, 'Certificazione Biologica EU'),
(1, 'KM Zero'),
(2, 'DOP'),
(2, 'Certificazione Biologica EU');

-- Insert some inventory data
INSERT INTO azienda_magazzino (azienda_id, prodotto_id, quantita) VALUES
(1, 1, 150),  -- Agricola Verde ha 150 kg di pomodori
(2, 2, 80),   -- Prodotti del Sole ha 80 litri di olio
(2, 3, 250);  -- Prodotti del Sole ha 250 kg di pasta