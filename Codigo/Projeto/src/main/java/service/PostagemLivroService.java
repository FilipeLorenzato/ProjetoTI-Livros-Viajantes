package service;

import java.util.List;

import dao.PostagemLivroDAO;
import model.PostagemLivro;

public class PostagemLivroService {
    private PostagemLivroDAO postagemLivroDAO;

    public PostagemLivroService() {
        this.postagemLivroDAO = new PostagemLivroDAO();
    }

    /**
     * Cadastra uma nova postagem de livro.
     * 
     * @param postagemLivro Objeto PostagemLivro a ser cadastrado.
     * @return true se o cadastro for bem-sucedido, false caso contrário.
     */
    public boolean cadastrarPostagemLivro(PostagemLivro postagemLivro) {
        // Aqui você pode adicionar validações ou lógica adicional antes de cadastrar
        return postagemLivroDAO.inserirPostagemLivro(postagemLivro);
    }

    /**
     * Obtém todas as postagens de livros.
     * 
     * @return Lista de PostagemLivro.
     */
    public List<PostagemLivro> getTodasPostagens() {
        return postagemLivroDAO.buscarTodasPostagens();
    }

    /**
     * Obtém as postagens de livros por usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de PostagemLivro.
     */
    public List<PostagemLivro> getPostagensPorUsuario(int idUsuario) {
        return postagemLivroDAO.buscarPostagensPorUsuario(idUsuario);
    }

    // Métodos adicionais como atualizar status, deletar postagem etc., podem ser
    // adicionados conforme necessário
}
