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
//import dao.DAO;

public class TrocaDAO {
    private Connection conexao;

    public Connection conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String mydatabase = "postgres";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "ti2cc";
        String password = "ti@cc";
        Connection conn = null;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            conn = conexao;
            System.out.println("Conexão efetuada com o postgres!");
        } catch (ClassNotFoundException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
        }

        return conn;
    }

    // Inserir nova troca
    public boolean inserir(Troca troca) {
        String sql = "INSERT INTO troca (id_livro, usuario_ofertante, usuario_contemplado, status) VALUES (?, ?, ?, ?)";
        try (Connection conexao = this.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, troca.getIdLivro());
            stmt.setInt(2, troca.getUsuarioOfertante());
            stmt.setInt(3, troca.getUsuarioContemplado());
            stmt.setString(4, troca.getStatus());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir troca: " + e.getMessage());
            return false;
        }
    }

    // Atualizar status da troca
    public boolean atualizarStatus(int idTroca, String novoStatus) {
        String sql = "UPDATE troca SET status = ? WHERE id_troca = ?";

        try (Connection conn = this.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idTroca);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da troca: " + e.getMessage());
            return false;
        }
    }

    // Obter uma troca por ID
    public Troca getById(int idTroca) {
        String sql = "SELECT * FROM troca WHERE id_troca = ?";
        Troca troca = null;

        try (Connection conexao = this.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            System.err.println("Erro ao obter troca por ID: " + e.getMessage());
        }
        return troca;
    }

    // Listar todas as trocas
    public List<Troca> listarTodas() {
        List<Troca> lista = new ArrayList<>();
        String sql = "SELECT * FROM troca";
        try (Connection conexao = this.conectar();
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Troca troca = new Troca(
                        rs.getInt("id_troca"),
                        rs.getInt("id_livro"),
                        rs.getInt("usuario_ofertante"),
                        rs.getInt("usuario_contemplado"),
                        rs.getString("status"));
                lista.add(troca);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar trocas: " + e.getMessage());
        }
        return lista;
    }

    // Remover troca por ID
    public boolean remover(int idTroca) {
        String sql = "DELETE FROM troca WHERE id_troca = ?";

        try (Connection conexao = this.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTroca);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao remover troca: " + e.getMessage());
            return false;
        }
    }
}
