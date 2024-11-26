document.addEventListener("DOMContentLoaded", function () {
  const myLibrary = document.getElementById("my-library");
  const closeDetailsButton = document.getElementById("close-details");
  const bookDetails = document.getElementById("book-details");
  const bookImageDetails = document.getElementById("image-details");
  const bookTitle = document.getElementById("book-title");
  const bookDescription = document.getElementById("book-description");
  const genreFilter = document.getElementById("genre-filter");

  let userBooks = [];

  // Renderiza a lista de livros na página
  function renderBooks(books = userBooks) {
    const booksContainer = document.querySelector(".books-container");
    booksContainer.innerHTML = "";

    books.forEach((book, index) => {
      const bookCard = document.createElement("div");
      console.log(book);
      bookCard.classList.add("book-card");
      bookCard.setAttribute("data-index", index);

      bookCard.innerHTML = `
          <img src="${book.imagem}" alt="${book.titulo}" class="book-image">
          <h3 class="book-title">${book.titulo}</h3>
          <p class="remove-button">Remover da Minha Biblioteca</p>
      `;

      booksContainer.appendChild(bookCard);

      // Evento para exibir detalhes do livro
      bookCard.addEventListener("click", () => {
        showBookDetails(index);
      });

      // Evento para remover o livro
      bookCard
        .querySelector(".remove-button")
        .addEventListener("click", (event) => {
          event.stopPropagation();
          removerLivro(index);
        });
    });
  }

  function showBookDetails(index) {
    const book = userBooks[index];
    bookImageDetails.src = book.image_url || "img/default.jpg";
    bookTitle.textContent = book.titulo;
    bookDescription.textContent = book.descricao;
    bookDetails.style.display = "flex";
  }

  function removerLivro(index) {
    if (confirm("Deseja realmente remover este livro da sua biblioteca?")) {
      const livroId = userBooks[index].idLivro;
      userBooks.splice(index, 1);
      renderBooks();
      // Atualizar no servidor
      removerLivroDoServidor(livroId);
    }
  }

  async function removerLivroDoServidor(livroId) {
    try {
      const response = await fetch(`http://localhost:4567/livro/${livroId}`, {
        method: "DELETE",
      });

      if (response.ok) {
        console.log("Livro removido do servidor.");
      } else {
        console.error("Erro ao remover livro do servidor:", response.status);
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
    }
  }

  genreFilter.addEventListener("change", () => {
    const selectedGenre = genreFilter.value;
    if (selectedGenre === "all") {
      renderBooks();
    } else {
      const filteredBooks = userBooks.filter(
        (book) => book.genero === selectedGenre
      );
      renderBooks(filteredBooks);
    }
  });

  closeDetailsButton.addEventListener("click", () => {
    bookDetails.style.display = "none";
  });

  async function buscarLivrosPorUsuario() {
    let userId = localStorage.getItem("userId");
    try {
      const response = await fetch(
        `http://localhost:4567/livros/usuario/${userId}`
      );

      if (response.ok) {
        const livros = await response.json();
        console.log("Livros do usuário:", livros);
        userBooks = livros;
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

  // Chama a função para buscar os livros ao carregar a página
  buscarLivrosPorUsuario();
});
