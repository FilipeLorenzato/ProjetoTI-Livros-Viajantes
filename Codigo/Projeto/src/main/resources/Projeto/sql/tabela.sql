-- Tabela Usuario
CREATE TABLE Usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(15),
    rua VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10)
);

-- Tabela Livro
CREATE TABLE Livro (
    id_livro SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    genero VARCHAR(50),
    sinopse TEXT
);

-- Tabela Troca
CREATE TABLE Troca (
    id_troca SERIAL PRIMARY KEY,
    id_livro INT NOT NULL,
    usuario_ofertante INT NOT NULL,
    usuario_contemplado INT,
    status VARCHAR(20) NOT NULL, -- Ex: "pendente", "concluída", "cancelada"
    FOREIGN KEY (id_livro) REFERENCES Livro (id_livro),
    FOREIGN KEY (usuario_ofertante) REFERENCES Usuario (id_usuario),
    FOREIGN KEY (usuario_contemplado) REFERENCES Usuario (id_usuario)
);

-- Comandos do banco SGDB em SQL
/*
deletar com uma tabela dependente:
DELETE FROM usuario WHERE id_usuario = id;

reiniciar a contagem do id:
TRUNCATE TABLE usuario RESTART IDENTITY;

Reiniciar a sequência manualmente:
ALTER SEQUENCE usuario_id_usuario_seq RESTART WITH 1;

*/