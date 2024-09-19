document.addEventListener('DOMContentLoaded', () => {
    const currentIndex = { programming: 0, terror: 0, aventura: 0, fantasia: 0, romance: 0, ficcao: 0 };

    // Função para exibir os livros no carrossel
    // Recebo o elemento do carrossel e a lista de livros para adicionar
    function displayBooks(bookListElement, books) {
        books.forEach(book => {
            const card = document.createElement('div');
            card.classList.add('card');
            
            // Crio um card com o título, imagem e sinopse do livro, e coloco um link para detalhes do livro
            card.innerHTML = `
                <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}">
                    <img src="${book.image}" alt="${book.name}">
                    <h2>${book.name}</h2>
                    <p>${book.synopsis}</p>
                </a>
            `;
            // Adiciono o card no carrossel
            bookListElement.appendChild(card);
        });
    }

    // Função de busca para exibir apenas o livro exato ou todos os livros se o campo de busca estiver vazio
    // Ativo a busca quando o usuário clica no botão "Buscar"
    document.getElementById('search-button').addEventListener('click', function() {
        const query = document.getElementById('search-input').value.toLowerCase().trim(); // Pego o texto digitado no campo de busca
        
        const allCarousels = document.querySelectorAll('.carousel-items'); // Pego todos os carrosséis

        if (query === '') {
            // Se o campo de busca estiver vazio, mostro todos os livros
            allCarousels.forEach(carousel => {
                const books = carousel.querySelectorAll('.card'); // Seleciono todos os cards do carrossel
                books.forEach(bookCard => {
                    bookCard.style.display = 'block'; // Exibo todos os livros
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
                        bookCard.style.display = 'block'; // Exibo o livro correspondente
                    }
                });
            });

            if (!found) { // Se `found` continuar sendo false, significa que nenhum livro foi encontrado
                alert('Livro não encontrado!');
            }
        }
    });

    // Função para buscar livros da API do Google Books e exibi-los no carrossel correspondente
    // Recebo o gênero de livros a ser buscado e o ID do carrossel onde exibir os livros
    function fetchBooksByGenre(genre, elementId) {
        const query = genre === 'programming' ? 'programming' :
                      genre === 'terror' ? 'horror' :
                      genre === 'aventura' ? 'adventure' :
                      genre === 'fantasia' ? 'fantasy' :
                      genre === 'romance' ? 'romance' :
                      genre === 'ficcao' ? 'fiction' : 'fiction';
        const url = `https://www.googleapis.com/books/v1/volumes?q=${query}&key=AIzaSyAGsmI-fG7fZ9G5At3hcvaunFzdAiTGhLQ`;

        // Faço uma requisição para a API do Google Books para buscar livros do gênero solicitado
        fetch(url)
            .then(response => response.json()) // Converto a resposta da API em JSON
            .then(data => {
                // Crio uma lista de livros retornada pela API com título, imagem e sinopse
                const books = data.items.map(item => ({
                    name: item.volumeInfo.title,
                    image: item.volumeInfo.imageLinks ? item.volumeInfo.imageLinks.thumbnail : 'https://via.placeholder.com/150',
                    synopsis: item.volumeInfo.description ? item.volumeInfo.description.substring(0, 100) + '...' : 'Sem descrição'
                }));
                // Exibo os livros no carrossel correspondente
                displayBooks(document.getElementById(elementId), books);
            })
            .catch(error => console.error(`Erro ao buscar livros do gênero ${genre}:`, error)); // Caso ocorra um erro, exibo no console
    }

    // Função para mover o carrossel para a esquerda ou direita
    // Recebo o tipo de carrossel e o valor de "step" (quantidade de itens a mover)
    function moveCarousel(type, step) {
        const carousel = document.getElementById(`${type}-book-list`); // Pego o carrossel correspondente ao tipo
        const items = carousel.querySelectorAll('.card'); // Pego todos os cards do carrossel
        const totalItems = items.length;
        if (totalItems === 0) return; // Se não houver itens, saio da função

        const itemWidth = items[0].offsetWidth + 20; // Calculo a largura de cada item (com margem)
        const visibleItems = Math.floor(carousel.offsetWidth / itemWidth); // Calculo quantos itens cabem no carrossel

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
        const bookList = JSON.parse(localStorage.getItem('books')) || []; // Carrego a lista de livros salvos no localStorage

        // Percorro a lista de livros e exibo cada um no carrossel correspondente
        bookList.forEach(book => {
            const carouselId = `${book.genre}-book-list`;
            const carousel = document.getElementById(carouselId);

            if (carousel) {
                const bookCard = createBookCard(book); // Crio um card para o livro
                carousel.appendChild(bookCard); // Adiciono o card ao carrossel
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
            <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}">
                <img src="${book.image}" alt="${book.name}">
                <h2>${book.name}</h2>
                <p>${book.synopsis}</p>
            </a>
        `;
        
        return card; // Retorno o card criado
    }

    // Carrego os livros do localStorage ao carregar a página
    loadBooksFromLocalStorage();

    // Faço as chamadas para buscar livros de cada gênero na API e exibi-los nos carrosséis correspondentes
    fetchBooksByGenre('programming', 'programming-book-list');
    fetchBooksByGenre('terror', 'terror-book-list');
    fetchBooksByGenre('aventura', 'aventura-book-list');
    fetchBooksByGenre('fantasia', 'fantasia-book-list');
    fetchBooksByGenre('romance', 'romance-book-list');
    fetchBooksByGenre('ficcao', 'ficcao-book-list');

    // Adiciono eventos de clique nos botões para mover os carrosséis para a esquerda e direita
    document.querySelector('#programming-carousel .prev').addEventListener('click', () => moveCarousel('programming', -1));
    document.querySelector('#programming-carousel .next').addEventListener('click', () => moveCarousel('programming', 1));
    document.querySelector('#terror-carousel .prev').addEventListener('click', () => moveCarousel('terror', -1));
    document.querySelector('#terror-carousel .next').addEventListener('click', () => moveCarousel('terror', 1));
    document.querySelector('#aventura-carousel .prev').addEventListener('click', () => moveCarousel('aventura', -1));
    document.querySelector('#aventura-carousel .next').addEventListener('click', () => moveCarousel('aventura', 1));
    document.querySelector('#fantasia-carousel .prev').addEventListener('click', () => moveCarousel('fantasia', -1));
    document.querySelector('#fantasia-carousel .next').addEventListener('click', () => moveCarousel('fantasia', 1));
    document.querySelector('#romance-carousel .prev').addEventListener('click', () => moveCarousel('romance', -1));
    document.querySelector('#romance-carousel .next').addEventListener('click', () => moveCarousel('romance', 1));
    document.querySelector('#ficcao-carousel .prev').addEventListener('click', () => moveCarousel('ficcao', -1));
    document.querySelector('#ficcao-carousel .next').addEventListener('click', () => moveCarousel('ficcao', 1));
});
