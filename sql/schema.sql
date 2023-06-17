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

CREATE TABLE banlist (
    id BIGSERIAL NOT NULL,
    user_id INT8,
    action VARCHAR(5),
    motive VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens(
    id VARCHAR(36) NOT NULL,
    user_id INT8,
    issued_at TIMESTAMP,
    expires_at TIMESTAMP,
    active BOOLEAN,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS users ADD CONSTRAINT users_unique_email UNIQUE (email);

ALTER TABLE if EXISTS users ADD CONSTRAINT fk_users_countries_id FOREIGN KEY (country_id) REFERENCES countries;

ALTER TABLE if EXISTS banlist ADD CONSTRAINT fk_banlist_users_id FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE if EXISTS refresh_tokens ADD CONSTRAINT fk_refresh_tokens_users_id FOREIGN KEY (user_id) REFERENCES users;

-- FUNCTION TO CREATE AND UPDATE DATES
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS
'
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
'
LANGUAGE plpgsql;

--TRIGGERS TO UPDATE DATES ON TABLES
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON banlist
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();