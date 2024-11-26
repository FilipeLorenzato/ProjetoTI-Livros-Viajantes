// Lista de livros
let books;

document.addEventListener("DOMContentLoaded", () => {
  buscarTodosOsLivros();
});

async function buscarTodosOsLivros() {
  try {
    const response = await fetch(`http://localhost:4567/livros`);

    if (response.ok) {
      const livros = await response.json();
      books = livros;
      renderBooks();
    } else {
      console.error(
        "Erro ao buscar livros:",
        response.status,
        response.statusText
      );
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
  }
}

function renderBooks() {
  const bookList = document.getElementById("book-list");
  bookList.innerHTML = ""; // Limpa o container antes de adicionar novos elementos

  // Função para embaralhar e pegar um subset de livros
  function getRandomBooks(array, number) {
    const shuffled = array.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, number);
  }

  // Obtem 8 livros aleatórios da lista de livros
  const randomBooks = getRandomBooks(books, 8);

  randomBooks.forEach((book) => {
    const bookItem = document.createElement("div");
    bookItem.classList.add("book");

    bookItem.innerHTML = `
              <img src="${book.imagem}" alt="${book.titulo}" class="book-image">
              <h3>${book.titulo}</h3>
              <p class="book-author">por ${book.autor}</p>
              <p class="book-description">${book.sinopse}</p>
              <button class="add-to-list-btn">Adicionar à Lista</button>
          `;

    // Evento para abrir o modal com informações detalhadas do livro
    bookItem
      .querySelector("img")
      .addEventListener("click", () => openModal(book));

    // Evento para adicionar o livro à lista
    bookItem
      .querySelector(".add-to-list-btn")
      .addEventListener("click", () => addToMyList(book));

    // Adiciona o livro ao container
    bookList.appendChild(bookItem);
  });
}

// Função para abrir o modal
function openModal(book) {
  const modal = document.getElementById("modal");
  const modalContent = document.querySelector(".modal-content");

  modalContent.innerHTML = `
        <button class="close-btn">&times;</button>
        <h2 class="book-details-title">${book.title}</h2>
        <img src="${book.image}" alt="${book.title}" class="modal-book-image">
        <p><strong>Autor:</strong> ${book.author}</p>
        <p><strong>Sinopse:</strong> ${book.description}</p>
        <a href="historico.html" class="trade-btn">Quero Trocar</a>
    `;

  modal.style.display = "flex";

  // Evento para fechar o modal
  modalContent.querySelector(".close-btn").addEventListener("click", () => {
    modal.style.display = "none";
  });

  // Evento para redirecionar ao clicar em "Quero Trocar"
  modalContent
    .querySelector(".trade-btn")
    .addEventListener("click", (event) => {
      event.preventDefault(); // Previne o comportamento padrão do link
      window.location.href = "historico.html"; // Redireciona para a página desejada
    });
}

// Função para adicionar o livro à lista (Favoritos)
function addToMyList(book) {
  const myList = JSON.parse(localStorage.getItem("myList")) || [];

  if (!myList.some((b) => b.id === book.id)) {
    myList.push(book);
    localStorage.setItem("myList", JSON.stringify(myList));
    alert("Livro adicionado à lista de favoritos!");
  } else {
    alert("Livro já está na lista de favoritos.");
  }
}

document.getElementById("clearList").addEventListener("click", function () {
  // Confirm with the user before clearing
  if (confirm("Tem certeza de que deseja limpar toda a lista?")) {
    // Clear the 'myList' from localStorage
    localStorage.removeItem("myList");
    alert("Lista limpa com sucesso!");
  }
});

// Função para filtrar livros por gênero
function filterBooksByGenre(genre) {
  const books = document.querySelectorAll(".book");

  books.forEach((book) => {
    const bookGenre = book
      .querySelector(".book-description")
      .innerText.toLowerCase();
    if (genre === "all" || bookGenre.includes(genre)) {
      book.style.display = "flex";
    } else {
      book.style.display = "none";
    }
  });
}

// Evento para filtrar livros ao mudar o gênero
const genreFilter = document.getElementById("genre-filter");
genreFilter.addEventListener("change", (event) => {
  filterBooksByGenre(event.target.value.toLowerCase());
});
