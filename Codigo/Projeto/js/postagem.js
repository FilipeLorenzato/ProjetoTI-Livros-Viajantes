document.addEventListener('DOMContentLoaded', () => {
    const currentIndex = { programming: 0, terror: 0, aventura: 0, fantasia: 0, romance: 0, ficcao: 0 };

    // Função para exibir os livros no carrossel
    function displayBooks(bookListElement, books) {
        books.forEach(book => {
            const card = document.createElement('div');
            card.classList.add('card');

            // Estrutura do card de livro com imagem, título e sinopse
            card.innerHTML = `
                <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}&author=${encodeURIComponent(book.author)}&synopsis=${encodeURIComponent(book.synopsis)}">
                    <img src="${book.image}" alt="${book.name}" class="book-image">
                    <h2>${book.name}</h2>
                    <p>${book.synopsis}</p>
                </a>
                <button class="add-to-list-btn">Adicionar à Lista</button>
            `;

            // Evento para abrir o modal com informações detalhadas do livro
            card.querySelector('img').addEventListener('click', () => openModal(book));

            // Evento para adicionar o livro à lista
            card.querySelector('.add-to-list-btn').addEventListener('click', () => addToMyList(book));

            // Adiciona o card no carrossel
            bookListElement.appendChild(card);
        });
    }

    // Função para abrir o modal
    function openModal(book) {
        const modal = document.getElementById('modal');
        const modalContent = document.querySelector('.modal-content');

        modalContent.innerHTML = `
            <button class="close-btn">&times;</button>
            <h2 class="book-details-title">${book.name}</h2>
            <img src="${book.image}" alt="${book.name}" class="modal-book-image">
            <p><strong>Autor:</strong> ${book.author}</p>
            <p><strong>Sinopse:</strong> ${book.synopsis}</p>
            <a href="historico.html" class="trade-btn">Quero Trocar</a>
        `;

        modal.style.display = 'flex';

        // Evento para fechar o modal
        modalContent.querySelector('.close-btn').addEventListener('click', () => {
            modal.style.display = 'none';
        });

        // Evento para fechar o modal clicando fora do conteúdo
        modal.addEventListener('click', (event) => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
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

        // Verifica se o livro já está na lista
        if (!myList.some(b => b.name === book.name)) {
            myList.push(book);
            localStorage.setItem('myList', JSON.stringify(myList));
            alert('Livro adicionado à lista de favoritos!');
        } else {
            alert('Livro já está na lista de favoritos.');
        }
    }

    // Função para mover o carrossel para a esquerda ou direita
    function moveCarousel(type, step) {
        const carousel = document.getElementById(`${type}-book-list`);
        const items = carousel.querySelectorAll('.card');
        const totalItems = items.length;
        if (totalItems === 0) return; // Se não houver itens, saio da função

        const itemWidth = items[0].offsetWidth + parseInt(getComputedStyle(items[0]).marginRight);
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

    // Função para buscar livros da API do Google Books e exibi-los no carrossel correspondente
    function fetchBooksByGenre(genre, elementId) {
        const query = genre === 'programming' ? 'programming' :
                      genre === 'terror' ? 'horror' :
                      genre === 'aventura' ? 'adventure' :
                      genre === 'fantasia' ? 'fantasy' :
                      genre === 'romance' ? 'romance' :
                      genre === 'ficcao' ? 'fiction' : 'fiction';
        const apiKey = 'AIzaSyAGsmI-fG7fZ9G5At3hcvaunFzdAiTGhLQ'; // Substitua pela sua chave de API válida
        const url = `https://www.googleapis.com/books/v1/volumes?q=${query}&maxResults=10&key=${apiKey}`;

        // Faço uma requisição para a API do Google Books para buscar livros do gênero solicitado
        fetch(url)
            .then(response => response.json()) // Converto a resposta da API em JSON
            .then(data => {
                if (!data.items) {
                    console.warn(`Nenhum livro encontrado para o gênero: ${genre}`);
                    return;
                }

                // Crio uma lista de livros retornada pela API com título, imagem, autor e sinopse
                const books = data.items.map(item => ({
                    name: item.volumeInfo.title,
                    author: item.volumeInfo.authors ? item.volumeInfo.authors.join(', ') : 'Autor desconhecido',
                    image: item.volumeInfo.imageLinks ? item.volumeInfo.imageLinks.thumbnail : 'https://via.placeholder.com/150',
                    genre: genre,
                    synopsis: item.volumeInfo.description ? item.volumeInfo.description.substring(0, 100) + '...' : 'Sem descrição'
                }));
                // Exibo os livros no carrossel correspondente
                displayBooks(document.getElementById(elementId), books);
            })
            .catch(error => console.error(`Erro ao buscar livros do gênero ${genre}:`, error)); // Caso ocorra um erro, exibo no console
    }

    // Função para carregar os livros salvos no localStorage e exibi-los no carrossel
    function loadBooksFromLocalStorage() {
        const myList = JSON.parse(localStorage.getItem('myList')) || []; // Carrego a lista de livros salvos no localStorage

        // Percorro a lista de livros e exibo cada um no carrossel correspondente
        myList.forEach(book => {
            const carouselId = `${book.genre}-book-list`;
            const carousel = document.getElementById(carouselId);

            if (carousel) {
                // Verifica se o livro já está presente no carrossel para evitar duplicação
                const isDuplicate = Array.from(carousel.children).some(child => {
                    const link = child.querySelector('a');
                    return link && decodeURIComponent(new URLSearchParams(link.href.split('?')[1]).get('name')) === book.name;
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
        const card = document.createElement('div');
        card.classList.add('card');

        // Estrutura do card de livro com imagem, título e sinopse
        card.innerHTML = `
            <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}&author=${encodeURIComponent(book.author)}&synopsis=${encodeURIComponent(book.synopsis)}">
                <img src="${book.image}" alt="${book.name}" class="book-image">
                <h2>${book.name}</h2>
                <p>${book.synopsis}</p>
            </a>
            <button class="add-to-list-btn">Adicionar à Lista</button>
        `;

        // Evento para abrir o modal com informações detalhadas do livro
        card.querySelector('img').addEventListener('click', () => openModal(book));

        // Evento para adicionar o livro à lista
        card.querySelector('.add-to-list-btn').addEventListener('click', () => addToMyList(book));

        return card; // Retorno o card criado
    }

    // Função de busca para exibir apenas o livro exato ou todos os livros se o campo de busca estiver vazio
    document.getElementById('search-button').addEventListener('click', function() {
        const query = document.getElementById('search-input').value.toLowerCase().trim(); // Pego o texto digitado no campo de busca

        const allCarousels = document.querySelectorAll('.carousel-items'); // Pego todos os carrosséis

        if (query === '') {
            // Se o campo de busca estiver vazio, mostro todos os livros
            allCarousels.forEach(carousel => {
                const books = carousel.querySelectorAll('.card'); // Seleciono todos os cards do carrossel
                books.forEach(bookCard => {
                    bookCard.style.display = 'flex'; // Exibo todos os livros
                });
            });
        } else {
            let found = false;

            // Se houver texto na busca, oculto todos os livros e exibo apenas o correspondente ao que foi buscado
            allCarousels.forEach(carousel => {
                const books = carousel.querySelectorAll('.card'); // Pego todos os cards do carrossel

                // Inicialmente, escondo todos os livros
                books.forEach(bookCard => {
                    bookCard.style.display = 'none';
                });

                // Verifico se algum livro tem o título igual ao texto buscado
                books.forEach(bookCard => {
                    const bookTitle = bookCard.querySelector('h2').textContent.toLowerCase();
                    if (bookTitle === query) {
                        found = true; // Se encontrar o livro, altero `found` para true
                        bookCard.style.display = 'flex'; // Exibo o livro correspondente
                    }
                });
            });

            if (!found) { // Se `found` continuar sendo false, significa que nenhum livro foi encontrado
                alert('Livro não encontrado!');
            }
        }
    });

    // Inicialização
    loadBooksFromLocalStorage();

    // Buscando e exibindo livros de cada gênero na API do Google Books
    fetchBooksByGenre('programming', 'programming-book-list');
    fetchBooksByGenre('terror', 'terror-book-list');
    fetchBooksByGenre('aventura', 'aventura-book-list');
    fetchBooksByGenre('fantasia', 'fantasia-book-list');
    fetchBooksByGenre('romance', 'romance-book-list');
    fetchBooksByGenre('ficcao', 'ficcao-book-list');

    // Adicionar event listeners para os botões de navegação dos carrosséis
    const genres = ['programming', 'terror', 'aventura', 'fantasia', 'romance', 'ficcao'];

    genres.forEach(genre => {
        const carousel = document.getElementById(`${genre}-carousel`);
        if (carousel) {
            const prevButton = carousel.querySelector('.carousel-button.prev');
            const nextButton = carousel.querySelector('.carousel-button.next');

            prevButton.addEventListener('click', () => moveCarousel(genre, -1));
            nextButton.addEventListener('click', () => moveCarousel(genre, 1));
        }
    });
});
