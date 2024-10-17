-- Tabela Usuario
CREATE TABLE Usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(15),
    cidade VARCHAR(100),
    rua VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    numero VARCHAR(10)
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
    status VARCHAR(20) NOT NULL,  -- Ex: "pendente", "concluída", "cancelada"
    
    FOREIGN KEY (id_livro) REFERENCES Livro(id_livro),
    FOREIGN KEY (usuario_ofertante) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (usuario_contemplado) REFERENCES Usuario(id_usuario)
);


/*-- Tabela Usuario
CREATE TABLE Usuario (
  id_usuario SERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(30) NOT NULL
);

-- Tabela Categoria
CREATE TABLE Categoria (
  id_categoria SERIAL PRIMARY KEY,
  nome_categoria VARCHAR(50) NOT NULL
);

-- Tabela Livro
CREATE TABLE Livro (
  id_livro SERIAL PRIMARY KEY,
  titulo_livro VARCHAR(50) NOT NULL,
  autor_livro VARCHAR(30),
  ano_publicacao INT,
  id_usuario INT,
  id_categoria INT,
  CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
  CONSTRAINT fk_categoria FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

-- Tabela Oferta
CREATE TABLE Oferta (
  id_oferta SERIAL PRIMARY KEY,
  id_livro_oferecido INT,
  id_livro_desejado INT,
  id_usuario_oferecendo INT,
  id_usuario_recebendo INT,
  data_oferta DATE DEFAULT CURRENT_DATE,
  status_oferta VARCHAR(10) NOT NULL,
  CONSTRAINT chk_status_oferta CHECK (status_oferta IN ('Pendente', 'Trocado', 'Cancelado')),
  CONSTRAINT fk_livro_oferecido FOREIGN KEY (id_livro_oferecido) REFERENCES Livro(id_livro),
  CONSTRAINT fk_livro_desejado FOREIGN KEY (id_livro_desejado) REFERENCES Livro(id_livro),
  CONSTRAINT fk_usuario_oferecendo FOREIGN KEY (id_usuario_oferecendo) REFERENCES Usuario(id_usuario),
  CONSTRAINT fk_usuario_recebendo FOREIGN KEY (id_usuario_recebendo) REFERENCES Usuario(id_usuario)
);*/