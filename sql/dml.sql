-- Countries
INSERT INTO country (name) VALUES ('USA'), ('UK'), ('Canada');

-- Cities
INSERT INTO city (name, country_id) VALUES 
('New York', 1),
('Los Angeles', 1),
('London', 2),
('Toronto', 3);

-- Sample customers
INSERT INTO customer (name, date_of_birth, nic) VALUES
('John Doe', '1990-05-15', '900000001V'),
('Jane Smith', '1985-08-22', '850000002V');

-- Sample customer addresses
INSERT INTO customer_address (customer_id, address_line_1, address_line_2, city_id, country_id) VALUES
(1, '123 Main Street', 'Apt 4B', 1, 1),
(2, '456 Park Ave', NULL, 3, 2);

-- Sample customer mobiles
INSERT INTO customer_mobile (customer_id, mobile_number) VALUES
(1, '+11234567890'),
(1, '+10987654321'),
(2, '+441234567890');

-- Sample customer families
INSERT INTO customer_family (customer_id, family_member_id, relationship) VALUES
(1, 2, 'Sister');
