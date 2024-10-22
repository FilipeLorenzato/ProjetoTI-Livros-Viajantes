package service;
//import dao.DAO;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.LivroDAO;
import model.Livro;
import spark.Request;
import spark.Response;

public class LivroService {
    private LivroDAO livroDAO;

    public LivroService() {
        this.livroDAO = new LivroDAO();
    }

    // Método para cadastrar um novo livro
    public Object cadastrarLivro(Request request, Response response) {
        try {
            JSONObject jsonBody = new JSONObject(request.body());
            String titulo = jsonBody.getString("titulo");
            String autor = jsonBody.getString("autor");
            String genero = jsonBody.getString("genero");
            String sinopse = jsonBody.getString("sinopse");

            int id = livroDAO.getMaxId() + 1;

            Livro livro = new Livro(id, titulo, autor, genero, sinopse);
            livroDAO.inserirLivro(livro);

            response.status(201); // 201 Created
            return id;
        } catch (Exception e) {
            response.status(500);
            return "Erro ao cadastrar livro: " + e.getMessage();
        }
    }

    // Método para buscar um livro pelo ID
    public Object getLivroById(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Livro livro = livroDAO.buscarLivroPorId(id);

            if (livro != null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("titulo", livro.getTitulo());
                jsonResponse.put("autor", livro.getAutor());
                jsonResponse.put("genero", livro.getGenero());
                jsonResponse.put("sinopse", livro.getSinopse());

                response.type("application/json");
                return jsonResponse.toString();
            } else {
                response.status(404); // 404 Not Found
                return "Livro não encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400); // Bad Request
            return "ID inválido.";
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            return "Erro ao processar a solicitação.";
        }
    }

    // Método para listar todos os livros
    public Object listarTodosLivros(Response response) {
        List<Livro> livros = livroDAO.buscarTodosLivros();
        JSONArray jsonResponse = new JSONArray();
        for (Livro livro : livros) {
            JSONObject jsonLivro = new JSONObject();
            jsonLivro.put("titulo", livro.getTitulo());
            jsonLivro.put("autor", livro.getAutor());
            jsonLivro.put("genero", livro.getGenero());
            jsonLivro.put("sinopse", livro.getSinopse());
            jsonResponse.put(jsonLivro);
        }

        response.type("application/json");
        return jsonResponse.toString();
    }

    // Método para atualizar um livro pelo ID
    public Object updateLivro(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Livro livro = livroDAO.buscarLivroPorId(id);
            if (livro != null) {
                JSONObject jsonBody = new JSONObject(request.body());
                livro.setTitulo(jsonBody.getString("titulo"));
                livro.setAutor(jsonBody.getString("autor"));
                livro.setGenero(jsonBody.getString("genero"));
                livro.setSinopse(jsonBody.getString("sinopse"));

                livroDAO.atualizarLivro(livro);
                return id;
            } else {
                response.status(404); // Not Found
                return "Livro não encontrado.";
            }
        } catch (Exception e) {
            response.status(500);
            return "Erro ao processar a requisição: " + e.getMessage();
        }
    }

    // Método para deletar um livro pelo ID
    public Object deletarLivro(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            Livro livro = livroDAO.buscarLivroPorId(id);
            if (livro != null) {
                livroDAO.deletarLivro(id);
                response.status(200); // success
                return id;
            } else {
                response.status(404); // 404 Not Found
                return "Livro não encontrado.";
            }
        } catch (NumberFormatException e) {
            response.status(400); // Bad Request
            return "ID inválido.";
        } catch (Exception e) {
            response.status(500); // Internal Server Error
            return "Erro ao processar a solicitação.";
        }
    }

    // Método para buscar livros postados por um usuário pelo ID do usuário
    public Object getHistoricoLivrosUsuario(Request request, Response response) {
        try {
            int idUsuario = Integer.parseInt(request.params(":idUsuario"));
            List<Livro> livros = livroDAO.buscarLivrosPorUsuario(idUsuario);

            if (!livros.isEmpty()) {
                JSONArray jsonResponse = new JSONArray();
                for (Livro livro : livros) {
                    JSONObject jsonLivro = new JSONObject();
                    jsonLivro.put("titulo", livro.getTitulo());
                    jsonLivro.put("autor", livro.getAutor());
                    jsonLivro.put("genero", livro.getGenero());
                    jsonLivro.put("sinopse", livro.getSinopse());
                    jsonResponse.put(jsonLivro);
                }
                response.type("application/json");
                return jsonResponse.toString();
            } else {
                response.status(404);
                return "Nenhum livro encontrado para o usuário especificado.";
            }
        } catch (Exception e) {
            response.status(500);
            return "Erro ao processar a solicitação: " + e.getMessage();
        }
    }

    // Método para listar livros "em alta" com limite especificado
    public Object getLivrosEmAlta(Response response) {
        try {
            List<Livro> livrosEmAlta = livroDAO.buscarLivrosEmAlta(10); // Limite de 10 como exemplo
            JSONArray jsonResponse = new JSONArray();
            for (Livro livro : livrosEmAlta) {
                JSONObject jsonLivro = new JSONObject();
                jsonLivro.put("titulo", livro.getTitulo());
                jsonLivro.put("autor", livro.getAutor());
                jsonLivro.put("genero", livro.getGenero());
                jsonLivro.put("sinopse", livro.getSinopse());
                jsonResponse.put(jsonLivro);
            }

            response.type("application/json");
            return jsonResponse.toString();
        } catch (Exception e) {
            response.status(500);
            return "Erro ao processar a solicitação: " + e.getMessage();
        }
    }
}
