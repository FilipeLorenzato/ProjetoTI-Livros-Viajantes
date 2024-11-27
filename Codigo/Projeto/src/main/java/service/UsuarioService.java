package service;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {

    private UsuarioDAO usuarioDao;

    public UsuarioService() {
        usuarioDao = new UsuarioDAO();
        usuarioDao.conectar();
    }

    // Inserir novo usuário
    public String inserir(Request request, Response response) {
        String resultado = "Erro ao criar usuário";
        response.status(400); // Bad Request

        // Validação dos parâmetros obrigatórios
        String nome = request.queryParams("nome");
        String email = request.queryParams("email");
        String senha = request.queryParams("senha");
        String telefone = request.queryParams("telefone");
        String rua = request.queryParams("rua");
        String cidade = request.queryParams("cidade");
        String estado = request.queryParams("estado");
        String cep = request.queryParams("cep");

        if (nome == null || email == null || senha == null || telefone == null
                || rua == null || cidade == null
                || estado == null || cep == null) {
            return "Parâmetros obrigatórios ausentes!";
        }

        // Criação de um novo usuário com os parâmetros fornecidos
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTelefone(telefone);
        usuario.setrua(rua);

        boolean sucesso = usuarioDao.inserirUsuario(usuario);
        if (sucesso) {
            resultado = "Usuário " + usuario.getNome() + " criado com sucesso!";
            response.status(201); // Created
        } else {
            resultado = "Falha ao criar usuário";
        }

        return resultado;
    }

    // Método para cadastrar um novo usuário
    public Object cadastrarUsuario(Request request, Response response) {
        try {
            // Extrai o corpo da requisição como JSON
            JSONObject jsonBody = new JSONObject(request.body());

            // Obtem os parâmetros necessários
            String nome = jsonBody.getString("nome");
            String email = jsonBody.getString("email");
            String senha = jsonBody.getString("senha");
            String telefone = jsonBody.getString("telefone");
            String rua = jsonBody.getString("rua");
            String cidade = jsonBody.getString("cidade");
            String estado = jsonBody.getString("estado");
            String cep = jsonBody.getString("cep");

            // Valida se os parâmetros não são nulos ou vazios
            if (nome == null || email == null || senha == null || telefone == null
                    || rua == null || cidade == null || estado == null || cep == null) {
                response.status(400);
                return "Parâmetros obrigatórios ausentes!";
            }

            // Criação de um novo usuário
            Usuario usuario = new Usuario(nome, email, senha, telefone, rua, cidade, estado, cep);

            // Insere o usuário no banco de dados
            boolean sucesso = usuarioDao.inserirUsuario(usuario);

            if (sucesso) {
                response.status(201); // Created
                return "Usuário criado com sucesso!";
            } else {
                response.status(500);
                return "Falha ao criar usuário";
            }
        } catch (Exception e) {
            response.status(500);
            return "Erro ao cadastrar usuário: " + e.getMessage();
        }
    }

    // Método login
    public Object login(Request request, Response response) {
        String body = request.body();
        JsonObject jsonRequest = new Gson().fromJson(body, JsonObject.class);

        // Obtendo os parâmetros de entrada do request
        String email = jsonRequest.get("email").getAsString();
        String senha = jsonRequest.get("senha").getAsString();

        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);

        // Verificando se os parâmetros não são nulos
        if (email == null || senha == null) {
            response.status(400); // Bad Request
            return "{\"mensagem\": \"Email e senha devem ser fornecidos.\"}"; // Resposta JSON
        }

        // Buscando o usuário pelo email
        Usuario usuario = usuarioDao.buscarPorEmail(email);

        // Verificando se o usuário existe e se a senha está correta
        if (usuario != null && usuario.getSenha().equals(senha)) {
            response.status(200); // OK
            // Retorna o userId junto com a mensagem de sucesso em JSON
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("userId", usuario.getIdUsuario());
            jsonResponse.put("mensagem", "Login realizado com sucesso.");
            return jsonResponse.toString();
        } else {
            response.status(401); // Unauthorized
            return "{\"mensagem\": \"Email ou senha inválidos.\"}"; // Resposta JSON
        }
    }

    // Método para buscar um usuário por email
    public Usuario buscarUsuarioPorEmail(Request request, Response response) {
        try {
            String requestBody = request.body();
            JSONObject jsonBody = new JSONObject(requestBody);

            if (jsonBody.has("email")) {
                String email = jsonBody.getString("email");
                Usuario usuario = usuarioDao.getByEmail(email);

                if (usuario != null) {
                    response.header("Content-Type", "application/xml");
                    response.header("Content-Encoding", "UTF-8");
                    return usuario;
                } else {
                    response.status(404); // 404 Not found
                    return null;
                }
            } else {
                response.status(400); // Bad Request
                return null;
            }
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            return null;
        }
    }

    public Object getUsuarioById(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Usuario usuario = usuarioDao.getById(id);

            if (usuario != null) {
                // Construir manualmente a string JSON
                String jsonResponse = "{"
                        + "\"nome\":\"" + usuario.getNome() + "\","
                        + "\"email\":\"" + usuario.getEmail() + "\","
                        + "\"telefone\":\"" + usuario.getTelefone() + "\","
                        + "\"rua\":\"" + usuario.getrua() + "\","
                        + "\"cidade\":\"" + usuario.getCidade() + "\","
                        + "\"estado\":\"" + usuario.getEstado() + "\","
                        + "\"cep\":\"" + usuario.getCep() + "\""
                        + "}";

                response.type("application/json");
                return jsonResponse;
            } else {
                response.status(404); // 404 Not Found
                return "Usuário não encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400); // Bad Request
            return "ID inválido.";
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            e.printStackTrace();
            return "Erro ao processar a solicitação.";
        }
    }

    // Método para atualizar os dados de um usuário
    public Object atualizarUsuario(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDao.getById(id);
        if (usuario != null) {
            try {
                JSONObject jsonBody = new JSONObject(request.body());
                usuario.setNome(jsonBody.getString("nome"));
                usuario.setEmail(jsonBody.getString("email"));
                usuario.setTelefone(jsonBody.getString("telefone"));
                usuario.setrua(jsonBody.getString("rua"));
                usuario.setCidade(jsonBody.getString("cidade"));
                usuario.setEstado(jsonBody.getString("estado"));
                usuario.setCep(jsonBody.getString("cep"));

                if (jsonBody.has("senha") && !jsonBody.getString("senha").isEmpty()) {
                    usuario.setSenha(jsonBody.getString("senha"));
                }

                usuarioDao.atualizarUsuario(usuario);
                return id;
            } catch (Exception e) {
                response.status(400); // Bad Request
                return "Erro ao processar a requisição: " + e.getMessage();
            }
        } else {
            response.status(404); // Not Found
            return "Usuário não encontrado.";
        }
    }

    // Método para deletar um usuário
    public Object deletarUsuario(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDao.getById(id);
        if (usuario != null) {
            usuarioDao.excluirUsuario(id);
            response.status(200); // success
            return id;
        } else {
            response.status(404); // 404 Not found
            return "Usuário não encontrado.";
        }
    }

    // Método para listar todos os usuários
    public Object listarTodosUsuarios(Response response) {
        StringBuffer returnValue = new StringBuffer("<usuarios type=\"array\">");
        for (Usuario usuario : usuarioDao.getAll()) {
            returnValue.append("\n<usuario>\n"
                    + "\t<nome>" + usuario.getNome() + "</nome>\n"
                    + "\t<email>" + usuario.getEmail() + "</email>\n"
                    + "\t<telefone>" + usuario.getTelefone() + "</telefone>\n"
                    + "\t<rua>" + usuario.getrua() + "</rua>\n"
                    + "\t<cidade>" + usuario.getCidade() + "</cidade>\n"
                    + "\t<estado>" + usuario.getEstado() + "</estado>\n"
                    + "\t<cep>" + usuario.getCep() + "</cep>\n"
                    + "</usuario>\n");
        }
        returnValue.append("</usuarios>");
        response.header("Content-Type", "application/xml");
        response.header("Content-Encoding", "UTF-8");
        return returnValue.toString();
    }
}

/*
 * package service;
 * 
 * import java.sql.Connection;
 * import java.sql.Date;
 * import java.sql.PreparedStatement;
 * import java.sql.SQLException;
 * 
 * import dao.DAO;
 * import dao.UsuarioDAO;
 * import model.rua;
 * import model.Usuario;
 * import spark.Request;
 * import spark.Response;
 * 
 * public class UsuarioService {
 * 
 * // Inserir novo usuário
 * public static String inserir(Request request, Response response) {
 * String resultado = "Erro ao criar usuário";
 * response.status(400); // Bad Request
 * 
 * // Validação dos parâmetros obrigatórios
 * String email = request.queryParams("email");
 * String senha = request.queryParams("senha");
 * String nome = request.queryParams("nome");
 * String telefone = request.queryParams("telefone");
 * String rua = request.queryParams("rua");
 * String cidade = request.queryParams("cidade");
 * String estado = request.queryParams("estado");
 * String cep = request.queryParams("cep");
 * 
 * if (email == null || senha == null || nome == null || telefone == null ||
 * rua == null || cidade == null
 * || estado == null || cep == null) {
 * return "Parâmetros obrigatórios ausentes!";
 * }
 * 
 * // Criação de um novo endereço com os parâmetros fornecidos
 * rua ruaObj = new rua();
 * ruaObj.setrua(rua);
 * ruaObj.setCidade(cidade);
 * ruaObj.setEstado(estado);
 * ruaObj.setCep(cep);
 * 
 * // Criação de um novo usuário com os parâmetros fornecidos
 * Usuario usuario = new Usuario();
 * usuario.setEmail(email);
 * usuario.setSenha(senha);
 * usuario.setNome(nome);
 * usuario.setTelefone(telefone);
 * usuario.setrua(ruaObj);
 * 
 * boolean sucesso = UsuarioDAO.inserir(usuario);
 * if (sucesso) {
 * resultado = "Usuário " + usuario.getNome() + " criado com sucesso!";
 * response.status(201); // Created
 * } else {
 * resultado = "Falha ao criar usuário";
 * }
 * 
 * return resultado;
 * }
 * 
 * // Login do usuário
 * public static String login(Request request, Response response) {
 * String resultado = "Erro de login";
 * response.status(400); // Bad Request
 * 
 * String email = request.queryParams("email");
 * String senha = request.queryParams("senha");
 * 
 * if (email == null || senha == null) {
 * return "Parâmetros obrigatórios ausentes!";
 * }
 * 
 * Usuario usuarioBanco = UsuarioDAO.procurarPorEmail(email);
 * if (usuarioBanco != null && senha.equals(usuarioBanco.getSenha())) {
 * resultado = "{\"idUsuario\":" + usuarioBanco.getId() + ",\"nome\":\"" +
 * usuarioBanco.getNome() + "\"}";
 * response.status(200); // OK
 * } else {
 * resultado = "Usuário ou senha incorretos";
 * response.status(404); // Not Found
 * }
 * 
 * return resultado;
 * }
 * 
 * // Buscar dados de perfil de um usuário
 * public static String getPerfilData(Request request, Response response) {
 * String resultado = "Erro ao buscar dados do perfil";
 * response.status(400); // Bad Request
 * 
 * try {
 * int id = Integer.parseInt(request.queryParams("id"));
 * Usuario usuario = UsuarioDAO.procurarPorId(id);
 * 
 * if (usuario != null) {
 * resultado = usuario.getPerfilData(); // Supondo que esse método existe no
 * modelo
 * response.status(200); // OK
 * } else {
 * resultado = "Usuário não encontrado";
 * response.status(404); // Not Found
 * }
 * } catch (NumberFormatException e) {
 * resultado = "ID inválido";
 * }
 * 
 * return resultado;
 * }
 * 
 * public Usuario autenticarUsuario(String email, String senha) {
 * // Chame o DAO para buscar o usuário pelo email
 * UsuarioDAO usuarioDAO = new UsuarioDAO();
 * Usuario usuario = null;
 * try {
 * usuario = usuarioDAO.buscarPorEmail(email);
 * } catch (SQLException e) {
 * e.printStackTrace();
 * // Handle the exception as needed, e.g., log it or rethrow it
 * }
 * 
 * // Verifique se o usuário foi encontrado e se a senha fornecida é a correta
 * if (usuario != null && usuario.getSenha().equals(senha)) {
 * // Autenticação bem-sucedida, retornar o objeto Usuario
 * return usuario;
 * }
 * 
 * // Caso contrário, retornar null (autenticação falhou)
 * return null;
 * }
 * 
 * 
 * public boolean cadastrarUsuario(Usuario usuario) throws SQLException {
 * boolean sucesso = false;
 * 
 * String sqlUsuario =
 * "INSERT INTO Usuario (nome, email, senha, data_nascimento, telefone, rua, cidade, estado, cep) "
 * +
 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
 * 
 * try (Connection conn = DAO.conectar()) {
 * // Desativar auto-commit para realizar transações
 * conn.setAutoCommit(false);
 * 
 * try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {
 * // Definir os parâmetros da consulta SQL
 * stmtUsuario.setString(1, usuario.getNome());
 * stmtUsuario.setString(2, usuario.getEmail());
 * stmtUsuario.setString(3, usuario.getSenha()); // Lembre-se de criptografar a
 * senha
 * stmtUsuario.setDate(4, Date.valueOf(usuario.getDataNascimento()));
 * stmtUsuario.setString(5, usuario.getTelefone());
 * 
 * // Definir os parâmetros de endereço do usuário
 * rua rua = usuario.getrua();
 * stmtUsuario.setString(6, rua.getrua());
 * stmtUsuario.setString(7, rua.getCidade());
 * stmtUsuario.setString(8, rua.getEstado());
 * stmtUsuario.setString(9, rua.getCep());
 * 
 * // Executa a inserção
 * stmtUsuario.executeUpdate();
 * 
 * // Commit da transação
 * conn.commit();
 * sucesso = true;
 * } catch (SQLException e) {
 * // Se ocorrer um erro, realizar rollback
 * conn.rollback();
 * e.printStackTrace(); // Tratar o erro de maneira apropriada
 * }
 * 
 * return sucesso;
 * }
 * 
 * }
 * }
 */
