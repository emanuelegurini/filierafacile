-- Insert sample shopping carts
INSERT INTO carrello (utente_id) VALUES
(1), -- Mario Rossi
(2), -- Luca Bianchi
(3); -- Giuseppe Verdi

-- Insert cart items (riga_carrello)
INSERT INTO riga_carrello (carrello_id, articolo_id, quantita) VALUES
-- Mario Rossi's cart
(1, 1, 2), -- 2 kg di Pomodori Bio
(1, 2, 1), -- 1 litro di Olio Extra Vergine

-- Luca Bianchi's cart
(2, 3, 3), -- 3 kg di Pasta Integrale
(2, 4, 1), -- 1 Bundle Pasta e Sugo

-- Giuseppe Verdi's cart
(3, 1, 5), -- 5 kg di Pomodori Bio
(3, 2, 2); -- 2 litri di Olio Extra Vergine