package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Troca;

public class TrocaDAO {
    private Connection conexao;

    public TrocaDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/livros_viajantes", "user",
                    "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inserir nova troca
    public boolean inserir(Troca troca) {
        try {
            String sql = "INSERT INTO troca (id_livro, usuario_ofertante, usuario_contemplado, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, troca.getIdLivro());
            stmt.setInt(2, troca.getUsuarioOfertante());
            stmt.setInt(3, troca.getUsuarioContemplado());
            stmt.setString(4, troca.getStatus());
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atualizar status da troca
    public boolean atualizarStatus(int idTroca, String novoStatus) {
        try {
            String sql = "UPDATE troca SET status = ? WHERE id_troca = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idTroca);
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obter uma troca por ID
    public Troca getById(int idTroca) {
        Troca troca = null;
        try {
            String sql = "SELECT * FROM troca WHERE id_troca = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idTroca);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                troca = new Troca(
                        rs.getInt("id_troca"),
                        rs.getInt("id_livro"),
                        rs.getInt("usuario_ofertante"),
                        rs.getInt("usuario_contemplado"),
                        rs.getString("status"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return troca;
    }

    // Listar todas as trocas
    public List<Troca> listarTodas() {
        List<Troca> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM troca";
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Troca troca = new Troca(
                        rs.getInt("id_troca"),
                        rs.getInt("id_livro"),
                        rs.getInt("usuario_ofertante"),
                        rs.getInt("usuario_contemplado"),
                        rs.getString("status"));
                lista.add(troca);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Remover troca por ID
    public boolean remover(int idTroca) {
        try {
            String sql = "DELETE FROM troca WHERE id_troca = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idTroca);
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
