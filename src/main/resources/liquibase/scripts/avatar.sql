-- changeset mk:1
CREATE TABLE IF NOT EXISTS avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT,
    media_type VARCHAR(255),
    data       BYTEA NOT NULL,
    student_id BIGINT UNIQUE REFERENCES student (id) ON DELETE CASCADE
    );

-- changeset mk:2
CREATE INDEX IF NOT EXISTS idx_avatar_student_id ON avatar (student_id);