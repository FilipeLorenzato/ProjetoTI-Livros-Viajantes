
document.getElementById("book-form").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevenir o envio padrão do formulário

    const titulo = document.getElementById("book-name").value;
    const autor = document.getElementById("book-author").value;
    const genero = document.getElementById("book-genre").value;
    const sinopse = document.getElementById("book-synopsis").value;
    const imageFile = document.getElementById("book-image").files[0];

    // Validação de campos (opcional, caso precise de validação adicional)
    if (!titulo || !autor || !genero || !sinopse) {
        alert("Preencha todos os campos obrigatórios.");
        return;
    }

    if (!imageFile) {
        alert("Por favor, selecione uma imagem para o livro.");
        return;
    }

    // Criação do FormData para enviar a imagem e os dados do livro
    const formData = new FormData();
    formData.append("titulo", titulo);
    formData.append("autor", autor);
    formData.append("genero", genero);
    formData.append("sinopse", sinopse);
    formData.append("book-image", imageFile);

    // Envio da requisição POST para cadastrar o livro com imagem
    fetch("http://localhost:4567/livro", {
        method: "POST",
        body: formData // Envia o FormData com o arquivo e dados do livro
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao cadastrar o livro: " + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            alert("Livro cadastrado com sucesso! ID: " + data);
            window.location.href = "pagina-de-postagem.html"; // Altere para o nome correto da sua página
            // Limpar o formulário após o cadastro
            document.getElementById("book-form").reset();
        })
        .catch(error => {
            console.error(error);
            alert("Erro: " + error.message);
        });
});


/*document.getElementById("book-form").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevenir o envio padrão do formulário

    const titulo = document.getElementById("book-name").value;
    const autor = document.getElementById("book-author").value;
    const genero = document.getElementById("book-genre").value;
    const sinopse = document.getElementById("book-synopsis").value;

    // Validação de campos (opcional, caso precise de validação adicional)
    if (!titulo || !autor || !genero || !sinopse) {
        alert("Preencha todos os campos obrigatórios.");
        return;
    }

    // Criação do JSON com os dados do livro
    const livro = {
        titulo: titulo,
        autor: autor,
        genero: genero,
        sinopse: sinopse
    };

    // Envio da requisição POST para cadastrar o livro
    fetch("http://localhost:4567/livro", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(livro)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao cadastrar o livro: " + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            alert("Livro cadastrado com sucesso! ID: " + data);
            // Limpar o formulário após o cadastro
            document.getElementById("book-form").reset();
        })
        .catch(error => {
            console.error(error);
            alert("Erro: " + error.message);
        });
});*/
