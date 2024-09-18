document.addEventListener('DOMContentLoaded', () => {
    const btnLogin = document.getElementById('btnLogin');
    btnLogin.addEventListener('click', logarUsuario);
});

function logarUsuario(event) {
    event.preventDefault();

    const email = document.getElementById('emailLogin').value.trim();
    const senha = document.getElementById('senhaLogin').value;

    if (!email || !senha) {
        alert('Por favor, preencha todos os campos.');
        return;
    }

    // Verifica os dados no backend
    fetch('http://localhost:3001/usuarios', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, senha }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.sucesso) {
                // Salva as informações no localStorage
                localStorage.setItem('usuarioLogado', JSON.stringify(data.usuario));
                alert('Login realizado com sucesso!');
                // Redirecionar para a página principal
                window.location.href = 'pagina-principal.html';
            } else {
                alert('E-mail ou senha incorretos.');
            }
        })
        .catch(error => {
            console.error('Erro ao fazer login:', error);
            alert('Ocorreu um erro ao fazer login. Tente novamente.');
        });
}



/*
TESTE DE LOGIN
document.addEventListener("DOMContentLoaded", function () {
    var loginButton = document.getElementById("login");

    loginButton.onclick = (e) => {
        e.preventDefault();

        const auth = {}
        for (var item of e.target.form) {
            if (item.name != '') auth[item.name] = item.value;
        }

        login(auth.email, auth.password);
    }
});*/