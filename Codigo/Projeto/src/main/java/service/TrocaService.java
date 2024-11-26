package service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import dao.LivroDAO;
import dao.TrocaDAO;
import dao.UsuarioDAO;
import model.Livro;
import model.Troca;
import model.Usuario;

public class TrocaService {
    private TrocaDAO trocaDAO = new TrocaDAO();
    private LivroDAO livroDAO = new LivroDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String criarTroca(Request request, Response response) {
        try {
            // Parse JSON manually using org.json
            JSONObject requestBody = new JSONObject(request.body());

            int idLivroRecebido = requestBody.getInt("idLivroRecebido");
            int idLivroEnviado = requestBody.getInt("idLivroEnviado");
            int usuarioOfertante = requestBody.getInt("usuario_ofertante");
            int usuarioContemplado = requestBody.getInt("usuario_contemplado");
            String status = requestBody.getString("status");

            Troca troca = new Troca(0, idLivroRecebido, idLivroEnviado, usuarioOfertante, usuarioContemplado, status);

            if (trocaDAO.inserir(troca)) {
                response.status(201); // Created
                livroDAO.atualizarUsuarioLivro(troca.getIdLivroEnviado(), troca.getUsuarioContemplado());
                livroDAO.atualizarUsuarioLivro(troca.getIdLivroRecebido(), troca.getUsuarioOfertante());
                return "{\"mensagem\": \"Troca criada com sucesso.\"}";
            } else {
                response.status(500); // Internal Server Error
                return "{\"mensagem\": \"Erro ao criar troca.\"}";
            }
        } catch (Exception e) {
            response.status(400); // Bad Request
            return "{\"mensagem\": \"Erro nos dados enviados: " + e.getMessage() + "\"}";
        }
    }

    public Troca buscarTroca(int idTroca) {
        return trocaDAO.getById(idTroca);
    }

    public String listarTrocas(Response response) {
        try {
            List<Troca> trocas = trocaDAO.listarTodas();
            JSONArray jsonArray = new JSONArray();

            for (Troca troca : trocas) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("idTroca", troca.getIdTroca());

                // Obter o livro enviado completo
                Livro livroEnviado = livroDAO.buscarLivroPorId(troca.getIdLivroEnviado());
                if (livroEnviado != null) {
                    JSONObject livroEnviadoJson = new JSONObject();
                    livroEnviadoJson.put("idLivro", livroEnviado.getIdLivro());
                    livroEnviadoJson.put("titulo", livroEnviado.getTitulo());
                    livroEnviadoJson.put("autor", livroEnviado.getAutor());
                    livroEnviadoJson.put("genero", livroEnviado.getGenero());
                    livroEnviadoJson.put("sinopse", livroEnviado.getSinopse());
                    livroEnviadoJson.put("imagem", livroEnviado.getImagem());
                    jsonObject.put("livroEnviado", livroEnviadoJson);
                }

                // Obter o livro recebido completo
                Livro livroRecebido = livroDAO.buscarLivroPorId(troca.getIdLivroRecebido());
                if (livroRecebido != null) {
                    JSONObject livroRecebidoJson = new JSONObject();
                    livroRecebidoJson.put("idLivro", livroRecebido.getIdLivro());
                    livroRecebidoJson.put("titulo", livroRecebido.getTitulo());
                    livroRecebidoJson.put("autor", livroRecebido.getAutor());
                    livroRecebidoJson.put("genero", livroRecebido.getGenero());
                    livroRecebidoJson.put("sinopse", livroRecebido.getSinopse());
                    livroRecebidoJson.put("imagem", livroRecebido.getImagem());
                    jsonObject.put("livroRecebido", livroRecebidoJson);
                }

                // Obter o usuário ofertante completo
                Usuario usuarioOfertante = usuarioDAO.getById(troca.getUsuarioOfertante());
                if (usuarioOfertante != null) {
                    JSONObject usuarioOfertanteJson = new JSONObject();
                    usuarioOfertanteJson.put("idUsuario", usuarioOfertante.getIdUsuario());
                    usuarioOfertanteJson.put("nome", usuarioOfertante.getNome());
                    usuarioOfertanteJson.put("email", usuarioOfertante.getEmail());
                    jsonObject.put("usuarioOfertante", usuarioOfertanteJson);
                }

                // Obter o usuário contemplado completo
                Usuario usuarioContemplado = usuarioDAO.getById(troca.getUsuarioContemplado());
                if (usuarioContemplado != null) {
                    JSONObject usuarioContempladoJson = new JSONObject();
                    usuarioContempladoJson.put("idUsuario", usuarioContemplado.getIdUsuario());
                    usuarioContempladoJson.put("nome", usuarioContemplado.getNome());
                    usuarioContempladoJson.put("email", usuarioContemplado.getEmail());
                    jsonObject.put("usuarioContemplado", usuarioContempladoJson);
                }

                jsonObject.put("status", troca.getStatus());
                jsonArray.put(jsonObject);
            }

            return jsonArray.toString();
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            return "{\"mensagem\": \"Erro ao listar trocas.\"}";
        }
    }

    public String listarTrocasPorUsuario(int userId, Response response) {
        try {
            List<Troca> trocas = trocaDAO.listarPorUsuario(userId);
            JSONArray jsonArray = new JSONArray();

            for (Troca troca : trocas) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("idTroca", troca.getIdTroca());

                // Obter o livro enviado completo
                Livro livroEnviado = livroDAO.buscarLivroPorId(troca.getIdLivroEnviado());
                if (livroEnviado != null) {
                    JSONObject livroEnviadoJson = new JSONObject();
                    livroEnviadoJson.put("idLivro", livroEnviado.getIdLivro());
                    livroEnviadoJson.put("titulo", livroEnviado.getTitulo());
                    livroEnviadoJson.put("autor", livroEnviado.getAutor());
                    livroEnviadoJson.put("genero", livroEnviado.getGenero());
                    livroEnviadoJson.put("sinopse", livroEnviado.getSinopse());
                    livroEnviadoJson.put("imagem", livroEnviado.getImagem());
                    jsonObject.put("livroEnviado", livroEnviadoJson);
                }

                // Obter o livro recebido completo
                Livro livroRecebido = livroDAO.buscarLivroPorId(troca.getIdLivroRecebido());
                if (livroRecebido != null) {
                    JSONObject livroRecebidoJson = new JSONObject();
                    livroRecebidoJson.put("idLivro", livroRecebido.getIdLivro());
                    livroRecebidoJson.put("titulo", livroRecebido.getTitulo());
                    livroRecebidoJson.put("autor", livroRecebido.getAutor());
                    livroRecebidoJson.put("genero", livroRecebido.getGenero());
                    livroRecebidoJson.put("sinopse", livroRecebido.getSinopse());
                    livroRecebidoJson.put("imagem", livroRecebido.getImagem());
                    jsonObject.put("livroRecebido", livroRecebidoJson);
                }

                // Obter o usuário ofertante completo
                Usuario usuarioOfertante = usuarioDAO.getById(troca.getUsuarioOfertante());
                if (usuarioOfertante != null) {
                    JSONObject usuarioOfertanteJson = new JSONObject();
                    usuarioOfertanteJson.put("idUsuario", usuarioOfertante.getIdUsuario());
                    usuarioOfertanteJson.put("nome", usuarioOfertante.getNome());
                    usuarioOfertanteJson.put("email", usuarioOfertante.getEmail());
                    jsonObject.put("usuarioOfertante", usuarioOfertanteJson);
                }

                // Obter o usuário contemplado completo
                Usuario usuarioContemplado = usuarioDAO.getById(troca.getUsuarioContemplado());
                if (usuarioContemplado != null) {
                    JSONObject usuarioContempladoJson = new JSONObject();
                    usuarioContempladoJson.put("idUsuario", usuarioContemplado.getIdUsuario());
                    usuarioContempladoJson.put("nome", usuarioContemplado.getNome());
                    usuarioContempladoJson.put("email", usuarioContemplado.getEmail());
                    jsonObject.put("usuarioContemplado", usuarioContempladoJson);
                }

                jsonObject.put("status", troca.getStatus());
                jsonArray.put(jsonObject);
            }

            response.type("application/json");
            return jsonArray.toString();
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            return "{\"mensagem\": \"Erro ao listar trocas.\"}";
        }
    }

    public boolean atualizarStatusTroca(int idTroca, String status) {
        return trocaDAO.atualizarStatus(idTroca, status);
    }

    public boolean removerTroca(int idTroca) {
        return trocaDAO.remover(idTroca);
    }
}
