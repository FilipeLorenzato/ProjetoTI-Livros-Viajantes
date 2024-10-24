function cadastrarUsuario(event) {
    event.preventDefault(); // Intercepta o comportamento padrão

    // Obtendo os valores dos campos
    const nome = document.getElementById('nome').value.trim();
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const telefone = document.getElementById('telefone').value.trim();
    const rua = document.getElementById('rua').value.trim();
    const cidade = document.getElementById('cidade').value.trim();
    const estado = document.getElementById('estado').value.trim();
    const cep = document.getElementById('cep').value.trim();

    // Validação básica
    if (!nome || !email || !senha || !telefone || !rua || !cidade || !estado || !cep) {
        alert('Por favor, preencha todos os campos.');
        return;
    }

    // Criando o objeto do usuário
    const usuario = { nome, email, senha, telefone, rua, cidade, estado, cep };

    // Enviando os dados para o servidor
    fetch('http://localhost:4567/usuario', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(usuario),
    })
    .then(response => {
        console.log(response);
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Usuário cadastrado com sucesso:', data);
        alert('Cadastro realizado com sucesso!');
    })
    .catch(error => {
        console.error('Erro ao cadastrar usuário:', error);
        alert('Ocorreu um erro ao cadastrar o usuário. Tente novamente.');
    });
}




/*document.addEventListener('DOMContentLoaded', () => {
    const button = document.getElementById('cadastrarUsuario');
    if (!button) {
        console.error('Botão de cadastrar usuário não encontrado.');
        return;
    }

    button.addEventListener('click', () => {
        // Código para manipular o evento de click
        console.log('Botão clicado!');
    });
});


function cadastrarUsuario() {
    event.preventDefault();

    // Obtendo os valores dos campos
    const nome = document.getElementById('nome').value.trim();
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const telefone = document.getElementById('telefone').value.trim();
    const rua = document.getElementById('rua').value.trim();
    const cidade = document.getElementById('cidade').value.trim();
    const estado = document.getElementById('estado').value.trim();
    const cep = document.getElementById('cep').value.trim();

    // Validação básica
    if (!nome || !email || !senha || !telefone || !rua || !cidade || !estado || !cep) {
        alert('Por favor, preencha todos os campos.');
        return;
    }

    // Criando o objeto do usuário
    const usuario = {
        nome,
        email,
        senha,
        telefone,
        rua,
        cidade,
        estado,
        cep
    }

    // Enviando os dados para o servidor
    fetch('http://localhost:4567/usuarios', { // '/usuarios'
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(usuario),
    })
    .then(response => {
        console.log(response); // Aqui está o log correto
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json(); // Converte a resposta para JSON
    })
    .then(data => {
        console.log('Usuário cadastrado com sucesso:', data);
        alert('Cadastro realizado com sucesso!');
    })
    .catch(error => {
        console.error('Erro ao cadastrar usuário:', error);
        alert('Ocorreu um erro ao cadastrar o usuário. Tente novamente.');
    });
}*/
