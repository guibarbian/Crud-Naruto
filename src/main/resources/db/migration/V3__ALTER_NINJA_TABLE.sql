ALTER TABLE personagens
    ADD COLUMN vida INTEGER NOT NULL DEFAULT 100;

UPDATE personagens
SET chakra = 100;

ALTER TABLE personagens
    ALTER COLUMN chakra SET DEFAULT 100;

ALTER TABLE personagem_jutsus
    ADD COLUMN dano INTEGER NOT NULL;


