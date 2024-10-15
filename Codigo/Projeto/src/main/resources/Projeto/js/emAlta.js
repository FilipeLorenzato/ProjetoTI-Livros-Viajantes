const apiKeyNYT = 'kWAJ52Y0zbACzaOqfLGyCayRqEq9QnOa';  // Chave da API do NYT
const apiKeyGoogleBooks = 'AIzaSyAGsmI-fG7fZ9G5At3hcvaunFzdAiTGhLQ';  // Chave da API do Google Books (não utilizada no momento)
const booksContainer = document.getElementById('books-container');
const modal = document.getElementById('modal');
const modalContent = document.getElementById('modal-content');
const genreFilter = document.getElementById('genre-filter');
const myList = [];  // Lista para armazenar os livros favoritos
const maxImageWidth = 200;  // Largura máxima da imagem
const maxImageHeight = 300;  // Altura máxima da imagem

// Função para buscar livros da API do NYT
async function searchBooksNYT() {
    try {
        const response = await fetch(`https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=${apiKeyNYT}`);
        const data = await response.json();
        const books = data.results.books;

        // Limpar o container de livros antes de adicionar novos livros
        booksContainer.innerHTML = '';

        // Adicionar livros ao DOM
        books.forEach(async (book) => {
            const bookElement = document.createElement('div');
            bookElement.classList.add('book'); // Garante que a classe "book" seja aplicada
            const genre = book.genre ? book.genre.toLowerCase() : 'unknown'; // Supondo que o gênero esteja disponível

            const thumbnail = book.book_image || '';
            const title = book.title || 'Título não disponível';
            const description = book.description || 'Sinopse não disponível';
            const authors = book.author || 'Autor desconhecido';

            // Limitar o comprimento do título e remover quebras de linha
            const maxLength = 50; // Número máximo de caracteres
            const formattedTitle = title.length > maxLength ? title.slice(0, maxLength) + '...' : title;

            // Remove quebras de linha e espaços em branco
            const cleanedTitle = formattedTitle.replace(/\s+/g, ' ').trim();

            // Adiciona um espaço reservado para a imagem do livro
            const imageUrl = await getResizedImage(thumbnail);

            bookElement.innerHTML = `
                <img src="${imageUrl}" alt="${title}" class="book-image">
                <h3>${cleanedTitle}</h3>
                <p class="book-author">${authors}</p>
                <p class="book-description">${description}</p>
                <button class="add-to-list-btn">Adicionar à Lista</button>
            `;

            // Evento para abrir o modal com informações detalhadas do livro
            bookElement.querySelector('img').addEventListener('click', () => openModal(title, description, imageUrl, authors));

            // Evento para adicionar o livro à lista
            bookElement.querySelector('.add-to-list-btn').addEventListener('click', () => addToMyList(title, description, imageUrl, authors));

            // Adiciona o livro ao container
            booksContainer.appendChild(bookElement);
        });
    } catch (error) {
        console.error('Erro ao buscar livros:', error);
        booksContainer.innerHTML = '<p class="error-message">Não foi possível carregar os livros no momento. Tente novamente mais tarde.</p>';
    }
}

// Função para obter uma imagem redimensionada
async function getResizedImage(url) {
    if (!url) return '';

    try {
        const response = await fetch(url);
        const blob = await response.blob();
        const img = document.createElement('img');
        img.src = URL.createObjectURL(blob);

        return new Promise((resolve) => {
            img.onload = () => {
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');
                const scale = Math.min(maxImageWidth / img.width, maxImageHeight / img.height);

                canvas.width = img.width * scale;
                canvas.height = img.height * scale;
                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

                const resizedImageUrl = canvas.toDataURL();
                resolve(resizedImageUrl);
            };
        });
    } catch (error) {
        console.error('Erro ao redimensionar imagem:', error);
        return url; // Retorna a URL original se houver erro
    }
}

// Função para abrir o modal
function openModal(title, description, imageUrl, authors) {
    modalContent.innerHTML = `
        <h2 class="book-details-title">${title}</h2>
        <img src="${imageUrl}" alt="${title}">
        <p><strong>Autor:</strong> ${authors}</p>
        <p><strong>Sinopse:</strong> ${description}</p>
        <button class="close-btn">Fechar</button>
        <a href="historico.html" class="trade-btn">Quero Trocar</a>
    `;
    modal.style.display = 'block';

    // Evento para fechar o modal
    modal.querySelector('.close-btn').addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Adiciona um evento ao botão "Quero Trocar"
    modal.querySelector('.trade-btn').addEventListener('click', (event) => {
        event.preventDefault(); // Previne o comportamento padrão do link
        window.location.href = "historico.html"; // Redireciona para a página desejada
    });
}

// Função para adicionar o livro à lista
function addToMyList(title, description, imageUrl, authors) {
    const book = { title, description, imageUrl, authors };
    if (!myList.some(b => b.title === book.title)) {
        myList.push(book);
        alert('Livro adicionado à lista!');
    } else {
        alert('Livro já está na lista.');
    }
}

// Função para filtrar livros por gênero
function filterBooksByGenre(genre) {
    const books = Array.from(booksContainer.getElementsByClassName('book'));
    books.forEach(book => {
        if (genre === 'all' || book.innerText.toLowerCase().includes(genre)) {
            book.style.display = 'flex';
        } else {
            book.style.display = 'none';
        }
    });
}

// Inicializa a busca e adiciona o evento de filtragem
document.addEventListener('DOMContentLoaded', () => {
    searchBooksNYT();
    genreFilter.addEventListener('change', (event) => {
        filterBooksByGenre(event.target.value.toLowerCase());
    });
});
