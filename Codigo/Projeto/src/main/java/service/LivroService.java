package service;

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
        livroDAO = new LivroDAO();
        livroDAO.conectar();
    }

    // Método para cadastrar um novo livro
    public Object cadastrarLivro(Request request, Response response) {
        try {
            JSONObject body = new JSONObject(request.body());

            String titulo = body.getString("titulo");
            String autor = body.getString("autor");
            String genero = body.getString("genero");
            String sinopse = body.getString("sinopse");
            String imagem = body.getString("imagem"); // Base64 diretamente
            int idUsuario = body.getInt("id_usuario");

            Livro livro = new Livro(titulo, autor, genero, sinopse, imagem, idUsuario);

            boolean sucesso = livroDAO.inserirLivro(livro);
            if (sucesso) {
                response.status(201); // Created
                return "Livro cadastrado com sucesso!";
            } else {
                response.status(500); // Internal Server Error
                return "Erro ao cadastrar o livro";
            }
        } catch (Exception e) {
            response.status(400); // Bad Request
            return "Erro na requisição: " + e.getMessage();
        }
    }

    // Método para buscar um livro pelo ID (com imagem)
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
                jsonResponse.put("imagem", livro.getImagem());

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
    

    // Método que chama o DAO para buscar livro pelo título
    public String buscarLivroPorTitulo(String titulo) {
        // Normaliza o título recebido (caso necessário)
        titulo = titulo.replaceAll("[\\s]+", " ").trim();  // Remove espaços extras, mas mantém espaços entre palavras
        titulo = titulo.toLowerCase();  // Converte para minúsculas

        System.out.println("Título enviado para a consulta: '" + titulo + "'");

        // Chama o método do DAO para buscar o livro
        Livro livro = livroDAO.buscarPorTitulo(titulo);

        // Verifica se o livro foi encontrado e retorna o resultado adequado
        if (livro != null) {
            return "Livro encontrado: " + livro.getTitulo() + " - Autor: " + livro.getAutor();
        } else {
            return "Livro não encontrado no banco de dados.";
        }
    }

    // Método para listar os livros de um usuário por userId
    public Object listarLivrosPorUsuario(Request request, Response response) {
        try {
            int userId = Integer.parseInt(request.params(":userId"));
            List<Livro> livros = livroDAO.buscarLivrosPorUsuario(userId);

            if (!livros.isEmpty()) {
                JSONArray jsonResponse = new JSONArray();
                for (Livro livro : livros) {
                    JSONObject jsonLivro = new JSONObject();
                    jsonLivro.put("idLivro", livro.getIdLivro());
                    jsonLivro.put("titulo", livro.getTitulo());
                    jsonLivro.put("autor", livro.getAutor());
                    jsonLivro.put("genero", livro.getGenero());
                    jsonLivro.put("sinopse", livro.getSinopse());
                    jsonLivro.put("imagem", livro.getImagem());

                    jsonResponse.put(jsonLivro);
                }
                response.type("application/json");
                return jsonResponse.toString();
            } else {
                response.status(404);
                return "{\"mensagem\":\"Nenhum livro encontrado para o usuário especificado.\"}";
            }
        } catch (NumberFormatException e) {
            response.status(400); // Bad Request
            return "{\"mensagem\":\"Parâmetro userId inválido.\"}";
        } catch (Exception e) {
            response.status(500);
            return "{\"mensagem\":\"Erro ao processar a solicitação: " + e.getMessage() + "\"}";
        }
    }

    // Método para listar todos os livros (com imagem)
    public Object listarTodosLivros(Response response) {
        List<Livro> livros = livroDAO.buscarTodosLivros();
        JSONArray jsonResponse = new JSONArray();
        for (Livro livro : livros) {
            JSONObject jsonLivro = new JSONObject();
            jsonLivro.put("id", livro.getIdLivro());
            jsonLivro.put("titulo", livro.getTitulo());
            jsonLivro.put("autor", livro.getAutor());
            jsonLivro.put("genero", livro.getGenero());
            jsonLivro.put("sinopse", livro.getSinopse());
            jsonLivro.put("id_usuario", livro.getidUsuario());
            jsonLivro.put("imagem", livro.getImagem());

            jsonResponse.put(jsonLivro);
        }

        response.type("application/json");
        return jsonResponse.toString();
    }

    // Método para atualizar um livro pelo ID (com imagem)
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
                livro.setImagem(jsonBody.getString("imagem"));

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

    // Método para buscar livros postados por um usuário pelo ID do usuário (com
    // imagem)
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
                    jsonLivro.put("imagem", livro.getImagem());

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

    // Método para listar livros "em alta" com limite especificado (com imagem)
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
                jsonLivro.put("imagem", livro.getImagem());

                jsonResponse.put(jsonLivro);
            }

            response.type("application/json");
            return jsonResponse.toString();
        } catch (Exception e) {
            response.status(500);
            return "Erro ao processar a solicitação: " + e.getMessage();
        }
    }

    public Object trocarUsuariosLivros(Request request, Response response) {
        try {
            JSONObject body = new JSONObject(request.body());
            int idLivroEnviado = body.getInt("idLivroEnviado");
            int idLivroRecebido = body.getInt("idLivroRecebido");
            int novoUsuarioEnviado = body.getInt("novoUsuarioEnviado");
            int novoUsuarioRecebido = body.getInt("novoUsuarioRecebido");

            // Atualizar o id_usuario dos livros
            boolean atualizadoEnviado = livroDAO.atualizarUsuarioLivro(idLivroEnviado, novoUsuarioEnviado);
            boolean atualizadoRecebido = livroDAO.atualizarUsuarioLivro(idLivroRecebido, novoUsuarioRecebido);

            if (atualizadoEnviado && atualizadoRecebido) {
                response.status(200); // OK
                return "Troca de usuários realizada com sucesso.";
            } else {
                response.status(500); // Internal Server Error
                return "Erro ao realizar a troca de usuários.";
            }
        } catch (Exception e) {
            response.status(400); // Bad Request
            return "Erro ao processar a requisição: " + e.getMessage();
        }
    }

}
