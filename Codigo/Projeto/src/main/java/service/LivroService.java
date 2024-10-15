package service;

import dao.LivroDAO;
import model.Livro;

import java.util.List;

public class LivroService {
    private LivroDAO livroDAO;

    public LivroService() {
        this.livroDAO = new LivroDAO();
    }

    public boolean cadastrarLivro(Livro livro) {
        // Aqui você pode adicionar validações ou lógica adicional antes de cadastrar
        return livroDAO.inserirLivro(livro);
    }

    public Livro getLivroById(int idLivro) {
        return livroDAO.buscarLivroPorId(idLivro);
    }

    public List<Livro> getAllLivros() {
        return livroDAO.buscarTodosLivros();
    }

    public boolean atualizarLivro(Livro livro) {
        // Validações ou lógica adicional podem ser adicionadas aqui
        return livroDAO.atualizarLivro(livro);
    }

    public boolean deletarLivro(int idLivro) {
        return livroDAO.deletarLivro(idLivro);
    }
}
