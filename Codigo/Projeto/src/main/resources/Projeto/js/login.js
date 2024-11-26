function logarUsuario() {
  const email = document.getElementById("emailLogin").value.trim();
  const senha = document.getElementById("senhaLogin").value.trim();

  // Validação dos campos
  if (!email || !senha) {
    alert("Por favor, preencha todos os campos.");
    return; // Sai da função se algum campo estiver vazio
  }

  // Mostra os dados enviados para depuração
  console.log("Dados enviados:", { email, senha });

  fetch("http://localhost:4567/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, senha }),
  })
    .then((response) => {
      if (!response.ok) {
        return response.json().then((errData) => {
          throw new Error(
            errData.mensagem || "Erro desconhecido ao fazer login."
          );
        });
      }
      return response.json();
    })
    .then((data) => {
      localStorage.setItem("userId", data.userId);
      alert(data.mensagem);
      window.location.href = "../pages/inicio.html";
    })
    .catch((error) => {
      console.error("Erro:", error);
      alert("Erro: " + error.message);
    });
}
