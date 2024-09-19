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
  }
}

// Carrega livros na lista
function carregarLivros(filtrados = []) {
  const listaLivros = document.querySelector(".book-list");
  listaLivros.innerHTML = ""; // Limpa a lista antes de adicionar novos livros

  filtrados.forEach((livro) => {
    const itemLivro = document.createElement("li");
    itemLivro.className = "book-item";

    // Adiciona o conteúdo do livro
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

// Função para abrir o modal de filtros
function abrirModal() {
  const modal = document.getElementById("filterModal");
  modal.style.display = "block";
}

// Função para fechar o modal de filtros
function fecharModal() {
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
  fecharModal();
}

// Função para filtrar por faixa de preço
function filtrarPorPreco(precoLivro, faixaPreco) {
  const precoNumerico = parseFloat(precoLivro);

  switch (faixaPreco) {
    case '10-49.99':
      return precoNumerico >= 10 && precoNumerico <= 49.99;
    case '50-99.99':
      return precoNumerico >= 50 && precoNumerico <= 99.99;
    case '100-500':
      return precoNumerico >= 100 && precoNumerico <= 500;
    case '501+':
      return precoNumerico > 501;
    default:
      return true;
  }
}

// Eventos de abertura e fechamento do modal
document.querySelector(".filter-btn").addEventListener("click", abrirModal);
document.getElementById("closeFilter").addEventListener("click", fecharModal);

// Evento de submissão do formulário de filtros
document.getElementById("filterForm").addEventListener("submit", aplicarFiltros);

// Executa o carregamento dos livros ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
  carregarDadosLivros(); // Chama a função para carregar os dados do JSON ao carregar a página
});
