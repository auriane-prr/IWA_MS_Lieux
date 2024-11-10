-- Création de la table locations
CREATE TABLE locations (
                           location_id SERIAL PRIMARY KEY,
                           user_id INTEGER NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           latitude NUMERIC(18, 16) NOT NULL,
                           longitude NUMERIC(18, 16) NOT NULL,
                           adresse VARCHAR(100),
                           ville VARCHAR(100),
                           code_postal VARCHAR(100),
                           description TEXT
);

-- Création de la table location_photos
CREATE TABLE location_photos (
                                 photo_id SERIAL PRIMARY KEY,
                                 location_id INTEGER NOT NULL REFERENCES locations(location_id),
                                 photo_url VARCHAR(100) NOT NULL
);

-- Création de la table equipments
CREATE TABLE equipments (
                            equipment_id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

-- Création de la table de jointure entre locations et equipments
CREATE TABLE location_equipments (
                                     location_id INTEGER NOT NULL REFERENCES locations(location_id),
                                     equipment_id INTEGER NOT NULL REFERENCES equipments(equipment_id),
                                     PRIMARY KEY (location_id, equipment_id)
);

-- Insertion de données d'exemple
INSERT INTO locations (user_id, name, latitude, longitude, adresse, ville, code_postal, description)
VALUES
    (1, 'Montpellier Bivouac', 43.6119, 3.8772, '123 Rue de la Paix', 'Montpellier', '34000', 'Un lieu de bivouac agréable à Montpellier');

INSERT INTO equipments (name) VALUES ('Wi-Fi'), ('Electricité'), ('Parking');

INSERT INTO location_equipments (location_id, equipment_id) VALUES (1, 1), (1, 2);
