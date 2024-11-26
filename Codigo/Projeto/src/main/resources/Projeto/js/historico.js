let trocasCarregadas = []; // Variável global para armazenar as trocas carregadas

// Função para carregar os dados das trocas do endpoint
async function carregarDadosTrocas() {
  try {
    const resposta = await fetch("http://localhost:4567/trocas"); // URL do endpoint listarTrocas
    if (!resposta.ok) {
      throw new Error("Erro ao carregar os dados das trocas");
    }
    const trocas = await resposta.json(); // Converte a resposta em JSON
    trocasCarregadas = trocas; // Armazena as trocas na variável global
    carregarTrocas(trocas); // Chama a função para renderizar as trocas
  } catch (erro) {
    console.error("Erro ao carregar os dados das trocas:", erro);
    const listaTrocas = document.querySelector(".book-list");
    listaTrocas.innerHTML =
      '<p class="error-message">Não foi possível carregar as trocas no momento. Tente novamente mais tarde.</p>';
  }
}

// Função para carregar as trocas na lista
function carregarTrocas(filtradas = []) {
  const listaTrocas = document.querySelector(".book-list");
  listaTrocas.innerHTML = ""; // Limpa a lista antes de adicionar novas trocas

  const trocasParaMostrar = filtradas.length > 0 ? filtradas : trocasCarregadas;

  trocasParaMostrar.forEach((troca) => {
    const itemTroca = document.createElement("li");
    itemTroca.className = "book-item";

    // Adiciona o conteúdo da troca usando template literals corretos
    itemTroca.innerHTML = `
      <div class="book-exchange">
        <div class="book-section">
          <h4>Livro Enviado</h4>
          <img src="${troca.livroEnviado.imagem}" alt="Imagem do livro ${
      troca.livroEnviado.titulo
    }" class="book-image">
          <h3>${troca.livroEnviado.titulo}</h3>
          <p>${troca.livroEnviado.genero}</p>
          <p class="description">Descrição: ${troca.livroEnviado.sinopse}</p>
        </div>
        <div class="book-section">
          <h4>Livro Recebido</h4>
          <img src="${troca.livroRecebido.imagem}" alt="Imagem do livro ${
      troca.livroRecebido.titulo
    }" class="book-image">
          <h3>${troca.livroRecebido.titulo}</h3>
          <p>${troca.livroRecebido.genero}</p>
          <p class="description">Descrição: ${troca.livroRecebido.sinopse}</p>
        </div>
      </div>
      <div class="exchange-info">
        <p><strong>Ofertante:</strong> ${troca.usuarioOfertante.nome} (${
      troca.usuarioOfertante.email
    })</p>
        <p><strong>Contemplado:</strong> ${troca.usuarioContemplado.nome} (${
      troca.usuarioContemplado.email
    })</p>
        <p class="status"><strong>Status:</strong> ${troca.status}</p>
        <p class="exchange-date">Data da troca: ${
          troca.dataTroca || "Não informada"
        }</p>
      </div>
    `;

    // Evento para abrir o modal com informações detalhadas do livro enviado
    itemTroca
      .querySelector(".book-section img")
      .addEventListener("click", () => abrirModal(troca.livroEnviado));

    // Evento para abrir o modal com informações detalhadas do livro recebido
    itemTroca
      .querySelectorAll(".book-section img")[1]
      .addEventListener("click", () => abrirModal(troca.livroRecebido));

    // Adiciona a troca à lista
    listaTrocas.appendChild(itemTroca);
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
  modal.style.display = "block";

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

// Função para adicionar o livro à lista de favoritos
function addToMyList(livro) {
  const myList = JSON.parse(localStorage.getItem("myList")) || [];
  if (!myList.some((b) => b.titulo === livro.titulo)) {
    myList.push(livro);
    localStorage.setItem("myList", JSON.stringify(myList));
    alert("Livro adicionado à lista de favoritos!");
  } else {
    alert("Livro já está na lista de favoritos.");
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
    const precoNumerico = parseFloat(
      livro.preco.replace("R$", "").replace(",", ".")
    );
    const precoFiltra = preco ? filtrarPorPreco(precoNumerico, preco) : true;

    return categoriaFiltra && precoFiltra;
  });

  carregarLivros(filtrados); // Atualiza a exibição com os livros filtrados
  fecharModalFiltros();
}

// Função para filtrar por faixa de preço
function filtrarPorPreco(precoLivro, faixaPreco) {
  switch (faixaPreco) {
    case "10-49.99":
      return precoLivro >= 10 && precoLivro <= 49.99;
    case "50-99.99":
      return precoLivro >= 50 && precoLivro <= 99.99;
    case "100-500":
      return precoLivro >= 100 && precoLivro <= 500;
    case "501+":
      return precoLivro > 501;
    default:
      return true;
  }
}

// Função para buscar livros por título
function buscarPorTitulo(query) {
  const filtrados = livrosCarregados.filter((livro) =>
    livro.titulo.toLowerCase().includes(query.toLowerCase())
  );
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
document
  .getElementById("search-input")
  .addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      document.getElementById("search-button").click();
    }
  });

// Eventos de abertura e fechamento do modal de filtros
document
  .querySelector(".filter-btn")
  .addEventListener("click", abrirModalFiltros);
document
  .getElementById("closeFilter")
  .addEventListener("click", fecharModalFiltros);

// Evento de submissão do formulário de filtros
document
  .getElementById("filterForm")
  .addEventListener("submit", aplicarFiltros);

// Executa o carregamento dos livros ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
  carregarDadosTrocas();
});
