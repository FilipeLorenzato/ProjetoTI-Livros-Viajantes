package service;

import java.util.List;

import dao.HistoricoDAO;
import model.Historico;

public class HistoricoService {
    private HistoricoDAO historicoDAO;

    public HistoricoService() {
        this.historicoDAO = new HistoricoDAO();
    }

    /**
     * Obtém o histórico de trocas de um usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de Historico.
     */
    public List<Historico> getHistoricoPorUsuario(int idUsuario) {
        return historicoDAO.buscarHistoricoPorUsuario(idUsuario);
    }

    // Métodos adicionais como inserir troca, atualizar status etc., podem ser
    // adicionados conforme necessário
}
