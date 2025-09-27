-- Create coordinate table
CREATE TABLE coordinate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL
);

-- Create azienda table
CREATE TABLE azienda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ragione_sociale VARCHAR(255) NOT NULL,
    partita_iva VARCHAR(20) NOT NULL UNIQUE,
    indirizzo VARCHAR(500) NOT NULL,
    email VARCHAR(255) NOT NULL,
    numero_telefono VARCHAR(20) NOT NULL,
    sito_web VARCHAR(255),
    registration_date DATETIME,
    coordinate_id BIGINT,
    FOREIGN KEY (coordinate_id) REFERENCES coordinate(id)
);

-- Create azienda_tipi table
CREATE TABLE azienda_tipi (
    azienda_id BIGINT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    FOREIGN KEY (azienda_id) REFERENCES azienda(id),
    PRIMARY KEY (azienda_id, tipo)
);

-- Create utente table
CREATE TABLE utente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(500) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- Create utente_ruoli table
CREATE TABLE utente_ruoli (
    utente_id BIGINT NOT NULL,
    ruolo VARCHAR(50) NOT NULL,
    FOREIGN KEY (utente_id) REFERENCES utente(id),
    PRIMARY KEY (utente_id, ruolo)
);

-- Create articolo_catalogo table (base table for inheritance)
CREATE TABLE articolo_catalogo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dtype VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descrizione TEXT,
    prezzo_unitario DOUBLE,
    quantita_disponibile DOUBLE,
    azienda_id BIGINT NOT NULL,
    stato VARCHAR(50),
    FOREIGN KEY (azienda_id) REFERENCES azienda(id)
);

-- Create prodotto table (extends articolo_catalogo)
CREATE TABLE prodotto (
    id BIGINT PRIMARY KEY,
    unita_misura VARCHAR(50),
    categoria_prodotto VARCHAR(50),
    tipo_prodotto VARCHAR(50) NOT NULL,
    metodo_coltivazione VARCHAR(50),
    metodo_trasformazione VARCHAR(255),
    FOREIGN KEY (id) REFERENCES articolo_catalogo(id)
);

-- Create prodotto_certificazioni table
CREATE TABLE prodotto_certificazioni (
    prodotto_id BIGINT NOT NULL,
    certificazione VARCHAR(255),
    FOREIGN KEY (prodotto_id) REFERENCES prodotto(id)
);

-- Create prodotto_ingredienti table
CREATE TABLE prodotto_ingredienti (
    prodotto_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    FOREIGN KEY (prodotto_id) REFERENCES prodotto(id),
    FOREIGN KEY (ingrediente_id) REFERENCES articolo_catalogo(id),
    PRIMARY KEY (prodotto_id, ingrediente_id)
);

-- Create azienda_magazzino table
CREATE TABLE azienda_magazzino (
    azienda_id BIGINT NOT NULL,
    prodotto_id BIGINT NOT NULL,
    quantita INT,
    FOREIGN KEY (azienda_id) REFERENCES azienda(id),
    FOREIGN KEY (prodotto_id) REFERENCES prodotto(id),
    PRIMARY KEY (azienda_id, prodotto_id)
);