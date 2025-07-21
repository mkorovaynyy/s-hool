-- liquibase formatted sql
-- changeset mk:1
CREATE TABLE IF NOT EXISTS student
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    age        INTEGER      NOT NULL DEFAULT 20 CHECK (age >= 16),
    faculty_id BIGINT       REFERENCES faculty (id) ON DELETE SET NULL
    );
-- changeset mk:2
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);