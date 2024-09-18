const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 3001;

// Middleware para permitir o uso de JSON no body
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Caminho para o arquivo JSON que armazenará os usuários
const filePath = path.join(__dirname, 'usuarios.json');

// Função para carregar os usuários do arquivo JSON
const carregarUsuarios = () => {
    if (fs.existsSync(filePath)) {
        const data = fs.readFileSync(filePath);
        return JSON.parse(data);
    }
    return [];
};

// Função para salvar os usuários no arquivo JSON
const salvarUsuarios = (usuarios) => {
    fs.writeFileSync(filePath, JSON.stringify(usuarios, null, 4));
};

// Rota para cadastro de usuário
app.post('/cadastrar', (req, res) => {
    const { nome, email, senha, dataNascimento, telefone, endereco } = req.body;

    // Carregar usuários existentes
    const usuarios = carregarUsuarios();

    // Adicionar o novo usuário
    const novoUsuario = { nome, email, senha, dataNascimento, telefone, endereco };
    usuarios.push(novoUsuario);

    // Salvar o novo usuário no arquivo JSON
    salvarUsuarios(usuarios);

    res.json({ message: 'Usuário cadastrado com sucesso!' });
});

// Servir arquivos estáticos (HTML, CSS, JS)
app.use(express.static('public'));

app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`);
});
