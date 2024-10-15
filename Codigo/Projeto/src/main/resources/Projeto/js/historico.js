let livrosCarregados = []; // Variável global para armazenar os livros carregados

// Função para carregar os dados do arquivo JSON
async function carregarDadosLivros() {
  try {
    const resposta = await fetch('livros.json'); // Carrega o arquivo JSON
    if (!resposta.ok) {
      throw new Error('Erro ao carregar o arquivo JSON');
    }
    const livros = await resposta.json(); // Converte a resposta em JSON
    livrosCarregados = livros; // Armazena os livros na variável global
    carregarLivros(livros); // Chama a função para renderizar os livros
  } catch (erro) {
    console.error('Erro ao carregar os dados dos livros:', erro);
    const listaLivros = document.querySelector(".book-list");
    listaLivros.innerHTML = '<p class="error-message">Não foi possível carregar os livros no momento. Tente novamente mais tarde.</p>';
  }
}

// Carrega livros na lista
function carregarLivros(filtrados = []) {
  const listaLivros = document.querySelector(".book-list");
  listaLivros.innerHTML = ""; // Limpa a lista antes de adicionar novos livros

  const livrosParaMostrar = filtrados.length > 0 ? filtrados : livrosCarregados;

  livrosParaMostrar.forEach((livro) => {
    const itemLivro = document.createElement("li");
    itemLivro.className = "book-item";

    // Adiciona o conteúdo do livro usando template literals corretos
    itemLivro.innerHTML = `
      <img src="${livro.imagem}" alt="Imagem do livro ${livro.titulo}" class="book-image">
      <div class="book-info">
        <h3>${livro.titulo}</h3>
        <p>${livro.categoria} • ${livro.preco} • ${livro.distancia}</p>
        <p class="description">${livro.descricao}</p>
        <p class="exchange-date">Data da troca: ${livro.dataTroca}</p>
      </div>
      <div class="book-rating">${gerarAvaliacao(livro.avaliacao)}</div>
    `;

    // Evento para abrir o modal com informações detalhadas do livro
    itemLivro.querySelector('img').addEventListener('click', () => abrirModal(livro));

    // Evento para adicionar o livro à lista
    itemLivro.querySelector('.book-info').addEventListener('dblclick', () => addToMyList(livro));

    // Adiciona o livro à lista
    listaLivros.appendChild(itemLivro);
  });
}

// Gera estrelas de avaliação
function gerarAvaliacao(avaliacao) {
  let estrelas = "";
  for (let i = 0; i < 5; i++) {
    estrelas += i < avaliacao ? "★" : "☆";
  }
  return estrelas;
}

// Função para abrir o modal
function abrirModal(livro) {
  const modal = document.getElementById("modal");
  const modalContent = document.getElementById("modal-content");

  modalContent.innerHTML = `
    <h2 class="book-details-title">${livro.titulo}</h2>
    <img src="${livro.imagem}" alt="${livro.titulo}">
    <p><strong>Autor:</strong> ${livro.autor}</p>
    <p><strong>Sinopse:</strong> ${livro.descricao}</p>
    <button class="close-btn">Fechar</button>
    <a href="historico.html" class="trade-btn">Quero Trocar</a>
  `;
  modal.style.display = 'block';

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

// Função para adicionar o livro à lista de favoritos
function addToMyList(livro) {
  const myList = JSON.parse(localStorage.getItem('myList')) || [];
  if (!myList.some(b => b.titulo === livro.titulo)) {
    myList.push(livro);
    localStorage.setItem('myList', JSON.stringify(myList));
    alert('Livro adicionado à lista de favoritos!');
  } else {
    alert('Livro já está na lista de favoritos.');
  }
}

// Função para abrir o modal de filtros
function abrirModalFiltros() {
  const modal = document.getElementById("filterModal");
  modal.style.display = "block";
}

// Função para fechar o modal de filtros
function fecharModalFiltros() {
  const modal = document.getElementById("filterModal");
  modal.style.display = "none";
}

// Função para aplicar filtros
function aplicarFiltros(event) {
  event.preventDefault();

  const categoria = document.getElementById("category").value;
  const preco = document.getElementById("price").value;

  // Aplica os filtros nos livros já carregados
  const filtrados = livrosCarregados.filter((livro) => {
    const categoriaFiltra = categoria ? livro.categoria === categoria : true;

    // Remove o "R$" e converte o preço para número
    const precoNumerico = parseFloat(livro.preco.replace("R$", "").replace(",", "."));
    const precoFiltra = preco ? filtrarPorPreco(precoNumerico, preco) : true;

    return categoriaFiltra && precoFiltra;
  });

  carregarLivros(filtrados); // Atualiza a exibição com os livros filtrados
  fecharModalFiltros();
}

// Função para filtrar por faixa de preço
function filtrarPorPreco(precoLivro, faixaPreco) {
  switch (faixaPreco) {
    case '10-49.99':
      return precoLivro >= 10 && precoLivro <= 49.99;
    case '50-99.99':
      return precoLivro >= 50 && precoLivro <= 99.99;
    case '100-500':
      return precoLivro >= 100 && precoLivro <= 500;
    case '501+':
      return precoLivro > 501;
    default:
      return true;
  }
}

// Função para buscar livros por título
function buscarPorTitulo(query) {
  const filtrados = livrosCarregados.filter(livro => livro.titulo.toLowerCase().includes(query.toLowerCase()));
  carregarLivros(filtrados);
}

// Evento de busca
document.getElementById("search-button").addEventListener("click", () => {
  const query = document.getElementById("search-input").value.trim();
  if (query === "") {
    carregarLivros(); // Carrega todos os livros se a busca estiver vazia
  } else {
    buscarPorTitulo(query);
  }
});

// Permite a busca ao pressionar Enter
document.getElementById("search-input").addEventListener("keypress", (event) => {
  if (event.key === "Enter") {
    event.preventDefault();
    document.getElementById("search-button").click();
  }
});

// Eventos de abertura e fechamento do modal de filtros
document.querySelector(".filter-btn").addEventListener("click", abrirModalFiltros);
document.getElementById("closeFilter").addEventListener("click", fecharModalFiltros);

// Evento de submissão do formulário de filtros
document.getElementById("filterForm").addEventListener("submit", aplicarFiltros);

// Executa o carregamento dos livros ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
  carregarDadosLivros(); // Chama a função para carregar os dados do JSON ao carregar a página
});
