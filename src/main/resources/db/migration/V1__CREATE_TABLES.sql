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

insert into personagens(id, nome, idade, aldeia, chakra, especialidade) values
(1, 'Naruto Uzumaki', 17, 'Konoha', 150, 'NinjaDeNinjutsu'),
(2, 'Sasuke Uchiha', 17, 'Konoha', 80, 'NinjaDeNinjutsu'),
(3, 'Rock Lee', 17, 'Konoha', 60, 'NinjaDeTaijutsu'),
(4, 'Itachi Uchiha', 23, 'Akatsuki', 100, 'NinjaDeGenjutsu');

insert into personagem_jutsus(personagem_id, jutsu) values
(1, 'Rasengan'),
(1, 'Kage Bunshin no Jutsu'),
(2, 'Chidori'),
(2, 'Sharingan'),
(3, 'Oito Portões'),
(4, 'Tsukuyomi'),
(4, 'Amaterasu');
