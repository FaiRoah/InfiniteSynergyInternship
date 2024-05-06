CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        balance DECIMAL(10,2) DEFAULT 100
);

CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
                        user_id INTEGER NOT NULL,
                        role_id INTEGER NOT NULL,
                        PRIMARY KEY (user_id, role_id),
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (role_id) REFERENCES roles(id)
);


