package service;

import java.util.List;

import dao.MeusLivrosDAO;
import model.MeusLivros;

public class MeusLivrosService {
    private MeusLivrosDAO meusLivrosDAO;

    public MeusLivrosService() {
        this.meusLivrosDAO = new MeusLivrosDAO();
    }

    /**
     * Obtém todas as postagens de livros de um usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de MeusLivros.
     */
    public List<MeusLivros> getMeusLivros(int idUsuario) {
        return meusLivrosDAO.buscarMeusLivros(idUsuario);
    }

    /**
     * Atualiza o status de uma postagem de livro.
     * 
     * @param idPostagem ID da postagem.
     * @param novoStatus Novo status a ser definido.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizarStatusPostagem(int idPostagem, String novoStatus) {
        return meusLivrosDAO.atualizarStatusPostagem(idPostagem, novoStatus);
    }

    /**
     * Deleta uma postagem de livro.
     * 
     * @param idPostagem ID da postagem.
     * @return true se a deleção for bem-sucedida, false caso contrário.
     */
    public boolean deletarPostagem(int idPostagem) {
        return meusLivrosDAO.deletarPostagem(idPostagem);
    }
}
