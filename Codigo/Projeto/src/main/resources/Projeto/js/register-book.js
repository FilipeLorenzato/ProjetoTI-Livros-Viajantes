document
  .getElementById("book-form")
  .addEventListener("submit", function (event) {
    if (localStorage.getItem("userId")) {
      event.preventDefault(); // Prevenir o envio padrão do formulário

      const titulo = document.getElementById("book-name").value;
      const autor = document.getElementById("book-author").value;
      const genero = document.getElementById("book-genre").value;
      const sinopse = document.getElementById("book-synopsis").value;
      const imageFile = document.getElementById("book-image").files[0];
      const userId = localStorage.getItem("userId");

      // Validação de campos (opcional, caso precise de validação adicional)
      if (!titulo || !autor || !genero || !sinopse) {
        alert("Preencha todos os campos obrigatórios.");
        return;
      }

      if (!imageFile) {
        alert("Por favor, selecione uma imagem para o livro.");
        return;
      }

      const reader = new FileReader();
      reader.onload = async function (event) {
        const imageBase64 = event.target.result; // Obter a string Base64 da imagem

        // Criar objeto do livro com a imagem em Base64
        const livro = {
          titulo: titulo,
          autor: autor,
          genero: genero,
          sinopse: sinopse,
          imagem: imageBase64, // Base64 no formato completo (data:image/jpeg;base64,...)
          id_usuario: parseInt(userId),
        };

        // Enviar o objeto via POST
        try {
          const response = await fetch("http://localhost:4567/livro", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(livro),
          });

          if (!response.ok) {
            throw new Error(`Erro ao cadastrar livro: ${response.statusText}`);
          }

          alert("Livro cadastrado com sucesso!");
          document.getElementById("book-form").reset();
          window.location.href = "meus-livros.html"; // Página de redirecionamento
        } catch (error) {
          console.error(error);
        }
      };

      reader.readAsDataURL(imageFile); // Ler o arquivo como DataURL
    } else {
      alert("Você precisa estar logado para postar um livro.");
    }
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
