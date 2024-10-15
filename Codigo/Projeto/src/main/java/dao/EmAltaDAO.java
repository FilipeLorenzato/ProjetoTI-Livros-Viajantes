package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Livro;
import model.LivroEmAlta;

public class EmAltaDAO {

    /**
     * Busca os livros mais trocados.
     * 
     * @param limite Quantidade máxima de livros a serem retornados.
     * @return Lista de LivroEmAlta.
     */
    public List<LivroEmAlta> buscarLivrosEmAlta(int limite) {
        String sql = "SELECT l.id_livro, l.titulo, l.autor, l.genero, l.sinopse, COUNT(t.id_troca) AS quantidadeTrocas "
                +
                "FROM livro l " +
                "JOIN troca t ON l.id_livro = t.id_livro " +
                "GROUP BY l.id_livro, l.titulo, l.autor, l.genero, l.sinopse " +
                "ORDER BY quantidadeTrocas DESC " +
                "LIMIT ?";
        List<LivroEmAlta> livrosEmAlta = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"));
                int quantidadeTrocas = rs.getInt("quantidadeTrocas");
                LivroEmAlta livroEmAlta = new LivroEmAlta(livro, quantidadeTrocas);
                livrosEmAlta.add(livroEmAlta);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros em alta: " + e.getMessage());
        }

        return livrosEmAlta;
    }
}
