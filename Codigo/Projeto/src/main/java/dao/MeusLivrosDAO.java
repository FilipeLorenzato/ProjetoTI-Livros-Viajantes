package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Livro;
import model.MeusLivros;

public class MeusLivrosDAO {

    /**
     * Busca todas as postagens de livros de um usuário específico.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de MeusLivros.
     */
    public List<MeusLivros> buscarMeusLivros(int idUsuario) {
        String sql = "SELECT pl.id_postagem, pl.data_postagem, pl.status, " +
                "l.id_livro, l.titulo, l.autor, l.genero, l.sinopse " +
                "FROM postagem_livro pl " +
                "JOIN livro l ON pl.id_livro = l.id_livro " +
                "WHERE pl.id_usuario_postante = ? " +
                "ORDER BY pl.data_postagem DESC";
        List<MeusLivros> meusLivros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"));

                MeusLivros postagem = new MeusLivros(
                        rs.getInt("id_postagem"),
                        livro,
                        rs.getString("data_postagem"),
                        rs.getString("status"));

                meusLivros.add(postagem);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar meus livros: " + e.getMessage());
        }

        return meusLivros;
    }

    /**
     * Atualiza o status de uma postagem de livro.
     * 
     * @param idPostagem ID da postagem.
     * @param novoStatus Novo status a ser definido.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizarStatusPostagem(int idPostagem, String novoStatus) {
        String sql = "UPDATE postagem_livro SET status = ? WHERE id_postagem = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus);
            pstmt.setInt(2, idPostagem);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da postagem: " + e.getMessage());
        }

        return false;
    }

    /**
     * Deleta uma postagem de livro.
     * 
     * @param idPostagem ID da postagem.
     * @return true se a deleção for bem-sucedida, false caso contrário.
     */
    public boolean deletarPostagem(int idPostagem) {
        String sql = "DELETE FROM postagem_livro WHERE id_postagem = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPostagem);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar postagem: " + e.getMessage());
        }

        return false;
    }
}
