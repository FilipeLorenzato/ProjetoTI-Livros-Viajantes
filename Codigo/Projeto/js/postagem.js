document.addEventListener('DOMContentLoaded', () => {
    const currentIndex = { programming: 0, terror: 0, aventura: 0, fantasia: 0, romance: 0, ficcao: 0 };

    // Função para exibir os livros no carrossel
    function displayBooks(bookListElement, books) {
        books.forEach(book => {
            const card = document.createElement('div');
            card.classList.add('card');
            
            card.innerHTML = `
                <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}">
                    <img src="${book.image}" alt="${book.name}">
                    <h2>${book.name}</h2>
                    <p>${book.synopsis}</p>
                </a>
            `;
            bookListElement.appendChild(card);
        });
    }

    // Função para buscar livros da API do Google Books
    function fetchBooksByGenre(genre, elementId) {
        const query = genre === 'programming' ? 'programming' :
                      genre === 'terror' ? 'horror' :
                      genre === 'aventura' ? 'adventure' :
                      genre === 'fantasia' ? 'fantasy' :
                      genre === 'romance' ? 'romance' :
                      genre === 'ficcao' ? 'fiction' : 'fiction';
        const url = `https://www.googleapis.com/books/v1/volumes?q=${query}&key=AIzaSyAGsmI-fG7fZ9G5At3hcvaunFzdAiTGhLQ`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const books = data.items.map(item => ({
                    name: item.volumeInfo.title,
                    image: item.volumeInfo.imageLinks ? item.volumeInfo.imageLinks.thumbnail : 'https://via.placeholder.com/150',
                    synopsis: item.volumeInfo.description ? item.volumeInfo.description.substring(0, 100) + '...' : 'Sem descrição'
                }));
                displayBooks(document.getElementById(elementId), books);
            })
            .catch(error => console.error(`Erro ao buscar livros do gênero ${genre}:`, error));
    }

    // Função para carregar livros do localStorage
    function loadBooksFromLocalStorage() {
        const bookList = JSON.parse(localStorage.getItem('books')) || [];

        bookList.forEach(book => {
            const carouselId = `${book.genre}-book-list`;
            const carousel = document.getElementById(carouselId);

            if (carousel) {
                const bookCard = createBookCard(book);
                carousel.appendChild(bookCard);
            } else {
                console.error(`Carrossel não encontrado para o gênero: ${book.genre}`);
            }
        });
    }

    // Função para criar um card de livro com os dados do localStorage
    function createBookCard(book) {
        const card = document.createElement('div');
        card.classList.add('card');
        
        card.innerHTML = `
            <a href="detalhes-livro.html?name=${encodeURIComponent(book.name)}&image=${encodeURIComponent(book.image)}">
                <img src="${book.image}" alt="${book.name}">
                <h2>${book.name}</h2>
                <p>${book.synopsis}</p>
            </a>
        `;
        
        return card;
    }

    // Função para mover o carrossel
    function moveCarousel(type, step) {
        const carousel = document.getElementById(`${type}-book-list`);
        const items = carousel.querySelectorAll('.card');
        const totalItems = items.length;
        if (totalItems === 0) return; // Não faz nada se não há itens

        const itemWidth = items[0].offsetWidth + 20; // Inclui margem ou espaçamento
        const visibleItems = Math.floor(carousel.offsetWidth / itemWidth); // Itens visíveis por vez
        const maxTranslate = Math.min(0, carousel.offsetWidth - (totalItems * itemWidth));

        // Calcula o novo índice
        currentIndex[type] += step;

        // Impede de ir além dos limites (esquerda e direita)
        if (currentIndex[type] < 0) {
            currentIndex[type] = 0; // Limita à esquerda
        } else if (currentIndex[type] > totalItems - visibleItems) {
            currentIndex[type] = totalItems - visibleItems; // Limita à direita
        }

        // Move o carrossel
        const newTranslate = -(currentIndex[type] * itemWidth);
        carousel.style.transform = `translateX(${newTranslate}px)`;
    }

    // Carrega livros cadastrados no localStorage
    loadBooksFromLocalStorage();

    // Chama as funções para buscar livros por gênero (API do Google Books)
    fetchBooksByGenre('programming', 'programming-book-list');
    fetchBooksByGenre('terror', 'terror-book-list');
    fetchBooksByGenre('aventura', 'aventura-book-list');
    fetchBooksByGenre('fantasia', 'fantasia-book-list');
    fetchBooksByGenre('romance', 'romance-book-list');
    fetchBooksByGenre('ficcao', 'ficcao-book-list');

    // Adiciona eventos para os botões do carrossel
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
