package service;

import java.util.List;

import dao.TrocaDAO;
import model.Troca;

public class TrocaService {
    private TrocaDAO trocaDAO = new TrocaDAO();

    public boolean criarTroca(Troca troca) {
        return trocaDAO.inserir(troca);
    }

    public Troca buscarTroca(int idTroca) {
        return trocaDAO.getById(idTroca);
    }

    public List<Troca> listarTrocas() {
        return trocaDAO.listarTodas();
    }

    public boolean atualizarStatusTroca(int idTroca, String status) {
        return trocaDAO.atualizarStatus(idTroca, status);
    }

    public boolean removerTroca(int idTroca) {
        return trocaDAO.remover(idTroca);
    }
}
