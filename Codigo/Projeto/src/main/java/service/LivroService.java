package service;

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

    // Método para cadastrar um novo livro (com imagem)
public Object cadastrarLivro(Request request, Response response) {
    if (ServletFileUpload.isMultipartContent(request.raw())) {
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request.raw());

            String titulo = null;
            String autor = null;
            String genero = null;
            String sinopse = null;
            FileItem imagem = null;

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Processa campos de texto
                    switch (item.getFieldName()) {
                        case "titulo": titulo = item.getString(); break;
                        case "autor": autor = item.getString(); break;
                        case "genero": genero = item.getString(); break;
                        case "sinopse": sinopse = item.getString(); break;
                    }
                } else {
                    // Processa o campo de imagem
                    if ("book-image".equals(item.getFieldName())) {
                        imagem = item; // Imagem enviada
                    }
                }
            }

            // Certifique-se de que os parâmetros essenciais estão presentes
            if (titulo == null || autor == null || genero == null || sinopse == null || imagem == null) {
                response.status(400);
                return "Parâmetros obrigatórios ausentes!";
            }

            // Agora você pode salvar o livro e a imagem
            Livro livro = new Livro(titulo, autor, genero, sinopse, imagem.get());
            // Processar a imagem conforme a lógica de armazenamento
            // ...

            boolean sucesso = livroDAO.inserirLivro(livro);
            if (sucesso) {
                response.status(201); // Created
                return "Livro cadastrado com sucesso!";
            } else {
                response.status(500);
                return "Erro ao cadastrar o livro";
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao processar o upload: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao cadastrar o livro: " + e.getMessage();
        }
    } else {
        response.status(400);
            return "Requisição não é multipart/form-data";
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

                // Converte a imagem para base64 para retornar como JSON
                if (livro.getImagem() != null) {
                    String imagemBase64 = java.util.Base64.getEncoder().encodeToString(livro.getImagem());
                    jsonResponse.put("imagem", imagemBase64);
                }

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

    // Método para listar todos os livros (com imagem)
    public Object listarTodosLivros(Response response) {
        List<Livro> livros = livroDAO.buscarTodosLivros();
        JSONArray jsonResponse = new JSONArray();
        for (Livro livro : livros) {
            JSONObject jsonLivro = new JSONObject();
            jsonLivro.put("titulo", livro.getTitulo());
            jsonLivro.put("autor", livro.getAutor());
            jsonLivro.put("genero", livro.getGenero());
            jsonLivro.put("sinopse", livro.getSinopse());

            // Converte a imagem para base64 para retornar como JSON
            if (livro.getImagem() != null) {
                String imagemBase64 = java.util.Base64.getEncoder().encodeToString(livro.getImagem());
                jsonLivro.put("imagem", imagemBase64);
            }

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

                // Atualiza a imagem se fornecida
                String imagemBase64 = jsonBody.optString("imagem");
                if (!imagemBase64.isEmpty()) {
                    byte[] imagem = java.util.Base64.getDecoder().decode(imagemBase64);
                    livro.setImagem(imagem);
                }

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

                    // Converte a imagem para base64 para retornar como JSON
                    if (livro.getImagem() != null) {
                        String imagemBase64 = java.util.Base64.getEncoder().encodeToString(livro.getImagem());
                        jsonLivro.put("imagem", imagemBase64);
                    }

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

                // Converte a imagem para base64 para retornar como JSON
                if (livro.getImagem() != null) {
                    String imagemBase64 = java.util.Base64.getEncoder().encodeToString(livro.getImagem());
                    jsonLivro.put("imagem", imagemBase64);
                }

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
