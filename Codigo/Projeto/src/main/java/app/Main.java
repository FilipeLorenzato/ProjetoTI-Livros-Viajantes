package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.DAO;
import dao.LivroDAO;
import model.Livro;
import service.LivroService;
import service.UsuarioService; // Add this import statement
import spark.Spark;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class Main {

    private static UsuarioService usuarioService = new UsuarioService();
    private static LivroService livroService = new LivroService();
    private static LivroDAO livroDAO = new LivroDAO();

    private static Connection connDAO;

    public static void main(String[] args) {
        // Initialize the connection
        connDAO = DAO.conectar();
        port(4567);
        // staticFiles.location("../Projeto");
        Spark.staticFiles.location("/public");

        // Configurar CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // ----------------- Rotas de Usuário -----------------

        // Criar novo usuário (Cadastro)
        post("/usuario", (request, response) -> usuarioService.cadastrarUsuario(request, response));

        // Obter usuário por ID
        get("/usuario/:id", (request, response) -> usuarioService.getUsuarioById(request, response));

        // Atualizar usuário
        put("/usuario/:id", (request, response) -> usuarioService.atualizarUsuario(request, response));

        // Deletar usuário
        delete("/usuario/:id", (request, response) -> usuarioService.deletarUsuario(request, response));

        // Listar todos os usuários
        get("/usuarios", (request, response) -> usuarioService.listarTodosUsuarios(response));

        // Autenticação (Login)
        // Exemplo de resposta no back-end (Java com Spark)
        post("/login", (request, response) -> {
            response.type("application/json");
            String json = request.body();

            // Converte o JSON em um objeto
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

            String email = jsonObject.get("email").getAsString();
            String senha = jsonObject.get("senha").getAsString();

            // Valida se o email e a senha estão presentes
            if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
                response.status(400); // Bad Request
                return "{\"mensagem\":\"Email e senha devem ser fornecidos.\"}";
            }

            // Chama a função de validação do usuário
            if (validaUsuario(email, senha, connDAO)) {
                response.status(200); // OK
                return "{\"mensagem\":\"Login bem-sucedido!\"}";
            } else {
                response.status(401); // Unauthorized
                return "{\"mensagem\":\"Credenciais inválidas.\"}";
            }
        });

        // ----------------- Rotas de Livros -----------------

        // Cadastrar novo livro
        post("/livro", (request, response) -> livroService.cadastrarLivro(request, response));

        // Obter livro por ID
        get("/livro/:id", (request, response) -> livroService.getLivroById(request, response));

        // Atualizar livro
        put("/livro/:id", (request, response) -> livroService.updateLivro(request, response));

        // Deletar livro
        delete("/livro/:id", (request, response) -> livroService.deletarLivro(request, response));

        // Listar todos os livros
        get("/livros", (request, response) -> livroService.listarTodosLivros(response));

             // Rota para obter a lista de livros
        get("/meus-livros", (request, response) -> {
            response.type("application/json");
            // Chame um método do seu DAO para buscar os livros
            List<Livro> livros = livroDAO.buscarTodosLivros(); // Exemplo
            return new Gson().toJson(livros); // Converte a lista em JSON
        });


        post("/cadastrar-livro", (req, res) -> {
            String body = req.body();
            Livro livro = new Gson().fromJson(body, Livro.class);
            // Salvar livro no banco de dados
            livroService.cadastrarLivro(req, res);
            res.status(201); // Código de sucesso
            return "Livro cadastrado com sucesso!";
        });
    
        // ----------------- Rotas "Em Alta" e "Histórico" -----------------

        // Listar livros "em alta"
        get("/livros/emAlta", (request, response) -> livroService.getLivrosEmAlta(response));

        // Listar histórico de livros postados por um usuário
        get("/livros/historico/:idUsuario",
                (request, response) -> livroService.getHistoricoLivrosUsuario(request, response));

    }

    private static boolean validaUsuario(String email, String senha, Connection conn) {
        try {
            String query = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

/*
 * package app;
 * 
 * import java.time.LocalDate;
 * import java.time.format.DateTimeFormatter;
 * import java.util.HashMap;
 * import java.util.List;
 * import java.util.Map;
 * 
 * import model.Endereco;
 * import model.Livro;
 * import model.Usuario;
 * import service.LivroService;
 * import service.UsuarioService;
 * import spark.ModelAndView;
 * import static spark.Spark.before;
 * import static spark.Spark.get;
 * import static spark.Spark.halt;
 * import static spark.Spark.port;
 * import static spark.Spark.post;
 * import static spark.Spark.staticFiles;
 * import spark.template.velocity.VelocityTemplateEngine;
 * 
 * 
 * public class Main {
 * public static void main(String[] args) {
 * // Configuração do servidor
 * port(4567); // Porta padrão do Spark
 * 
 * // Servir arquivos estáticos da pasta 'resources/public'
 * staticFiles.location("/public");
 * 
 * // Instanciar os serviços
 * LivroService livroService = new LivroService();
 * UsuarioService usuarioService = new UsuarioService();
 * 
 * // ----------------- Rotas de Login -----------------
 * 
 * // Rota para exibir o formulário de login
 * get("/login", (req, res) -> {
 * System.out.println("estou a	qui 123445");
 * Map<String, Object> model = new HashMap<>();
 * if (req.queryParams("erro") != null) {
 * model.put("mensagemErro", "Email ou senha inválidos.");
 * }
 * return render(model, "login.html");
 * });
 * 
 * // Rota para processar o login
 * post("/login", (req, res) -> {
 * System.out.println("estou a	qui 123445");
 * String email = req.queryParams("email");
 * String senha = req.queryParams("senha");
 * 
 * Usuario usuario = usuarioService.autenticarUsuario(email, senha);
 * 
 * System.out.println(usuario);
 * 
 * if (usuario != null) {
 * req.session(true).attribute("usuario", usuario);
 * res.redirect("/inicio");
 * } else {
 * res.redirect("/login?erro=true");
 * }
 * System.out.println(usuario);
 * return null;
 * });
 * 
 * // Rota para logout
 * get("/logout", (req, res) -> {
 * req.session().invalidate();
 * res.redirect("/login");
 * return null;
 * });
 * 
 * // ----------------- Rotas de Cadastro de Usuário -----------------
 * 
 * // Rota para exibir o formulário de cadastro de usuário
 * get("/cadastroUsuario", (req, res) -> {
 * return render(null, "cadastroUsuario.html");
 * });
 * 
 * // Rota para processar o cadastro de usuário
 * post("/cadastroUsuario", (req, res) -> {
 * String nome = req.queryParams("nome");
 * String email = req.queryParams("email");
 * String senha = req.queryParams("senha");
 * String dataNascimento = req.queryParams("dataNascimento");
 * String telefone = req.queryParams("telefone");
 * String cidade = req.queryParams("cidade");
 * String rua = req.queryParams("rua");
 * String estado = req.queryParams("estado");
 * String cep = req.queryParams("cep");
 * String numeroStr = req.queryParams("numero");
 * int numero = Integer.parseInt(numeroStr);
 * 
 * LocalDate dataNascimentoLocalDate = LocalDate.parse(dataNascimento,
 * DateTimeFormatter.ofPattern("yyyy-MM-dd"));
 * Endereco endereco = new Endereco(cidade, rua, estado, cep, numero);
 * Usuario usuario = new Usuario(nome, email, senha, dataNascimentoLocalDate,
 * telefone, endereco);
 * boolean sucesso = usuarioService.cadastrarUsuario(usuario);
 * 
 * if (sucesso) {
 * res.redirect("/login");
 * } else {
 * res.redirect("/erro.html");
 * }
 * return null;
 * });
 * 
 * // ----------------- Rotas de Cadastro de Livro -----------------
 * 
 * // Rota para exibir o formulário de cadastro de livros
 * get("/cadastrarLivro", (req, res) -> {
 * return render(null, "cadastrarLivro.html");
 * });
 * 
 * // Rota para processar o formulário de cadastro de livros
 * post("/cadastrarLivro", (req, res) -> {
 * String titulo = req.queryParams("titulo");
 * String autor = req.queryParams("autor");
 * String genero = req.queryParams("genero");
 * String sinopse = req.queryParams("sinopse");
 * 
 * Livro livro = new Livro(titulo, autor, genero, sinopse);
 * boolean sucesso = livroService.cadastrarLivro(livro);
 * 
 * if (sucesso) {
 * res.redirect("/meusLivros");
 * } else {
 * res.redirect("/erro.html");
 * }
 * return null;
 * });
 * 
 * // Rota para listar os livros cadastrados
 * get("/meusLivros", (req, res) -> {
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login");
 * return null;
 * }
 * 
 * Map<String, Object> model = new HashMap<>();
 * List<Livro> meusLivros =
 * livroService.getLivrosPorUsuario(usuario.getIdUsuario());
 * model.put("meusLivros", meusLivros);
 * model.put("usuario", usuario);
 * return render(model, "meusLivros.html");
 * });
 * 
 * // ----------------- Rota "Em Alta" -----------------
 * 
 * get("/emAlta", (req, res) -> {
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login");
 * return null;
 * }
 * 
 * int limite = 10; // Número máximo de livros a exibir
 * List<Livro> livrosEmAlta = livroService.getLivrosEmAlta(limite);
 * 
 * Map<String, Object> model = new HashMap<>();
 * model.put("livrosEmAlta", livrosEmAlta);
 * model.put("usuario", usuario);
 * return render(model, "emAlta.html");
 * });
 * 
 * // ----------------- Rotas "Postagem de Livros" -----------------
 * 
 * // Rota para exibir o formulário de postagem de livros
 * get("/postagemLivros", (req, res) -> {
 * 
 * System.out.println("estou aqui");
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login");
 * return null;
 * }
 * 
 * Map<String, Object> model = new HashMap<>();
 * model.put("usuario", usuario);
 * return render(model, "postagemLivros.html");
 * });
 * 
 * // Rota para processar a postagem de livros
 * post("/postagemLivros", (req, res) -> {
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login");
 * return null;
 * }
 * 
 * String titulo = req.queryParams("titulo");
 * String autor = req.queryParams("autor");
 * String genero = req.queryParams("genero");
 * String sinopse = req.queryParams("sinopse");
 * 
 * Livro livro = new Livro(titulo, autor, genero, sinopse);
 * boolean livroInserido = livroService.cadastrarLivro(livro);
 * 
 * if (livroInserido) {
 * res.redirect("/meusLivros");
 * } else {
 * res.redirect("/erro.html");
 * }
 * 
 * return null;
 * });
 * 
 * // ----------------- Rota "Histórico" -----------------
 * 
 * get("/historico", (req, res) -> {
 * System.out.println("ESTOU AQUI");
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login.html");
 * return null;
 * }
 * 
 * // Histórico de livros postados pelo usuário (simulado para usar com
 * LivroService)
 * List<Livro> historicoLivros =
 * livroService.getLivrosPorUsuario(usuario.getIdUsuario());
 * 
 * Map<String, Object> model = new HashMap<>();
 * model.put("historicoLivros", historicoLivros);
 * model.put("usuario", usuario);
 * return render(model, "historico.html");
 * });
 * 
 * // ----------------- Rota "Início" -----------------
 * 
 * get("/inicio", (req, res) -> {
 * Usuario usuario = req.session().attribute("usuario");
 * if (usuario == null) {
 * res.redirect("/login.html");
 * return null;
 * }
 * 
 * Map<String, Object> model = new HashMap<>();
 * model.put("usuario", usuario);
 * return render(model, "inicio.html");
 * });
 * 
 * // ----------------- Filtros de Autenticação -----------------
 * 
 * before((req, res) -> {
 * String path = req.pathInfo();
 * if (!path.equals("/login") && !path.equals("/cadastroUsuario") &&
 * req.session().attribute("usuario") == null) {
 * res.redirect("/login");
 * halt();
 * }
 * });
 * }
 * 
 * /**
 * Método auxiliar para renderizar templates HTML usando Velocity.
 * 
 * @param model Mapa de dados para o template.
 * 
 * @param templatePath Caminho do template HTML.
 * 
 * @return Conteúdo renderizado.
 * 
 * private static String render(Map<String, Object> model, String templatePath)
 * {
 * if (model == null) {
 * model = new HashMap<>();
 * }
 * return new VelocityTemplateEngine().render(new ModelAndView(model,
 * templatePath));
 * }
 * }
 */