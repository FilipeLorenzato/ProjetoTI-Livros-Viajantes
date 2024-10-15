package service;

import dao.InicioDAO;
import model.Inicio;

public class InicioService {
    private InicioDAO inicioDAO;

    public InicioService() {
        this.inicioDAO = new InicioDAO();
    }

    /**
     * Obtém as estatísticas de início para um usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Objeto Inicio com as estatísticas.
     */
    public Inicio getEstatisticasInicio(int idUsuario) {
        return inicioDAO.buscarEstatisticasInicio(idUsuario);
    }
}
