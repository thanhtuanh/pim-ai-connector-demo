-- Schema erstellen, falls nicht vorhanden
CREATE SCHEMA IF NOT EXISTS auth_service;

-- In das Schema wechseln
SET search_path TO auth_service;

-- Tabellen löschen (in der richtigen Reihenfolge mit CASCADE)
DROP TABLE IF EXISTS refresh_tokens CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Benutzer-Tabelle erstellen
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Refresh-Token-Tabelle erstellen
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Admin-Benutzer einfügen
-- Passwort ist 'admin' (BCrypt-Hash)
INSERT INTO users (username, password, role, enabled)
VALUES 
('admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'ROLE_ADMIN', true)
ON CONFLICT (username) DO NOTHING;