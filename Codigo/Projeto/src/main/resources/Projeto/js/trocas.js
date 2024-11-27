let userBooks = [];

let trocaData = {
  idLivroEnviado: null,
  idLivroRecebido: null,
  usuario_ofertante: null,
  usuario_contemplado: null,
  status: "Concluido",
};

getUserBooks(localStorage.getItem("userId"));

let selectedBookSend = null;

document.addEventListener("DOMContentLoaded", function () {
  let bookList = localStorage.getItem("myList");
  const userId = localStorage.getItem("userId");

  if (userId) {
    getTrocaHistory(userId);
  } else {
    console.error("Nenhum usuário autenticado encontrado.");
  }

  loadSelectedBooks(bookList);

  getUserDetails(userBooks[0].id_usuario);
});

function loadSelectedBooks(bookList) {
  if (bookList) {
    // Converte a string JSON de volta para um array de objetos
    bookList = JSON.parse(bookList);

    userBooks = bookList;

    const selectWrapper = document.querySelector(".chooseBookSelect");

    selectWrapper.innerHTML = ""; // Limpa a lista de opções

    // Adiciona uma opção para cada livro na lista
    bookList.forEach(function (book) {
      const option = document.createElement("option");
      option.value = book.id; // Definindo o valor da opção como o nome do livro
      option.textContent = book.titulo; // Definindo o texto da opção como o nome do livro
      selectWrapper.appendChild(option);
    });
  } else {
    console.error("Nenhum livro foi selecionado no site para iniciar a troca.");
  }
}

async function getUserDetails(userId) {
  try {
    const response = await fetch(`http://localhost:4567/usuario/${userId}`);
    if (response.ok) {
      const data = await response.json();
      document.querySelector(
        ".userInformations"
      ).textContent = `${data.nome} - ${data.email}`;
    } else {
      console.error(
        "Erro ao buscar usuário:",
        response.status,
        response.statusText
      );
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
  }
}

document
  .querySelector(".chooseBookSelect")
  .addEventListener("change", function (event) {
    const bookId = event.target.value;
    const book = userBooks.find((book) => book.id === parseInt(bookId));
    getUserDetails(book.id_usuario);
  });

document
  .querySelector(".exchangeButton")
  .addEventListener("click", async function () {
    const bookId = document.querySelector(".chooseBookSelect").value;
    const book = userBooks.find((book) => book.id === parseInt(bookId));

    console.log(bookId, userBooks);

    trocaData.idLivroRecebido = book.id;
    trocaData.idLivroEnviado = selectedBookSend.idLivro;
    trocaData.usuario_ofertante = book.id_usuario;
    trocaData.usuario_contemplado = parseInt(localStorage.getItem("userId"));

    console.log(trocaData);

    if (
      trocaData.usuario_ofertante === parseInt(localStorage.getItem("userId"))
    ) {
      alert("Você não pode trocar um livro com você mesmo.");
      return;
    }
    if (localStorage.getItem("userId") === null) {
      alert("Você precisa estar logado para realizar uma troca.");
      return;
    }

    if (
      !trocaData.idLivroEnviado ||
      !trocaData.idLivroRecebido ||
      !trocaData.usuario_ofertante ||
      !trocaData.usuario_contemplado
    ) {
      alert("Selecione um livro para trocar.");
      return;
    }

    try {
      const response = await fetch("http://localhost:4567/troca", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(trocaData),
      });
      if (response.ok) {
        alert("Troca realizada com sucesso!");

        // Atualiza o localStorage para refletir a mudança no id_usuario
        let myBooks = JSON.parse(localStorage.getItem("myList")) || [];

        // Remover o livro enviado do localStorage
        myBooks = myBooks.filter(
          (book) => book.id !== trocaData.idLivroRecebido
        );

        const updatedBooks = myBooks.filter(
          (book) => book.id !== trocaData.idLivroRecebido
        );

        // Salvar a lista atualizada no localStorage
        localStorage.setItem("myList", JSON.stringify(updatedBooks));

        // Atualizar histórico de trocas
        getTrocaHistory(localStorage.getItem("userId"));
        getUserBooks(localStorage.getItem("userId"));
        loadSelectedBooks(localStorage.getItem("myList"));
      } else {
        alert("Erro ao realizar troca:", response.status, response.statusText);
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
    }
  });

async function getUserBooks(userId) {
  try {
    const response = await fetch(
      `http://localhost:4567/livros/usuario/${userId}`
    );
    if (response.ok) {
      const data = await response.json();
      let fetchedUserBooks = data;

      const selectWrapper = document.querySelector(".chooseMyBookSelect");

      selectWrapper.innerHTML = ""; // Limpar opções preesixtentes;

      fetchedUserBooks.forEach(function (book) {
        const option = document.createElement("option");
        option.value = book.idLivro;
        option.textContent = book.titulo;
        selectWrapper.appendChild(option);
      });

      selectedBookSend = fetchedUserBooks[0];

      selectWrapper.addEventListener("change", function () {
        const selectedBookId = parseInt(selectWrapper.value);
        selectedBookSend = fetchedUserBooks.find(
          (book) => book.idLivro === selectedBookId
        );
      });
    } else {
      console.error(
        "Erro ao buscar livros do usuário:",
        response.status,
        response.statusText
      );
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
  }
}

async function getTrocaHistory(userId) {
  try {
    const response = await fetch(
      `http://localhost:4567/trocas/usuario/${userId}`
    );
    if (response.ok) {
      const data = await response.json();
      renderTrocaHistory(data);
    } else {
      console.error(
        "Erro ao buscar histórico de trocas:",
        response.status,
        response.statusText
      );
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
  }
}

function renderTrocaHistory(trocaHistory) {
  const historyContainer = document.querySelector(".exchangeHistory > div");

  historyContainer.innerHTML = ""; // Clear existing history

  if (trocaHistory.length === 0) {
    historyContainer.innerHTML = "<p>Nenhuma troca realizada.</p>";
    return;
  }

  // Create a table structure
  const table = document.createElement("table");
  table.classList.add("trocaTable");
  table.style.overflow = "auto";
  table.style.height = "200px";
  table.innerHTML = `
            <thead>
                <tr>
                    <th><i class="fa-solid fa-id-card"></i> ID da Troca</th>
                    <th><i class="fa-solid fa-book"></i> Livro Recebido</th>
                    <th><i class="fa-solid fa-book"></i> Livro Enviado</th>
                    <th><i class="fa-solid fa-user"></i> Ofertante</th>
                    <th><i class="fa-solid fa-user-check"></i> Contemplado</th>
                    <th><i class="fa-solid fa-check-circle"></i> Status</th>
                </tr>
            </thead>
            <tbody></tbody>
        `;

  const tbody = table.querySelector("tbody");

  trocaHistory.forEach((troca) => {
    const row = document.createElement("tr");
    row.classList.add("trocaRow");

    row.innerHTML = `
                <td>${troca.idTroca}</td>
                <td><i class="fa-solid fa-book"></i> ${troca.livroRecebido.titulo}</td>
                <td><i class="fa-solid fa-book"></i> ${troca.livroEnviado.titulo}</td>
                <td>${troca.usuarioOfertante.nome} - ${troca.usuarioOfertante.email}</td>
                <td>${troca.usuarioContemplado.nome} - ${troca.usuarioContemplado.email}</td>
                <td>${troca.status}</td>
            `;

    tbody.appendChild(row);
  });

  historyContainer.appendChild(table);
}

// IA
document.getElementById("analyzeImageButton").addEventListener("click", async function () {
  const fileInput = document.getElementById("book-image");
  const resultDiv = document.getElementById("imageAnalysisResult");

  if (!fileInput.files || fileInput.files.length === 0) {
      alert("Por favor, selecione uma imagem para analisar.");
      return;
  }

  const file = fileInput.files[0];

  // Crie um FormData para enviar a imagem
  const formData = new FormData();
  formData.append("file", file);

  try {
      const response = await fetch("http://localhost:4567/analyze-image", {
          method: "POST",
          body: formData,
      });

      if (response.ok) {
          const data = await response.text();
          resultDiv.innerText = data;

          if (data.includes("Livro encontrado:")) {
              alert("Livro identificado e disponível para troca!");
          } else {
              alert("O livro não foi encontrado no banco de dados.");
          }
      } else {
          console.error("Erro ao analisar a imagem:", response.status, response.statusText);
          resultDiv.innerText = "Erro ao processar a imagem.";
      }
  } catch (error) {
      console.error("Erro na requisição:", error);
      resultDiv.innerText = "Erro ao processar a imagem.";
  }
});
