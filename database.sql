DROP DATABASE IF EXISTS hotel_requests;
CREATE DATABASE hotel_requests CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_requests;

CREATE TABLE room (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      room_number VARCHAR(20) NOT NULL,
                      category VARCHAR(100) NOT NULL,
                      floor INT NOT NULL
);

CREATE TABLE specialist (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            full_name VARCHAR(150) NOT NULL,
                            specialization VARCHAR(100) NOT NULL,
                            phone VARCHAR(30) NOT NULL
);

CREATE TABLE request (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         room_id BIGINT NOT NULL,
                         specialist_id BIGINT,
                         description TEXT NOT NULL,
                         state VARCHAR(30) NOT NULL,
                         f1 DOUBLE NOT NULL,
                         f2 DOUBLE NOT NULL,
                         f3 DOUBLE NOT NULL,
                         priority_index DOUBLE NOT NULL,
                         FOREIGN KEY (room_id) REFERENCES room(id),
                         FOREIGN KEY (specialist_id) REFERENCES specialist(id)
);

CREATE TABLE execution_record (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  request_id BIGINT NOT NULL,
                                  report_details TEXT NOT NULL,
                                  completed_date VARCHAR(20) NOT NULL,
                                  FOREIGN KEY (request_id) REFERENCES request(id)
);

INSERT INTO room (room_number, category, floor) VALUES
                                                    ('101', 'Стандарт', 1),
                                                    ('205', 'Люкс', 2);

INSERT INTO specialist (full_name, specialization, phone) VALUES
                                                              ('Іван Петренко', 'Сантехніка', '+380501112233'),
                                                              ('Олег Сидоренко', 'Електрика', '+380673334455');

INSERT INTO request (room_id, specialist_id, description, state, f1, f2, f3, priority_index) VALUES
                                                                                                 (1, 1, 'Протікає кран у ванній кімнаті', 'НОВА', 70.0, 60.0, 50.0, 61.5),
                                                                                                 (2, 2, 'Не працює кондиціонер', 'ПРИЗНАЧЕНА', 80.0, 90.0, 70.0, 80.5);

INSERT INTO execution_record (request_id, report_details, completed_date) VALUES
    (1, 'очікує виконання', '2026-10-19');