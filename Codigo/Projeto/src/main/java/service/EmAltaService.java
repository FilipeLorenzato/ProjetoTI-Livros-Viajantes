package service;

import dao.EmAltaDAO;
import model.LivroEmAlta;

import java.util.List;

public class EmAltaService {
    private EmAltaDAO emAltaDAO;

    public EmAltaService() {
        this.emAltaDAO = new EmAltaDAO();
    }

    /**
     * Obtém os livros mais trocados.
     * @param limite Quantidade máxima de livros a serem retornados.
     * @return Lista de LivroEmAlta.
     */
    public List<LivroEmAlta> getLivrosEmAlta(int limite) {
        return emAltaDAO.buscarLivrosEmAlta(limite);
    }
}
