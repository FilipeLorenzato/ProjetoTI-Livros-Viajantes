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
import model.Endereco;
import model.Usuario;

public class UsuarioDAO {

    // Método para inserir um novo usuário
    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, data_nascimento, telefone, id_endereco) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha()); // Em produção, use hashing
            pstmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            pstmt.setString(5, usuario.getTelefone());
            pstmt.setInt(6, usuario.getEndereco().getIdEndereco());

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
        String sql = "SELECT u.*, e.cidade, e.rua, e.estado, e.cep, e.numero FROM usuario u JOIN endereco e ON u.id_endereco = e.id_endereco WHERE u.email = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Cria o objeto Endereco
                Endereco endereco = new Endereco(
                        rs.getInt("id_endereco"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );

                // Cria o objeto Usuario
                usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        endereco.getCidade(),
                        endereco.getRua(),
                        endereco.getEstado(),
                        endereco.getCep(),
                        endereco.getNumero()
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }

        return usuario;
    }

    // Método para buscar usuário por ID (opcional)
    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT u.*, e.cidade, e.rua, e.estado, e.cep, e.numero FROM usuario u JOIN endereco e ON u.id_endereco = e.id_endereco WHERE u.id_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Cria o objeto Endereco
                Endereco endereco = new Endereco(
                        rs.getInt("id_endereco"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );

                // Cria o objeto Usuario
                usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        endereco.getCidade(),
                        endereco.getRua(),
                        endereco.getEstado(),
                        endereco.getCep(),
                        endereco.getNumero()
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return usuario;
    }

    // Método para buscar todos os usuários
    public List<Usuario> buscarTodosUsuarios() {
        String sql = "SELECT u.id_usuario, u.nome, u.email, u.senha, u.data_nascimento, u.telefone, " +
                     "e.cidade, e.rua, e.estado, e.cep, e.numero " +
                     "FROM usuario u JOIN endereco e ON u.id_endereco = e.id_endereco";
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
        String sqlEndereco = "UPDATE endereco SET cidade = ?, rua = ?, estado = ?, cep = ?, numero = ? WHERE id_endereco = ?";
        String sqlUsuario = "UPDATE usuario SET nome = ?, email = ?, senha = ?, data_nascimento = ?, telefone = ? WHERE id_usuario = ?";

        Connection conn = null;
        PreparedStatement pstmtEndereco = null;
        PreparedStatement pstmtUsuario = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // Primeiro, obter o id_endereco do usuário
            String sqlObterEndereco = "SELECT id_endereco FROM usuario WHERE id_usuario = ?";
            PreparedStatement pstmtObterEndereco = conn.prepareStatement(sqlObterEndereco);
            pstmtObterEndereco.setInt(1, usuario.getIdUsuario());
            ResultSet rs = pstmtObterEndereco.executeQuery();
            int idEndereco = 0;
            if (rs.next()) {
                idEndereco = rs.getInt("id_endereco");
            } else {
                conn.rollback();
                throw new SQLException("Usuário não encontrado para atualização.");
            }
            rs.close();
            pstmtObterEndereco.close();

            // Atualizar endereço
            pstmtEndereco = conn.prepareStatement(sqlEndereco);
            pstmtEndereco.setString(1, usuario.getCidade());
            pstmtEndereco.setString(2, usuario.getRua());
            pstmtEndereco.setString(3, usuario.getEstado());
            pstmtEndereco.setString(4, usuario.getCep());
            pstmtEndereco.setString(5, usuario.getNumero());
            pstmtEndereco.setInt(6, idEndereco);

            int rowsAffected = pstmtEndereco.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                throw new SQLException("Atualização de endereço falhou, nenhuma linha afetada.");
            }

            // Atualizar usuário
            pstmtUsuario = conn.prepareStatement(sqlUsuario);
            pstmtUsuario.setString(1, usuario.getNome());
            pstmtUsuario.setString(2, usuario.getEmail());
            pstmtUsuario.setString(3, usuario.getSenha());
            pstmtUsuario.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            pstmtUsuario.setString(5, usuario.getTelefone());
            pstmtUsuario.setInt(6, usuario.getIdUsuario());

            rowsAffected = pstmtUsuario.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                throw new SQLException("Atualização de usuário falhou, nenhuma linha afetada.");
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (pstmtEndereco != null) pstmtEndereco.close();
                if (pstmtUsuario != null) pstmtUsuario.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    // Método para deletar um usuário
    public boolean deletarUsuario(int idUsuario) {
        String sqlUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
        String sqlEndereco = "DELETE FROM endereco WHERE id_endereco = (SELECT id_endereco FROM usuario WHERE id_usuario = ?)";

        Connection conn = null;
        PreparedStatement pstmtUsuario = null;
        PreparedStatement pstmtEndereco = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // Deletar usuário
            pstmtUsuario = conn.prepareStatement(sqlUsuario);
            pstmtUsuario.setInt(1, idUsuario);
            int rowsAffected = pstmtUsuario.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                throw new SQLException("Deleção de usuário falhou, nenhuma linha afetada.");
            }

            // Deletar endereço
            pstmtEndereco = conn.prepareStatement(sqlEndereco);
            pstmtEndereco.setInt(1, idUsuario);
            rowsAffected = pstmtEndereco.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                throw new SQLException("Deleção de endereço falhou, nenhuma linha afetada.");
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (pstmtUsuario != null) pstmtUsuario.close();
                if (pstmtEndereco != null) pstmtEndereco.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
