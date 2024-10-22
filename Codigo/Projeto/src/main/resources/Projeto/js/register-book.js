document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('book-form');

    form.addEventListener('submit', (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário

        // Captura os dados do formulário
        const bookName = document.getElementById('book-name').value.trim();
        const bookAuthor = document.getElementById('book-author').value.trim();
        const bookGenre = document.getElementById('book-genre').value;
        const bookSynopsis = document.getElementById('book-synopsis').value.trim();
        // Se você tiver mais campos, adicione aqui

        // Validação simples
        if (!bookName || !bookAuthor || !bookGenre || !bookSynopsis) {
            alert('Preencha todos os campos corretamente.');
            return;
        }

        // Cria um objeto com os dados do livro
        const bookData = {
            name: bookName,
            author: bookAuthor,
            genre: bookGenre,
            synopsis: bookSynopsis,
            // Adicione outros campos conforme necessário
        };

        // Envia os dados para o servidor
        fetch('http://localhost:4567/livro', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData) // Converte os dados do livro em JSON
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao cadastrar o livro: ' + response.statusText);
            }
            return response.json(); // Converte a resposta em JSON
        })
        .then(data => {
            // Verifica se o servidor retornou uma mensagem de sucesso
            alert(data.mensagem || 'Livro cadastrado com sucesso!');
            window.location.href = 'postagem-livros.html'; // Redireciona após o cadastro
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Ocorreu um erro ao cadastrar o livro. Tente novamente.');
        });
    });
});






/*document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('book-form');

    form.addEventListener('submit', (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário

        // Captura os dados do formulário
        const bookName = document.getElementById('book-name').value.trim();
        const bookAuthor = document.getElementById('book-author').value.trim();
        const bookGenre = document.getElementById('book-genre').value;
        const bookSynopsis = document.getElementById('book-synopsis').value.trim();
        /*const bookPages = parseInt(document.getElementById('book-pages').value);
        const bookImage = document.getElementById('book-image').files[0]; // Imagem do arquivo
        const bookImageUrl = document.getElementById('book-image-url').value.trim(); // URL da imagem

        // Validação simples
        if (!bookName || !bookAuthor || !bookGenre || !bookSynopsis) {
            alert('Preencha todos os campos corretamente e forneça uma imagem.');
            return;
        }

        // Verifica se a URL da imagem foi fornecida
        if (bookImageUrl) {
            const bookData = {
                name: bookName,
                genre: bookGenre,
                synopsis: bookSynopsis,
                pages: bookPages,
                author: bookAuthor,
                image: bookImageUrl // Usa a URL fornecida pelo usuário
            };

            saveBookData(bookData); // Salva os dados com a URL da imagem
        } else if (bookImage) {
            // Caso haja uma imagem local, converte para base64
            const reader = new FileReader();
            reader.onloadend = () => {
                const bookData = {
                    name: bookName,
                    genre: bookGenre,
                    synopsis: bookSynopsis,
                    pages: bookPages,
                    author: bookAuthor,
                    image: reader.result // Usa a imagem convertida para base64
                };

                saveBookData(bookData); // Salva os dados com a imagem em base64
            };
            reader.readAsDataURL(bookImage);
        }
    });

    // Função para salvar o livro no localStorage e redirecionar
    function saveBookData(bookData) {
        // Armazena no localStorage
        let books = JSON.parse(localStorage.getItem('books')) || [];
        books.push(bookData);
        localStorage.setItem('books', JSON.stringify(books));

        // Exibe mensagem de sucesso
        alert('Livro cadastrado com sucesso!');
        window.location.href = 'postagem-livros.html';
    }
})});*/