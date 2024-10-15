// Lista de livros
const books = [
    { title: "Orgulho e Preconceito", author: "Jane Austen", image: "https://m.media-amazon.com/images/I/719esIW3D7L._SY466_.jpg", genre: "romance", description: "Uma clássica história de amor e preconceito na Inglaterra do século XIX." },
    { title: "Dom Quixote", author: "Miguel de Cervantes", image: "https://m.media-amazon.com/images/I/51542n0o9dL._SY445_SX342_.jpg", genre: "fiction", description: "As aventuras e desventuras de Dom Quixote, um cavaleiro andante idealista." },
    { title: "1984", author: "George Orwell", image: "https://m.media-amazon.com/images/I/819js3EQwbL._SY466_.jpg", genre: "fiction", description: "Uma distopia sobre um futuro totalitário e vigilância constante." },
    { title: "Cem Anos de Solidão", author: "Gabriel García Márquez", image: "https://m.media-amazon.com/images/I/515cVYLIP9L._SY445_SX342_.jpg", genre: "fantasy", description: "A saga da família Buendía na fictícia cidade de Macondo." },
    { title: "Harry Potter e a Pedra Filosofal", author: "J.K. Rowling", image: "https://m.media-amazon.com/images/I/41897yAI4LL._SY445_SX342_.jpg", genre: "fantasy", description: "A primeira aventura de Harry Potter no mundo da magia." }
];

// Função para gerar a lista de livros
const bookList = document.getElementById("book-list");

books.forEach(book => {
    const bookItem = document.createElement("div");
    bookItem.classList.add("book");

    bookItem.innerHTML = `
        <img src="${book.image}" alt="${book.title}" class="book-image">
        <h3>${book.title}</h3>
        <p class="book-author">por ${book.author}</p>
        <p class="book-description">${book.description}</p>
        <button class="add-to-list-btn">Adicionar à Lista</button>
    `;

    // Evento para abrir o modal com informações detalhadas do livro
    bookItem.querySelector('img').addEventListener('click', () => openModal(book));

    // Evento para adicionar o livro à lista
    bookItem.querySelector('.add-to-list-btn').addEventListener('click', () => addToMyList(book));

    // Adiciona o livro ao container
    bookList.appendChild(bookItem);
});

// Função para abrir o modal
function openModal(book) {
    const modal = document.getElementById('modal');
    const modalContent = document.querySelector('.modal-content');

    modalContent.innerHTML = `
        <button class="close-btn">&times;</button>
        <h2 class="book-details-title">${book.title}</h2>
        <img src="${book.image}" alt="${book.title}" class="modal-book-image">
        <p><strong>Autor:</strong> ${book.author}</p>
        <p><strong>Sinopse:</strong> ${book.description}</p>
        <a href="historico.html" class="trade-btn">Quero Trocar</a>
    `;

    modal.style.display = 'flex';

    // Evento para fechar o modal
    modalContent.querySelector('.close-btn').addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Evento para redirecionar ao clicar em "Quero Trocar"
    modalContent.querySelector('.trade-btn').addEventListener('click', (event) => {
        event.preventDefault(); // Previne o comportamento padrão do link
        window.location.href = "historico.html"; // Redireciona para a página desejada
    });
}

// Função para adicionar o livro à lista (Favoritos)
function addToMyList(book) {
    const myList = JSON.parse(localStorage.getItem('myList')) || [];

    if (!myList.some(b => b.title === book.title)) {
        myList.push(book);
        localStorage.setItem('myList', JSON.stringify(myList));
        alert('Livro adicionado à lista de favoritos!');
    } else {
        alert('Livro já está na lista de favoritos.');
    }
}

// Função para filtrar livros por gênero
function filterBooksByGenre(genre) {
    const books = document.querySelectorAll('.book');

    books.forEach(book => {
        const bookGenre = book.querySelector('.book-description').innerText.toLowerCase();
        if (genre === 'all' || bookGenre.includes(genre)) {
            book.style.display = 'flex';
        } else {
            book.style.display = 'none';
        }
    });
}

// Evento para filtrar livros ao mudar o gênero
const genreFilter = document.getElementById('genre-filter');
genreFilter.addEventListener('change', (event) => {
    filterBooksByGenre(event.target.value.toLowerCase());
});
