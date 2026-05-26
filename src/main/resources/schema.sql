CREATE TABLE users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(50) NOT NULL
);

CREATE TABLE flea_market (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    price INT NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    image_path VARCHAR(255),
                    buyer_id BIGINT,
                    FOREIGN KEY (user_id) REFERENCES users(id)
);
