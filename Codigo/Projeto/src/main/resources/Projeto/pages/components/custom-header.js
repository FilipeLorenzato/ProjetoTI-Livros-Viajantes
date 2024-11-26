class CustomHeader extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });

    this.shadowRoot.innerHTML = `
            <link rel="stylesheet" href="../style/inicio.css">
            <header class="header">
                <div class="header-content">
                    <div class="logo-container">
                        <a href="inicio.html"><img src="img/logo1-removebg-preview.png" alt="Logo" class="logo"></a>
                    </div>
                    <nav class="header-nav">
                        <a href="comofunciona.html" class="header-button">Como Funciona</a>
                        <a href="postagem-livros.html" class="header-button">Catálogo de Livros</a>
                        <a href="trocas.html" class="header-button">Troque Agora</a>
                        <a href="meus-livros.html" class="header-button">Meus Livros</a>
                        <a href="emAlta.html" class="header-button">Em Alta</a>
                        <a href="historico.html" class="header-button">Historico de trocas</a>
                        <a href="login.html" class="header-button" id="login-button">Login</a>
                        <a href="CadastroUsuarios.html" class="header-button" id="register-button">Cadastrar</a>
                        <a href="cadastrar-livro.html" class="header-button">Registrar Novo Livro</a>
                        <a class="header-button" id="logout-button">Logout</a>
                    </nav>
                </div>
            </header>
        `;
  }

  connectedCallback() {
    this.checkUserStatus();
    this.shadowRoot
      .getElementById("logout-button")
      .addEventListener("click", () => this.logOutUser());
  }

  checkUserStatus() {
    const userId = localStorage.getItem("userId");
    const loginButton = this.shadowRoot.getElementById("login-button");
    const registerButton = this.shadowRoot.getElementById("register-button");
    const logoutButton = this.shadowRoot.getElementById("logout-button");

    if (userId) {
      if (loginButton) loginButton.style.display = "none";
      if (registerButton) registerButton.style.display = "none";
      if (logoutButton) logoutButton.style.display = "block";
    } else {
      if (loginButton) loginButton.style.display = "block";
      if (registerButton) registerButton.style.display = "block";
      if (logoutButton) logoutButton.style.display = "none";
    }
  }

  logOutUser() {
    localStorage.removeItem("userId");
    alert("Usuário deslogado com sucesso!");
    this.checkUserStatus();
  }
}

customElements.define("custom-header", CustomHeader);
