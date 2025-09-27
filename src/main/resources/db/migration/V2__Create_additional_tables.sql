-- Create affiliazione table
CREATE TABLE affiliazione (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utente_id BIGINT NOT NULL,
    azienda_id BIGINT NOT NULL,
    ruolo_aziendale VARCHAR(50) NOT NULL,
    data_affiliazione DATETIME NOT NULL,
    FOREIGN KEY (utente_id) REFERENCES utente(id),
    FOREIGN KEY (azienda_id) REFERENCES azienda(id),
    UNIQUE KEY unique_utente_azienda (utente_id, azienda_id)
);

-- Create evento table
CREATE TABLE evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descrizione TEXT,
    data_ora_inizio DATETIME,
    data_ora_fine DATETIME,
    organizzatore_id BIGINT,
    indirizzo VARCHAR(500),
    coordinate_id BIGINT,
    posti_disponibili INT,
    costo_partecipazione DOUBLE,
    stato_validazione VARCHAR(50),
    FOREIGN KEY (organizzatore_id) REFERENCES utente(id),
    FOREIGN KEY (coordinate_id) REFERENCES coordinate(id)
);

-- Create evento_aziende table (many-to-many between evento and azienda)
CREATE TABLE evento_aziende (
    evento_id BIGINT NOT NULL,
    azienda_id BIGINT NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (azienda_id) REFERENCES azienda(id),
    PRIMARY KEY (evento_id, azienda_id)
);

-- Create carrello table
CREATE TABLE carrello (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utente_id BIGINT NOT NULL,
    FOREIGN KEY (utente_id) REFERENCES utente(id)
);

-- Create riga_carrello table
CREATE TABLE riga_carrello (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrello_id BIGINT NOT NULL,
    articolo_id BIGINT NOT NULL,
    quantita INT NOT NULL,
    FOREIGN KEY (carrello_id) REFERENCES carrello(id),
    FOREIGN KEY (articolo_id) REFERENCES articolo_catalogo(id)
);

-- Create pacchetto_prodotti table (extends articolo_catalogo)
CREATE TABLE pacchetto_prodotti (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES articolo_catalogo(id)
);

-- Create pacchetto_prodotti_inclusi table
CREATE TABLE pacchetto_prodotti_inclusi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pacchetto_id BIGINT NOT NULL,
    articolo_id BIGINT NOT NULL,
    quantita INT NOT NULL,
    FOREIGN KEY (pacchetto_id) REFERENCES pacchetto_prodotti(id),
    FOREIGN KEY (articolo_id) REFERENCES articolo_catalogo(id)
);