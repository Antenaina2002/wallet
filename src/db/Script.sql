-- Creating the 'currency' table with 'name' as primary key
CREATE TABLE IF NOT EXISTS currency (
    name VARCHAR(50) PRIMARY KEY
);

-- Inserting the 10 existing currencies (including Ariary) with specific names
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

-- Creating the 'transaction' table with 'RIB' for transaction identification
CREATE TABLE IF NOT EXISTS transaction (
    RIB VARCHAR(50) PRIMARY KEY,
    debit DECIMAL(15, 2) -- Using DECIMAL for amounts (can vary based on your requirements)
);

-- Creating the 'account' table with 'user_name' column instead of 'name'
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(100), -- Changing 'name' to 'user_name'
    currency_name VARCHAR(50) REFERENCES currency(name),
    transaction_RIB VARCHAR(50) REFERENCES transaction(RIB),
    portfolio INT -- Amount of money in the account
);
