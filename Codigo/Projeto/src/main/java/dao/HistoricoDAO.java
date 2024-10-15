package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Historico;
import model.Livro;
import model.Usuario;

public class HistoricoDAO {

    /**
     * Busca o histórico de trocas de um usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de Historico.
     */
    public List<Historico> buscarHistoricoPorUsuario(int idUsuario) {
        String sql = "SELECT t.id_troca, t.status, t.data_troca, " +
                "l.id_livro, l.titulo, l.autor, l.genero, l.sinopse, " +
                "u1.id_usuario AS ofertante_id, u1.nome AS ofertante_nome, u1.email AS ofertante_email, u1.senha AS ofertante_senha, u1.data_nascimento AS ofertante_dtnasc, u1.telefone AS ofertante_tel, "
                +
                "u2.id_usuario AS contemplado_id, u2.nome AS contemplado_nome, u2.email AS contemplado_email, u2.senha AS contemplado_senha, u2.data_nascimento AS contemplado_dtnasc, u2.telefone AS contemplado_tel "
                +
                "FROM troca t " +
                "JOIN livro l ON t.id_livro = l.id_livro " +
                "JOIN usuario u1 ON t.usuario_ofertante = u1.id_usuario " +
                "JOIN usuario u2 ON t.usuario_contemplado = u2.id_usuario " +
                "WHERE t.usuario_ofertante = ? OR t.usuario_contemplado = ? " +
                "ORDER BY t.data_troca DESC";

        List<Historico> historicos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Cria o objeto Livro
                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"));

                // Cria o objeto Usuario ofertante
                Usuario ofertante = new Usuario(
                        rs.getInt("ofertante_id"),
                        rs.getString("ofertante_nome"),
                        rs.getString("ofertante_email"),
                        rs.getString("ofertante_senha"),
                        rs.getString("ofertante_dtnasc"),
                        rs.getString("ofertante_tel"),
                        null // Endereço pode ser adicionado se necessário
                );

                // Cria o objeto Usuario contemplado
                Usuario contemplado = new Usuario(
                        rs.getInt("contemplado_id"),
                        rs.getString("contemplado_nome"),
                        rs.getString("contemplado_email"),
                        rs.getString("contemplado_senha"),
                        rs.getString("contemplado_dtnasc"),
                        rs.getString("contemplado_tel"),
                        null // Endereço pode ser adicionado se necessário
                );

                // Cria o objeto Historico
                Historico historico = new Historico(
                        rs.getInt("id_troca"),
                        livro,
                        ofertante,
                        contemplado,
                        rs.getString("status"),
                        rs.getString("data_troca"));

                historicos.add(historico);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        }

        return historicos;
    }

    // Métodos adicionais como inserir troca, atualizar status etc., podem ser
    // adicionados conforme necessário
}
