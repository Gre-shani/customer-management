-- DROP TABLES IF EXISTS (for fresh setup)
DROP TABLE IF EXISTS customer_family;
DROP TABLE IF EXISTS customer_mobile;
DROP TABLE IF EXISTS customer_address;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS country;

-- Countries
CREATE TABLE country (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Cities
CREATE TABLE city (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country_id BIGINT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Customers
CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    nic VARCHAR(20) NOT NULL UNIQUE
);

-- Customer addresses
CREATE TABLE customer_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Customer mobile numbers
CREATE TABLE customer_mobile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    mobile_number VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

-- Customer family members
CREATE TABLE customer_family (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    family_member_id BIGINT NOT NULL,
    relationship VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    FOREIGN KEY (family_member_id) REFERENCES customer(id) ON DELETE CASCADE
);
