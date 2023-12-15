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
    devise VARCHAR(50) REFERENCES devise(nom),
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

-- Table pour les catégories de transactions
CREATE TABLE IF NOT EXISTS catégorie_transaction (
    id SERIAL PRIMARY KEY,
    référence SERIAL UNIQUE,
    catégorie VARCHAR(50) UNIQUE,
    type_transaction VARCHAR(10) CHECK (type_transaction IN ('Débit', 'Crédit'))
);

-- Insertion des catégories de transactions
INSERT INTO catégorie_transaction (référence, catégorie, type_transaction) VALUES
    (DEFAULT, 'alimentation', 'Débit'),
    (DEFAULT, 'transport', 'Débit'),
    (DEFAULT, 'divertissement', 'Débit'),
    (DEFAULT, 'salaire', 'Crédit'),
    (DEFAULT, 'transfert_entrant', 'Crédit'),
    (DEFAULT, 'pret_debit', 'Débit'),
    (DEFAULT, 'pret_credit', 'Crédit')
ON CONFLICT (catégorie) DO NOTHING;

-- Fonction pour insérer une nouvelle transaction
CREATE OR REPLACE FUNCTION fonction_transaction(catégorie VARCHAR, montant DECIMAL)
RETURNS VOID AS $$
DECLARE
    référence_transaction VARCHAR(8);
BEGIN
    SELECT référence INTO référence_transaction
    FROM catégorie_transaction
    WHERE catégorie_transaction.catégorie = catégorie;

    IF référence_transaction IS NOT NULL THEN
        INSERT INTO transaction (label, montant, date_transaction, type_transaction, compte_id)
        VALUES (catégorie, montant, CURRENT_TIMESTAMP, (SELECT type_transaction FROM catégorie_transaction WHERE catégorie = catégorie LIMIT 1), 1); -- Modifier le compte_id selon vos besoins
    ELSE
        RAISE EXCEPTION 'Catégorie de transaction non trouvée.';
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Table pour l'historique des transactions
CREATE TABLE IF NOT EXISTS historique_transaction (
    id SERIAL PRIMARY KEY,
    transaction_id INT REFERENCES transaction(id),
    ancien_solde DECIMAL(15, 2),
    nouveau_solde DECIMAL(15, 2),
    date_historique TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION somme_entrees_sorties(
    compte_id INT,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP
)
RETURNS DECIMAL AS $$
DECLARE
    total DECIMAL := 0;
BEGIN
    SELECT COALESCE(SUM(CASE WHEN type_transaction = 'Crédit' THEN montant ELSE 0 END), 0) -
           COALESCE(SUM(CASE WHEN type_transaction = 'Débit' THEN montant ELSE 0 END), 0)
    INTO total
    FROM transaction
    WHERE compte_id = $1
    AND date_transaction BETWEEN $2 AND $3;

    RETURN total;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION somme_montants_par_categorie(
    compte_id INT,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP
)
RETURNS TABLE (
    catégorie VARCHAR(50),
    somme_montant DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT ct.catégorie,
           COALESCE(SUM(CASE WHEN t.montant IS NOT NULL THEN t.montant ELSE 0 END), 0) AS somme_montant
    FROM catégorie_transaction ct
    LEFT JOIN transaction t ON ct.catégorie = t.label
                            AND t.compte_id = compte_id
                            AND t.date_transaction BETWEEN date_debut AND date_fin
    GROUP BY ct.catégorie;
END;
$$ LANGUAGE plpgsql;
