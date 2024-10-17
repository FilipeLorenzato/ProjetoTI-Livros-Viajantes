package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, Object> model = new HashMap<>();
            if (req.queryParams("erro") != null) {
                model.put("mensagemErro", "Email ou senha inválidos.");
            }
            return render(model, "login.html");
        });

        // Rota para processar o login
        post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String senha = req.queryParams("senha");

            Usuario usuario = usuarioService.autenticarUsuario(email, senha);

            if (usuario != null) {
                req.session(true).attribute("usuario", usuario);
                res.redirect("/inicio");
            } else {
                res.redirect("/login?erro=true");
            }
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
            Usuario usuario = new Usuario(nome, email, senha, dataNascimentoLocalDate, telefone, cidade, rua, estado, cep, String.valueOf(numero));
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
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
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
                res.redirect("/login");
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
     */
    private static String render(Map<String, Object> model, String templatePath) {
        if (model == null) {
            model = new HashMap<>();
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
