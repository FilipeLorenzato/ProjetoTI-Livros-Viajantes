package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Usuario;

public class UsuarioDAO {

    // Método para inserir um novo usuário
    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, data_nascimento, telefone, cidade, rua, estado, cep, numero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha()); // Em produção, use hashing
            pstmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            pstmt.setString(5, usuario.getTelefone());
            pstmt.setString(6, usuario.getCidade());
            pstmt.setString(7, usuario.getRua());
            pstmt.setString(8, usuario.getEstado());
            pstmt.setString(9, usuario.getCep());
            pstmt.setString(10, usuario.getNumero());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Obtém o ID gerado automaticamente
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }

        return false;
    }

    // Método para buscar usuário por email
    public Usuario buscarUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }

        return usuario;
    }

    // Método para buscar usuário por ID
    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return usuario;
    }

    // Método para buscar todos os usuários
    public List<Usuario> buscarTodosUsuarios() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os usuários: " + e.getMessage());
        }

        return usuarios;
    }

    // Método para atualizar um usuário
    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, data_nascimento = ?, telefone = ?, cidade = ?, rua = ?, estado = ?, cep = ?, numero = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            pstmt.setString(5, usuario.getTelefone());
            pstmt.setString(6, usuario.getCidade());
            pstmt.setString(7, usuario.getRua());
            pstmt.setString(8, usuario.getEstado());
            pstmt.setString(9, usuario.getCep());
            pstmt.setString(10, usuario.getNumero());
            pstmt.setInt(11, usuario.getIdUsuario());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }

        return false;
    }

    // Método para deletar um usuário
    public boolean deletarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }

        return false;
    }
}
