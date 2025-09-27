-- Insert sample events
INSERT INTO evento (nome, descrizione, data_ora_inizio, data_ora_fine, organizzatore_id, indirizzo, coordinate_id, posti_disponibili, costo_partecipazione, stato_validazione) VALUES
('Mercato Bio Milano', 'Mercato biologico con prodotti locali e degustazioni', '2025-10-15 09:00:00', '2025-10-15 18:00:00', 1, 'Piazza Duomo, Milano', 1, 100, 5.00, 2),
('Fiera del Tartufo', 'Mostra e degustazione di tartufi del territorio', '2025-11-20 10:00:00', '2025-11-20 16:00:00', 2, 'Centro Storico, Roma', 2, 50, 15.00, 2),
('Workshop Agricoltura Sostenibile', 'Corso pratico su tecniche di coltivazione biologica', '2025-12-05 14:00:00', '2025-12-05 17:00:00', 3, 'Via Po 123, Torino', 3, 30, 25.00, 0);

-- Associate companies with events
INSERT INTO evento_aziende (evento_id, azienda_id) VALUES
(1, 1), -- Agricola Verde partecipa al Mercato Bio Milano
(1, 2), -- Prodotti del Sole partecipa al Mercato Bio Milano
(2, 2), -- Prodotti del Sole partecipa alla Fiera del Tartufo
(3, 1), -- Agricola Verde organizza il Workshop
(3, 3); -- Distributore Alimentare partecipa al Workshop