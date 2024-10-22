package app;

import service.LivroService;
import service.UsuarioService;
import static spark.Spark.*;

public class Main {

    private static UsuarioService usuarioService = new UsuarioService();
    private static LivroService livroService = new LivroService();

    public static void main(String[] args) {
        port(4567);

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
        post("/login", (request, response) -> usuarioService.login(request, response));

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
        
        // ----------------- Rotas "Em Alta" e "Histórico" -----------------

        // Listar livros "em alta"
        get("/livros/emAlta", (request, response) -> livroService.getLivrosEmAlta(response));

        // Listar histórico de livros postados por um usuário
        get("/livros/historico/:idUsuario", (request, response) -> livroService.getHistoricoLivrosUsuario(request, response));

    }
}


/*package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Endereco;
import model.Livro;
import model.Usuario;
import service.LivroService;
import service.UsuarioService;
import spark.ModelAndView;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.template.velocity.VelocityTemplateEngine;


public class Main {
    public static void main(String[] args) {
        // Configuração do servidor
        port(4567); // Porta padrão do Spark

        // Servir arquivos estáticos da pasta 'resources/public'
        staticFiles.location("/public");

        // Instanciar os serviços
        LivroService livroService = new LivroService();
        UsuarioService usuarioService = new UsuarioService();

        // ----------------- Rotas de Login -----------------

        // Rota para exibir o formulário de login
        get("/login", (req, res) -> {
        	System.out.println("estou a	qui 123445");
            Map<String, Object> model = new HashMap<>();
            if (req.queryParams("erro") != null) {
                model.put("mensagemErro", "Email ou senha inválidos.");
            }
            return render(model, "login.html");
        });

        // Rota para processar o login
        post("/login", (req, res) -> {
        	System.out.println("estou a	qui 123445");
            String email = req.queryParams("email");
            String senha = req.queryParams("senha");

            Usuario usuario = usuarioService.autenticarUsuario(email, senha);
            
            System.out.println(usuario);

            if (usuario != null) {
                req.session(true).attribute("usuario", usuario);
                res.redirect("/inicio");
            } else {
                res.redirect("/login?erro=true");
            }
            System.out.println(usuario);
            return null;
        });

        // Rota para logout
        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/login");
            return null;
        });

        // ----------------- Rotas de Cadastro de Usuário -----------------

        // Rota para exibir o formulário de cadastro de usuário
        get("/cadastroUsuario", (req, res) -> {
            return render(null, "cadastroUsuario.html");
        });

        // Rota para processar o cadastro de usuário
        post("/cadastroUsuario", (req, res) -> {
            String nome = req.queryParams("nome");
            String email = req.queryParams("email");
            String senha = req.queryParams("senha");
            String dataNascimento = req.queryParams("dataNascimento");
            String telefone = req.queryParams("telefone");
            String cidade = req.queryParams("cidade");
            String rua = req.queryParams("rua");
            String estado = req.queryParams("estado");
            String cep = req.queryParams("cep");
            String numeroStr = req.queryParams("numero");
            int numero = Integer.parseInt(numeroStr);

            LocalDate dataNascimentoLocalDate = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Endereco endereco = new Endereco(cidade, rua, estado, cep, numero);
            Usuario usuario = new Usuario(nome, email, senha, dataNascimentoLocalDate, telefone, endereco);
            boolean sucesso = usuarioService.cadastrarUsuario(usuario);

            if (sucesso) {
                res.redirect("/login");
            } else {
                res.redirect("/erro.html");
            }
            return null;
        });

        // ----------------- Rotas de Cadastro de Livro -----------------

        // Rota para exibir o formulário de cadastro de livros
        get("/cadastrarLivro", (req, res) -> {
            return render(null, "cadastrarLivro.html");
        });

        // Rota para processar o formulário de cadastro de livros
        post("/cadastrarLivro", (req, res) -> {
            String titulo = req.queryParams("titulo");
            String autor = req.queryParams("autor");
            String genero = req.queryParams("genero");
            String sinopse = req.queryParams("sinopse");

            Livro livro = new Livro(titulo, autor, genero, sinopse);
            boolean sucesso = livroService.cadastrarLivro(livro);

            if (sucesso) {
                res.redirect("/meusLivros");
            } else {
                res.redirect("/erro.html");
            }
            return null;
        });

        // Rota para listar os livros cadastrados
        get("/meusLivros", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            List<Livro> meusLivros = livroService.getLivrosPorUsuario(usuario.getIdUsuario());
            model.put("meusLivros", meusLivros);
            model.put("usuario", usuario);
            return render(model, "meusLivros.html");
        });

        // ----------------- Rota "Em Alta" -----------------

        get("/emAlta", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            int limite = 10;  // Número máximo de livros a exibir
            List<Livro> livrosEmAlta = livroService.getLivrosEmAlta(limite);

            Map<String, Object> model = new HashMap<>();
            model.put("livrosEmAlta", livrosEmAlta);
            model.put("usuario", usuario);
            return render(model, "emAlta.html");
        });

        // ----------------- Rotas "Postagem de Livros" -----------------

        // Rota para exibir o formulário de postagem de livros
        get("/postagemLivros", (req, res) -> {
        	
        	System.out.println("estou aqui");
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("usuario", usuario);
            return render(model, "postagemLivros.html");
        });

        // Rota para processar a postagem de livros
        post("/postagemLivros", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            String titulo = req.queryParams("titulo");
            String autor = req.queryParams("autor");
            String genero = req.queryParams("genero");
            String sinopse = req.queryParams("sinopse");

            Livro livro = new Livro(titulo, autor, genero, sinopse);
            boolean livroInserido = livroService.cadastrarLivro(livro);

            if (livroInserido) {
                res.redirect("/meusLivros");
            } else {
                res.redirect("/erro.html");
            }

            return null;
        });

        // ----------------- Rota "Histórico" -----------------

        get("/historico", (req, res) -> {
        	System.out.println("ESTOU AQUI");
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login.html");
                return null;
            }

            // Histórico de livros postados pelo usuário (simulado para usar com LivroService)
            List<Livro> historicoLivros = livroService.getLivrosPorUsuario(usuario.getIdUsuario());

            Map<String, Object> model = new HashMap<>();
            model.put("historicoLivros", historicoLivros);
            model.put("usuario", usuario);
            return render(model, "historico.html");
        });

        // ----------------- Rota "Início" -----------------

        get("/inicio", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login.html");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("usuario", usuario);
            return render(model, "inicio.html");
        });

        // ----------------- Filtros de Autenticação -----------------

        before((req, res) -> {
            String path = req.pathInfo();
            if (!path.equals("/login") && !path.equals("/cadastroUsuario") && req.session().attribute("usuario") == null) {
                res.redirect("/login");
                halt();
            }
        });
    }

    /**
     * Método auxiliar para renderizar templates HTML usando Velocity.
     * 
     * @param model        Mapa de dados para o template.
     * @param templatePath Caminho do template HTML.
     * @return Conteúdo renderizado.
     
    private static String render(Map<String, Object> model, String templatePath) {
        if (model == null) {
            model = new HashMap<>();
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
*/