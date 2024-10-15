package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Historico;
import model.Inicio;
import model.Livro;
import model.MeusLivros;
import model.Usuario;
import service.EmAltaService;
import service.HistoricoService;
import service.InicioService;
import service.LivroService;
import service.MeusLivrosService;
import service.PostagemLivroService;
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
        EmAltaService emAltaService = new EmAltaService();
        PostagemLivroService postagemLivroService = new PostagemLivroService();
        HistoricoService historicoService = new HistoricoService();
        MeusLivrosService meusLivrosService = new MeusLivrosService();
        InicioService inicioService = new InicioService();

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
                // Armazena informações do usuário na sessão
                req.session(true).attribute("usuario", usuario);
                // Redireciona para a página inicial ou dashboard
                res.redirect("/inicio");
            } else {
                // Redireciona para a página de login com erro
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

            // Cria o endereço
            model.Endereco endereco = new model.Endereco(cidade, rua, estado, cep, String.valueOf(numero));

            // Cria o usuário
            LocalDate dataNascimentoLocalDate = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.Usuario usuario = new model.Usuario(nome, email, senha, dataNascimentoLocalDate, telefone, cidade, rua, estado, cep, numeroStr);
            boolean sucesso = usuarioService.cadastrarUsuario(usuario);

            if (sucesso) {
                // Redireciona para o login após cadastro bem-sucedido
                res.redirect("/login");
            } else {
                // Redireciona para uma página de erro
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
                // Redireciona para a página de listagem de livros
                res.redirect("/meusLivros");
            } else {
                // Redireciona para uma página de erro
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

            // Obtém os livros postados pelo usuário
            List<MeusLivros> meusLivros = meusLivrosService.getMeusLivros(usuario.getIdUsuario());

            Map<String, Object> model = new HashMap<>();
            model.put("meusLivros", meusLivros);
            model.put("usuario", usuario);
            return render(model, "meusLivros.html");
        });

        // ----------------- Rotas de "Em Alta" -----------------

        // Rota para exibir a página "Em Alta"
        get("/emAlta", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            // Define o limite de livros em alta
            int limite = 10;
            List<model.LivroEmAlta> livrosEmAlta = emAltaService.getLivrosEmAlta(limite);

            Map<String, Object> model = new HashMap<>();
            model.put("livrosEmAlta", livrosEmAlta);
            model.put("usuario", usuario);
            return render(model, "emAlta.html");
        });

        // ----------------- Rotas de "Postagem de Livros" -----------------

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
            String status = "Disponível"; // Status inicial

            // Cria o livro
            Livro livro = new Livro(titulo, autor, genero, sinopse);
            boolean livroInserido = livroService.cadastrarLivro(livro);

            if (!livroInserido) {
                res.redirect("/erro.html");
                return null;
            }

            // Define a data de postagem
            LocalDate dataPostagem = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = dataPostagem.format(formatter);

            // Cria a postagem de livro
            model.PostagemLivro postagemLivro = new model.PostagemLivro(livro, usuario, dataFormatada, status);
            boolean postagemInserida = postagemLivroService.cadastrarPostagemLivro(postagemLivro);

            if (postagemInserida) {
                // Redireciona para a página de "Meus Livros"
                res.redirect("/meusLivros");
            } else {
                // Redireciona para uma página de erro
                res.redirect("/erro.html");
            }

            return null;
        });

        // ----------------- Rotas de "Histórico" -----------------

        // Rota para exibir a página "Histórico"
        get("/historico", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            // Obtém o histórico de trocas do usuário
            List<Historico> historicos = historicoService.getHistoricoPorUsuario(usuario.getIdUsuario());

            Map<String, Object> model = new HashMap<>();
            model.put("historicos", historicos);
            model.put("usuario", usuario);
            return render(model, "historico.html");
        });

        // ----------------- Rotas de "Início" -----------------

        // Rota para exibir a página "Início"
        get("/inicio", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                res.redirect("/login");
                return null;
            }

            // Obtém as estatísticas de início do usuário
            Inicio estatisticas = inicioService.getEstatisticasInicio(usuario.getIdUsuario());

            Map<String, Object> model = new HashMap<>();
            model.put("estatisticas", estatisticas);
            model.put("usuario", usuario);
            return render(model, "inicio.html");
        });

        // ----------------- Filtros de Autenticação -----------------

        // Filtro para proteger rotas que exigem autenticação
        before((req, res) -> {
            String path = req.pathInfo();
            // Defina as rotas que não exigem autenticação
            List<String> openRoutes = Arrays.asList("/login", "/cadastroUsuario", "/erro.html", "/public/");

            boolean isOpen = openRoutes.stream().anyMatch(route -> path.startsWith(route));

            if (!isOpen) {
                Usuario usuario = req.session().attribute("usuario");
                if (usuario == null) {
                    res.redirect("/login");
                    halt();
                }
            }
        });
    }

    /**
     * Método auxiliar para renderizar templates HTML usando Velocity.
     * @param model Mapa de dados para o template.
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
