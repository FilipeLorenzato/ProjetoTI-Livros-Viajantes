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

-- Tabela Livro com id_usuario como chave estrangeira
CREATE TABLE Livro (
    id_livro SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    genero VARCHAR(50),
    sinopse TEXT,
    imagem TEXT, -- Coluna para armazenar dados binários, como imagens
    id_usuario INT, -- Nova coluna para associar o livro ao usuário
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);

-- Tabela Troca
CREATE TABLE Troca (
    id_troca SERIAL PRIMARY KEY,
    id_livro_enviado INT NOT NULL,
    id_livro_recebido INT NOT NULL,
    usuario_ofertante INT NOT NULL,
    usuario_contemplado INT,
    status VARCHAR(20) NOT NULL, -- Ex: "pendente", "concluída", "cancelada"
    FOREIGN KEY (id_livro_enviado) REFERENCES Livro (id_livro),
    FOREIGN KEY (id_livro_recebido) REFERENCES Livro (id_livro),
    FOREIGN KEY (usuario_ofertante) REFERENCES Usuario (id_usuario),
    FOREIGN KEY (usuario_contemplado) REFERENCES Usuario (id_usuario)
);
