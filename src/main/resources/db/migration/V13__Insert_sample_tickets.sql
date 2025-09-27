-- Insert sample tickets
INSERT INTO biglietto (evento_id, intestatario_id, data_emissione, prezzo_pagato, stato) VALUES
-- Tickets for Mercato Bio Milano (evento_id = 1, costo = 5.00)
(1, 1, '2025-10-10 14:30:00', 5.00, 0), -- Mario Rossi, VALIDO
(1, 2, '2025-10-12 16:20:00', 5.00, 0), -- Luca Bianchi, VALIDO

-- Tickets for Fiera del Tartufo (evento_id = 2, costo = 15.00)
(2, 2, '2025-11-15 10:45:00', 15.00, 0), -- Luca Bianchi, VALIDO
(2, 3, '2025-11-18 09:15:00', 15.00, 1), -- Giuseppe Verdi, UTILIZZATO

-- Tickets for Workshop Agricoltura Sostenibile (evento_id = 3, costo = 25.00)
(3, 1, '2025-12-01 11:00:00', 25.00, 0), -- Mario Rossi, VALIDO
(3, 3, '2025-12-02 15:30:00', 25.00, 2); -- Giuseppe Verdi, ANNULLATO