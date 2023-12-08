-- Suppression de la base de données existante si elle existe
DROP DATABASE IF EXISTS gestion_portefeuille;

-- Création d'une nouvelle base de données
CREATE DATABASE gestion_portefeuille;

-- Connexion à la nouvelle base de données
\c gestion_portefeuille

-- Table pour les types de devise (par exemple : Euro, Ariary)
CREATE TABLE IF NOT EXISTS devise (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE,
    code VARCHAR(3) UNIQUE
);

-- Insertion des devises Euro et Ariary
INSERT INTO devise (nom, code) VALUES
    ('Euro', 'EUR'),
    ('Ariary', 'MGA')
ON CONFLICT (nom) DO NOTHING;

-- Table pour les comptes
CREATE TABLE IF NOT EXISTS compte (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    solde_montant DECIMAL(15, 2),
    solde_date_maj TIMESTAMP,
    devise INT REFERENCES devise(nom),
    type VARCHAR(20) CHECK (type IN ('Banque', 'Espèce', 'Mobile Money'))
);

-- Table pour les transactions
CREATE TABLE IF NOT EXISTS transaction (
    id SERIAL PRIMARY KEY,
    label VARCHAR(50),
    montant DECIMAL(15, 2),
    date_transaction TIMESTAMP,
    type_transaction VARCHAR(10) CHECK (type_transaction IN ('Débit', 'Crédit')),
    compte_id INT REFERENCES compte(id)
);
