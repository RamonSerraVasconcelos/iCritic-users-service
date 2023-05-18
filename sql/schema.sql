CREATE TABLE countries (
    id BIGSERIAL NOT NULL, 
    name VARCHAR(255), 
    PRIMARY KEY (id)
);

CREATE TABLE users (
    id BIGSERIAL NOT NULL, 
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL, 
    password VARCHAR(255) NOT NULL, 
    description VARCHAR(255), 
    country_id INT8,
    role VARCHAR(255),
    active BOOLEAN, 
    email_reset_date TIMESTAMP, 
    email_reset_hash VARCHAR(255),  
    new_email_reset VARCHAR(255), 
    password_reset_date TIMESTAMP, 
    password_reset_hash VARCHAR(255), 
    created_at TIMESTAMP, 
    updated_at TIMESTAMP, 
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS users ADD CONSTRAINT UK_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

ALTER TABLE if EXISTS users ADD CONSTRAINT FKjlpks00ofkq3sqd9hqiavv5lg FOREIGN KEY (country_id) REFERENCES countries;