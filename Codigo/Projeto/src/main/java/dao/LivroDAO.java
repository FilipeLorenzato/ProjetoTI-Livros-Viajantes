package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Livro;

public class LivroDAO {

    /**
     * Insere um novo livro no banco de dados.
     * 
     * @param livro Objeto Livro a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserirLivro(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autor, genero, sinopse) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getSinopse());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    livro.setIdLivro(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca um livro por ID.
     * 
     * @param idLivro ID do livro a ser buscado.
     * @return Objeto Livro se encontrado, null caso contrário.
     */
    public Livro buscarLivroPorId(int idLivro) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        Livro livro = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLivro);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
        }

        return livro;
    }

    /**
     * Busca todos os livros cadastrados.
     * 
     * @return Lista de livros.
     */
    public List<Livro> buscarTodosLivros() {
        String sql = "SELECT * FROM livro";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"));
                livros.add(livro);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os livros: " + e.getMessage());
        }

        return livros;
    }

    /**
     * Atualiza as informações de um livro existente.
     * 
     * @param livro Objeto Livro com as informações atualizadas.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizarLivro(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, genero = ?, sinopse = ? WHERE id_livro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getSinopse());
            pstmt.setInt(5, livro.getIdLivro());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deleta um livro do banco de dados.
     * 
     * @param idLivro ID do livro a ser deletado.
     * @return true se a deleção for bem-sucedida, false caso contrário.
     */
    public boolean deletarLivro(int idLivro) {
        String sql = "DELETE FROM livro WHERE id_livro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLivro);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro: " + e.getMessage());
            return false;
        }
    }
}
