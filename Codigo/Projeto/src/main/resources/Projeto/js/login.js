function logarUsuario() {
    const email = document.getElementById('email').value.trim(); // Remove espaços em branco
    const senha = document.getElementById('senha').value.trim(); // Remove espaços em branco

    // Validação dos campos
    if (!email || !senha) {
        alert("Por favor, preencha todos os campos.");
        return; // Sai da função se algum campo estiver vazio
    }

    // Mostra os dados enviados para depuração
    console.log("Dados enviados:", { email, senha });

    fetch('http://localhost:4567/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, senha })
    })
        .then(response => {
            if (!response.ok) {
                // Se a resposta não for OK, trata o erro baseado no status
                return response.json().then(errData => {
                    // Lança um erro com a mensagem recebida do servidor
                    throw new Error(errData.mensagem || "Erro desconhecido ao fazer login.");
                });
            }
            return response.json(); // Converte a resposta em JSON se tudo estiver ok
        })
        .then(data => {
            console.log(data.mensagem); // Exibe a mensagem retornada pelo servidor
            alert(data.mensagem); // Exibe a mensagem para o usuário

            // Redireciona para a página inicial após o login bem-sucedido
            window.location.href = "/inicio"; // Ajuste o URL conforme necessário para sua aplicação
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Erro: " + error.message); // Exibe um alerta com a mensagem de erro
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