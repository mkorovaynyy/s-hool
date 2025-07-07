CREATE TABLE car (
                     id BIGSERIAL PRIMARY KEY,
                     brand VARCHAR(50) NOT NULL,
                     model VARCHAR(50) NOT NULL,
                     cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE person (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        age INTEGER NOT NULL,
                        has_driver_license BOOLEAN NOT NULL,
                        car_id BIGINT REFERENCES car(id)
);