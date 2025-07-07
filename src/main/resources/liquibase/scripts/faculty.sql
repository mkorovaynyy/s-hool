-- liquibase formatted sql
-- changeset mk:1
CREATE TABLE IF NOT EXISTS faculty
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    CONSTRAINT unique_faculty_name_color UNIQUE (name, color)
    );
-- changeset mk:2
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);