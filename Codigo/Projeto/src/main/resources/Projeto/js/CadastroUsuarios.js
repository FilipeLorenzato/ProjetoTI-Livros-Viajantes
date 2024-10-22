document.addEventListener('DOMContentLoaded', () => {
    const btnCadastrarUsuario = document.getElementById('btnCadastrarUsuario');
    if (btnCadastrarUsuario) {
        btnCadastrarUsuario.addEventListener('click', cadastrarUsuario);
    } else {
        console.error('Botão de cadastrar usuário não encontrado.');
    }
});

function cadastrarUsuario(event) {
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
    fetch('http://localhost:4567/CadastroUsuarios', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(usuario),
    })
    console.log(response)
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.text(); // Mudar para .text() e tentar converter para JSON só se o corpo existir
    })
    .then(data => {
        try {
            const jsonData = JSON.parse(data);
            console.log('Usuário cadastrado com sucesso:', jsonData);
            alert('Cadastro realizado com sucesso!');
        } catch (error) {
            console.error('Erro ao processar o JSON:', error);
            alert('Erro ao processar a resposta do servidor.');
        }
    })
    .catch(error => {
        console.error('Erro ao cadastrar usuário:', error);
        alert('Ocorreu um erro ao cadastrar o usuário. Tente novamente.');
    });
}
