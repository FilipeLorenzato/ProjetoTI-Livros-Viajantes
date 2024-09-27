// Lista de livros
const books = [
    { title: "Orgulho e Preconceito", author: "Jane Austen", image: "https://m.media-amazon.com/images/I/719esIW3D7L._SY466_.jpg" },
    { title: "Dom Quixote", author: "Miguel de Cervantes", image: "https://m.media-amazon.com/images/I/51542n0o9dL._SY445_SX342_.jpg" },
    { title: "1984", author: "George Orwell", image: "https://m.media-amazon.com/images/I/819js3EQwbL._SY466_.jpg" },
    { title: "Cem Anos de Solidão", author: "Gabriel García Márquez", image: "https://m.media-amazon.com/images/I/515cVYLIP9L._SY445_SX342_.jpg" },
    { title: "Harry Potter e a Pedra Filosofal", author: "J.K. Rowling", image: "https://m.media-amazon.com/images/I/41897yAI4LL._SY445_SX342_.jpg" }
];

// Função para gerar a lista de livros
const bookList = document.getElementById("book-list");

books.forEach(book => {
    const bookItem = document.createElement("div");
    bookItem.classList.add("book");

    bookItem.innerHTML = `
        <img src="${book.image}" alt="${book.title}">
        <h3>${book.title}</h3>
        <p>por ${book.author}</p>
        <button class="cta">Solicitar Troca</button>
    `;

    bookList.appendChild(bookItem);
});
