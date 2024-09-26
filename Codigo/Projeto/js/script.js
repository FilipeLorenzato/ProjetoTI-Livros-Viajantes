document.addEventListener("DOMContentLoaded", function() {
    const myLibrary = document.getElementById("my-library");
    const closeDetailsButton = document.getElementById("close-details");
    const bookDetails = document.getElementById("book-details");
    const bookImageDetails = document.getElementById("image-details");
    const bookDetailsContent = document.querySelector(".book-details-content");
    const bookTitle = document.getElementById("book-title");
    const bookDescription = document.getElementById("book-description");
    const genreFilter = document.getElementById("genre-filter");
    const logoutBtn = document.getElementById("logout-btn");

    const favoriteBooks = [];

    // Função para lidar com o logout
    logoutBtn.addEventListener("click", function() {
        // Exemplo simples: Limpar o localStorage ou sessionStorage
        localStorage.clear(); 

        // Redireciona o usuário para a página de login
        window.location.href = "login.html";
    });
    
    
    // Estrutura JSON com os dados dos livros
    const bookList = [
        {
            title: "Corte de Névoa e Fúria",
            description: "Feyre retorna à Corte da Primavera após ter derrotado Amarantha, mas agora enfrenta novas ameaças e desafios, descobrindo poderes ocultos e uma conexão inesperada com Rhysand, o Grão-Senhor da Corte Noturna.",
            genre: "Fantasia",
            image_url: "img/livro1.jpg",
            inMyList: true
        },
        {
            title: "Fourth Wing",
            description: "Em um mundo onde dragões são essenciais para a guerra, Violet Sorrengail deve enfrentar seu destino na elite Dragon Riders Academy, onde somente os mais fortes sobrevivem. Entre intrigas e batalhas, ela precisa provar seu valor.",
            genre: "Fantasia",
            image_url: "img/livro2.jpg",
            inMyList: true
        },
        {
            title: "Love, Theoretically",
            description: "Elsie Hannaway, uma professora adjunta de física, equilibra sua carreira acadêmica com um trabalho paralelo de namoro falso. Seu mundo vira de cabeça para baixo quando Jack Smith-Turner, um renomado físico, desafia sua vida profissional e pessoal, levando-a a reavaliar suas escolhas e sentimentos.",
            genre: "Romance",
            image_url: "img/livro3.jpg",
            inMyList: true
        },
        {
            title: "Coroa da Meia-Noite",
            description: "A assassina Celaena Sardothien retorna ao castelo do rei como campeã, mas enquanto cumpre missões mortais, ela descobre segredos sombrios sobre o reino e precisa escolher entre lealdade e amor.",
            genre: "Fantasia",
            image_url: "img/livro4.jpg",
            inMyList: true
        },
        {
            title: "É Assim que Acaba",
            description: "Lily Bloom enfrenta um dilema emocional quando conhece o charmoso Ryle Kincaid, mas a chegada de seu antigo amor, Atlas Corrigan, a faz reavaliar tudo o que pensava sobre amor, trauma e redenção.",
            genre: "Romance",
            image_url: "img/livro5.jpg",
            inMyList: true
        },
        {
            title: "A Empregada",
            description: "Molly Gray, uma empregada de hotel que lida com dificuldades sociais, se vê envolvida em um mistério de assassinato quando descobre o corpo de um hóspede no quarto que estava limpando. Sua vida vira de cabeça para baixo enquanto tenta provar sua inocência.",
            genre: "Suspense",
            image_url: "img/livro6.jpg",
            inMyList: true
        },
        {
            title: "Harry Potter",
            description: "Harry Potter, um menino órfão, descobre que é um bruxo no seu 11º aniversário e é convidado a estudar na Escola de Magia e Bruxaria de Hogwarts. Lá, ele descobre seu passado, faz novos amigos e enfrenta o perigoso Lord Voldemort.",
            genre: "Fantasia",
            image_url: "img/livro7.jpg",
            inMyList: true
        },
        {
            title: "Verity",
            description: "Lowen Ashleigh, uma escritora em dificuldades, é contratada para terminar os livros de Verity Crawford, uma famosa autora que está incapacitada. Ao vasculhar os manuscritos de Verity, Lowen descobre segredos perturbadores que colocam sua vida em risco.",
            genre: "Suspense",
            image_url: "img/livro8.jpg",
            inMyList: true
        }
    ];

    // Adiciona livros à lista de favoritos
    for(let i = 0; i < bookList.length; i++){
        if(bookList[i].inMyList) {
            favoriteBooks.push(bookList[i]);
        }
    } 

    // Renderiza a lista de livros na página
    renderMyLibrary();

    // Exibe detalhes do livro quando clicado
    myLibrary.addEventListener("click", (event) => {
        const clickedElement = event.target;

        if (clickedElement.classList.contains("book-card")) {
            // Se o clique ocorreu em um elemento com a classe "book-card"
            const index = clickedElement.getAttribute("data-index");
            showBookDetails(index);
        } else if (clickedElement.tagName === "IMG" && clickedElement.closest(".book-card")) {
            // Se o clique ocorreu em uma imagem dentro de um elemento com a classe "book-card"
            const bookCard = clickedElement.closest(".book-card");
            const index = bookCard.getAttribute("data-index");
            showBookDetails(index);
        }
    });
    
    // Fecha a janela de detalhes do livro
    closeDetailsButton.addEventListener("click", () => {
        bookDetails.style.display = "none";
    });

    // Filtra os livros por gênero
    genreFilter.addEventListener('change', () => {
        const selectedGenre = genreFilter.value;
        if (selectedGenre === 'all') {
            renderMyLibrary();
        } else {
            const filteredBooks = bookList.filter(book => book.genre === selectedGenre);
            renderMyLibrary(filteredBooks);
        }
    });

    function renderMyLibrary(books) {
        myLibrary.innerHTML = "";
        (books || favoriteBooks).forEach((book, index) => {
            const bookCard = document.createElement("div");
            bookCard.classList.add("book-card");
            bookCard.setAttribute("data-index", index);
            bookCard.innerHTML = `
                <img src="${book.image_url}" alt="${book.title}">
                <h3>${book.title}</h3>
                <p class="remove-button">${book.inMyList ? 'Remover da Minha Biblioteca' : 'Adicionar à Minha Biblioteca'}</p>
            `;
            myLibrary.appendChild(bookCard);

            // Remove o livro da Minha Biblioteca
            bookCard.querySelector('.remove-button').addEventListener('click', () => {
                favoriteBooks[index].inMyList = !favoriteBooks[index].inMyList;
                if (!favoriteBooks[index].inMyList) {
                    favoriteBooks.splice(index, 1);
                }
                renderMyLibrary();
            });
        });
    }

    function showBookDetails(index) {
        bookImageDetails.src = favoriteBooks[index].image_url;
        bookTitle.textContent = favoriteBooks[index].title;
        bookDescription.textContent = favoriteBooks[index].description;
        bookDetails.style.display = 'flex';
    }
});
