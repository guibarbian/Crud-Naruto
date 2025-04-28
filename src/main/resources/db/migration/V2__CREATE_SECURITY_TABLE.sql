CREATE TABLE usuarios (
id SERIAL PRIMARY KEY,
nome VARCHAR(60) NOT NULL UNIQUE,
senha VARCHAR(255) NOT NULL,
role VARCHAR(15) NOT NULL
);

INSERT INTO usuarios (nome, senha, role) VALUES ('admin', 'admin', 'ADMIN');
INSERT INTO usuarios (nome, senha, role) VALUES ('user', 'user', 'USER');