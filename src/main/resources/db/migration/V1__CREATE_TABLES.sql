CREATE TABLE personagens (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    idade INT NOT NULL,
    aldeia VARCHAR(90) NOT NULL,
    chakra INT NOT NULL,
    especialidade VARCHAR(20) NOT NULL
);

CREATE TABLE personagem_jutsus (
    personagem_id BIGINT NOT NULL,
    jutsu VARCHAR(255) NOT NULL,
    PRIMARY KEY (personagem_id, jutsu),
    FOREIGN KEY (personagem_id) REFERENCES personagens(id)
);