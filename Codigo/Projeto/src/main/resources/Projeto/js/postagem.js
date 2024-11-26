document.addEventListener("DOMContentLoaded", () => {
  buscarTodosOsLivros();

  async function buscarTodosOsLivros() {
    try {
      const response = await fetch(`http://localhost:4567/livros`);

      if (response.ok) {
        const livros = await response.json();
        console.log(livros);

        // Process the books
        processBooks(livros);
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

  function processBooks(livros) {
    // Group books by genre
    const booksByGenre = {};

    livros.forEach((livro) => {
      const genre = livro.genero || "Outros"; // Use "Outros" if genre is undefined
      if (!booksByGenre[genre]) {
        booksByGenre[genre] = [];
      }
      booksByGenre[genre].push(livro);
    });

    // Display the books in carousels
    displayCarousels(booksByGenre);
  }

  function displayCarousels(booksByGenre) {
    const carouselsContainer = document.getElementById("carousels-container");
    if (!carouselsContainer) {
      console.error("Carousels container not found in HTML");
      return;
    }

    Object.keys(booksByGenre).forEach((genre) => {
      // Create carousel elements for this genre
      const carouselWrapper = document.createElement("div");
      carouselWrapper.classList.add("carousel-wrapper");

      // Create a title for the genre
      const genreTitle = document.createElement("h2");
      genreTitle.textContent = genre;
      genreTitle.style.textTransform = "capitalize";
      genreTitle.style.margin = "1rem";
      carouselWrapper.appendChild(genreTitle);

      // Create carousel container
      const carousel = document.createElement("div");
      carousel.classList.add("carousel");
      carousel.id = `${genre}-carousel`;

      // Create buttons for navigation
      const prevButton = document.createElement("button");
      prevButton.classList.add("carousel-button", "prev");
      prevButton.textContent = "<";
      const nextButton = document.createElement("button");
      nextButton.classList.add("carousel-button", "next");
      nextButton.textContent = ">";

      // Append buttons to carousel
      carousel.appendChild(prevButton);

      // Create the book list container
      const bookListElement = document.createElement("div");
      bookListElement.classList.add("carousel-items");
      bookListElement.id = `${genre}-book-list`;

      // Append the book list element to carousel
      carousel.appendChild(bookListElement);

      carousel.appendChild(nextButton);

      // Append the carousel to the wrapper
      carouselWrapper.appendChild(carousel);

      // Append the wrapper to the container
      carouselsContainer.appendChild(carouselWrapper);

      // Display the books in this genre
      displayBooks(bookListElement, booksByGenre[genre]);

      // Initialize carousel index
      carousel.currentIndex = 0;

      // Add event listeners for carousel navigation
      prevButton.addEventListener("click", () => moveCarousel(carousel, -1));
      nextButton.addEventListener("click", () => moveCarousel(carousel, 1));
    });
  }

  function displayBooks(bookListElement, books) {
    books.forEach((book) => {
      const card = document.createElement("div");
      card.classList.add("card");

      // Construct the image source
      let imageSrc;
      if (book.imagem) {
        imageSrc = `data:image/jpeg;base64,${book.imagem}`;
      } else {
        imageSrc = "img/default.jpg"; // Use your default image path
      }

      // Card structure with image, title, and synopsis
      card.innerHTML = `
        <a href="detalhes-livro.html?titulo=${encodeURIComponent(
          book.titulo
        )}&imagem=${encodeURIComponent(book.imagem)}&autor=${encodeURIComponent(
        book.autor
      )}&sinopse=${encodeURIComponent(book.sinopse)}">
          <img src="${book.imagem}" alt="${book.titulo}" class="book-image">
          <h2>${book.titulo}</h2>
          <p>${book.sinopse}</p>
        </a>
        <button class="add-to-list-btn">Adicionar à Lista</button>
      `;

      // Event to open modal with detailed book info
      card.querySelector("img").addEventListener("click", (event) => {
        event.preventDefault(); // Prevent default link behavior
        openModal(book);
      });

      // Event to add book to the list
      card
        .querySelector(".add-to-list-btn")
        .addEventListener("click", () => addToMyList(book));

      // Add the card to the carousel
      bookListElement.appendChild(card);
    });
  }

  function moveCarousel(carousel, step) {
    const items = carousel.querySelectorAll(".card");
    const totalItems = items.length;
    if (totalItems === 0) return; // If no items, exit the function

    const itemWidth =
      items[0].offsetWidth + parseInt(getComputedStyle(items[0]).marginRight);
    const visibleItems = Math.floor(carousel.offsetWidth / itemWidth);

    carousel.currentIndex += step; // Update the index by the step

    // Prevent the index from going out of bounds
    if (carousel.currentIndex < 0) {
      carousel.currentIndex = 0; // Can't go further left
    } else if (carousel.currentIndex > totalItems - visibleItems) {
      carousel.currentIndex = totalItems - visibleItems; // Can't go further right
    }

    // Move the carousel to the new position
    const newTranslate = -(carousel.currentIndex * itemWidth);
    carousel.querySelector(
      ".carousel-items"
    ).style.transform = `translateX(${newTranslate}px)`;
  }

  // Função para abrir o modal
  function openModal(book) {
    const modal = document.getElementById("modal");
    const modalContent = document.querySelector(".modal-content");

    modalContent.innerHTML = `
            <button class="close-btn">&times;</button>
            <h2 class="book-details-title">${book.name}</h2>
            <img src="${book.image}" alt="${book.name}" class="modal-book-image">
            <p><strong>Autor:</strong> ${book.author}</p>
            <p><strong>Sinopse:</strong> ${book.synopsis}</p>
            <a href="historico.html" class="trade-btn">Quero Trocar</a>
        `;

    modal.style.display = "flex";

    // Evento para fechar o modal
    modalContent.querySelector(".close-btn").addEventListener("click", () => {
      modal.style.display = "none";
    });

    // Evento para fechar o modal clicando fora do conteúdo
    modal.addEventListener("click", (event) => {
      if (event.target === modal) {
        modal.style.display = "none";
      }
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

  // Função para mover o carrossel para a esquerda ou direita
  function moveCarousel(type, step) {
    const carousel = document.getElementById(`${type}-book-list`);
    const items = carousel.querySelectorAll(".card");
    const totalItems = items.length;
    if (totalItems === 0) return; // Se não houver itens, saio da função

    const itemWidth =
      items[0].offsetWidth + parseInt(getComputedStyle(items[0]).marginRight);
    const visibleItems = Math.floor(carousel.offsetWidth / itemWidth);

    currentIndex[type] += step; // Atualizo o índice de acordo com o passo

    // Impedir que o índice saia dos limites do carrossel (esquerda ou direita)
    if (currentIndex[type] < 0) {
      currentIndex[type] = 0; // Não posso ir mais para a esquerda
    } else if (currentIndex[type] > totalItems - visibleItems) {
      currentIndex[type] = totalItems - visibleItems; // Não posso ir mais para a direita
    }

    // Movo o carrossel para a nova posição
    const newTranslate = -(currentIndex[type] * itemWidth);
    carousel.style.transform = `translateX(${newTranslate}px)`;
  }

  // Função para carregar os livros salvos no localStorage e exibi-los no carrossel
  function loadBooksFromLocalStorage() {
    const myList = JSON.parse(localStorage.getItem("myList")) || []; // Carrego a lista de livros salvos no localStorage

    // Percorro a lista de livros e exibo cada um no carrossel correspondente
    myList.forEach((book) => {
      const carouselId = `${book.genre}-book-list`;
      const carousel = document.getElementById(carouselId);

      if (carousel) {
        // Verifica se o livro já está presente no carrossel para evitar duplicação
        const isDuplicate = Array.from(carousel.children).some((child) => {
          const link = child.querySelector("a");
          return (
            link &&
            decodeURIComponent(
              new URLSearchParams(link.href.split("?")[1]).get("name")
            ) === book.name
          );
        });

        if (!isDuplicate) {
          const bookCard = createBookCard(book); // Crio um card para o livro
          carousel.appendChild(bookCard); // Adiciono o card ao carrossel
        }
      } else {
        console.error(`Carrossel não encontrado para o gênero: ${book.genre}`);
      }
    });
  }

  // Função para criar um card de livro com os dados do livro
  function createBookCard(book) {
    const card = document.createElement("div");
    card.classList.add("card");

    // Estrutura do card de livro com imagem, título e sinopse
    card.innerHTML = `
            <a href="detalhes-livro.html?name=${encodeURIComponent(
              book.name
            )}&image=${encodeURIComponent(
      book.image
    )}&author=${encodeURIComponent(book.author)}&synopsis=${encodeURIComponent(
      book.synopsis
    )}">
                <img src="${book.image}" alt="${book.name}" class="book-image">
                <h2>${book.name}</h2>
                <p>${book.synopsis}</p>
            </a>
            <button class="add-to-list-btn">Adicionar à Lista</button>
        `;

    // Evento para abrir o modal com informações detalhadas do livro
    card.querySelector("img").addEventListener("click", () => openModal(book));

    // Evento para adicionar o livro à lista
    card
      .querySelector(".add-to-list-btn")
      .addEventListener("click", () => addToMyList(book));

    return card; // Retorno o card criado
  }

  // Função de busca para exibir apenas o livro exato ou todos os livros se o campo de busca estiver vazio
  document
    .getElementById("search-button")
    .addEventListener("click", function () {
      const query = document
        .getElementById("search-input")
        .value.toLowerCase()
        .trim(); // Pego o texto digitado no campo de busca

      const allCarousels = document.querySelectorAll(".carousel-items"); // Pego todos os carrosséis

      if (query === "") {
        // Se o campo de busca estiver vazio, mostro todos os livros
        allCarousels.forEach((carousel) => {
          const books = carousel.querySelectorAll(".card"); // Seleciono todos os cards do carrossel
          books.forEach((bookCard) => {
            bookCard.style.display = "flex"; // Exibo todos os livros
          });
        });
      } else {
        let found = false;

        // Se houver texto na busca, oculto todos os livros e exibo apenas o correspondente ao que foi buscado
        allCarousels.forEach((carousel) => {
          const books = carousel.querySelectorAll(".card"); // Pego todos os cards do carrossel

          // Inicialmente, escondo todos os livros
          books.forEach((bookCard) => {
            bookCard.style.display = "none";
          });

          // Verifico se algum livro tem o título igual ao texto buscado
          books.forEach((bookCard) => {
            const bookTitle = bookCard
              .querySelector("h2")
              .textContent.toLowerCase();
            if (bookTitle === query) {
              found = true; // Se encontrar o livro, altero `found` para true
              bookCard.style.display = "flex"; // Exibo o livro correspondente
            }
          });
        });

        if (!found) {
          // Se `found` continuar sendo false, significa que nenhum livro foi encontrado
          alert("Livro não encontrado!");
        }
      }
    });

  // Inicialização
  loadBooksFromLocalStorage();
});
