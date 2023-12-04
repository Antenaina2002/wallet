-- Drop the database if it exists
DROP DATABASE IF EXISTS account_management;

-- Create a new database
CREATE DATABASE account_management;

-- Connect to the newly created database
\c account_management

-- Creating the 'currency' table with 'name' as primary key
CREATE TABLE IF NOT EXISTS currency (
    name VARCHAR(50) PRIMARY KEY
);

-- Inserting the 10 existing currencies
INSERT INTO currency (name) VALUES
    ('Euro'),
    ('US Dollar'),
    ('Japanese Yen'),
    ('British Pound'),
    ('Swiss Franc'),
    ('Canadian Dollar'),
    ('Australian Dollar'),
    ('Mexican Peso'),
    ('Indian Rupee'),
    ('Ariary')
ON CONFLICT (name) DO NOTHING;

-- Creating the 'account' table
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    RIB varchar(50) UNIQUE NOT NULL,
    portfolio INT -- Amount of money in the account
);

-- Creating the 'transaction' table with 'RIB' for transaction identification
CREATE TABLE IF NOT EXISTS transaction (
    id_transaction SERIAL PRIMARY KEY,
    account_sending VARCHAR(50) REFERENCES account(RIB),
    account_recieving VARCHAR(50) REFERENCES account(RIB),
    transaction_description TEXT,
    debit DECIMAL(15, 2) NOT NULL, -- Using DECIMAL for amounts
    currency_used VARCHAR(50) REFERENCES currency(name),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);