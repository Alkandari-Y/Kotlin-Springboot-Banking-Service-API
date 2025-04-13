
DROP TABLE IF EXISTS users;
CREATE TABLE "users" (
    "id" SERIAL PRIMARY KEY,
    "username" VARCHAR(200) UNIQUE NOT NULL,
    "password" VARCHAR(200) NOT NULL
);

DROP TABLE IF EXISTS kycs;
CREATE TABLE "kycs" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INT REFERENCES users(id) NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "date_of_birth" DATE NOT NULL,
    "nationality" VARCHAR(255) NOT NULL,
    "salary" DECIMAL(9, 3) NOT NULL
);

DROP TABLE IF EXISTS accounts;
CREATE TABLE "accounts" (
    "id" SERIAL PRIMARY KEY,
    "user_id" INT REFERENCES users(id) NOT NULL,
    "balance" DECIMAL(9, 3) NOT NULL,
    "is_active" BOOLEAN DEFAULT TRUE,
    "account_number" VARCHAR(255) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS transactions;
CREATE TABLE "transactions" (
    "id" SERIAL PRIMARY KEY,
    "source_account" INT REFERENCES accounts(id),
    "destination_account" INT REFERENCES accounts(id),
    "amount" DECIMAL(9, 3)
)