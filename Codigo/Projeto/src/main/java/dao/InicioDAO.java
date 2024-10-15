package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.DatabaseConnection;
import model.Inicio;

public class InicioDAO {

    /**
     * Obtém as estatísticas de início para um usuário específico.
     * 
     * @param idUsuario ID do usuário.
     * @return Objeto Inicio com as estatísticas.
     */
    public Inicio buscarEstatisticasInicio(int idUsuario) {
        String sqlTotalLivros = "SELECT COUNT(*) AS total FROM postagem_livro WHERE id_usuario_postante = ?";
        String sqlTotalTrocas = "SELECT COUNT(*) AS total FROM troca WHERE usuario_ofertante = ? OR usuario_contemplado = ?";
        String sqlLivrosDisponiveis = "SELECT COUNT(*) AS total FROM postagem_livro WHERE id_usuario_postante = ? AND status = 'Disponível'";
        String sqlLivrosEmTroca = "SELECT COUNT(*) AS total FROM postagem_livro WHERE id_usuario_postante = ? AND status = 'Em Troca'";
        String sqlLivrosConcluidos = "SELECT COUNT(*) AS total FROM postagem_livro WHERE id_usuario_postante = ? AND status = 'Concluído'";

        Inicio inicio = new Inicio();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Total de livros postados
            try (PreparedStatement pstmt = conn.prepareStatement(sqlTotalLivros)) {
                pstmt.setInt(1, idUsuario);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    inicio.setTotalLivrosPostados(rs.getInt("total"));
                }
            }

            // Total de trocas realizadas
            try (PreparedStatement pstmt = conn.prepareStatement(sqlTotalTrocas)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idUsuario);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    inicio.setTotalTrocasRealizadas(rs.getInt("total"));
                }
            }

            // Livros disponíveis
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLivrosDisponiveis)) {
                pstmt.setInt(1, idUsuario);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    inicio.setLivrosDisponiveis(rs.getInt("total"));
                }
            }

            // Livros em troca
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLivrosEmTroca)) {
                pstmt.setInt(1, idUsuario);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    inicio.setLivrosEmTroca(rs.getInt("total"));
                }
            }

            // Livros concluídos
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLivrosConcluidos)) {
                pstmt.setInt(1, idUsuario);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    inicio.setLivrosConcluidos(rs.getInt("total"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar estatísticas de início: " + e.getMessage());
        }

        return inicio;
    }
}
