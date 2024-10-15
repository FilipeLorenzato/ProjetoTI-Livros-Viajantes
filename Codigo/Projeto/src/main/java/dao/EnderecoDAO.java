package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import app.DatabaseConnection;
import model.Endereco;

public class EnderecoDAO {

    /**
     * Insere um novo endereço no banco de dados.
     * @param endereco Objeto Endereco a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserirEndereco(Endereco endereco) {
        String sql = "INSERT INTO endereco (cidade, rua, estado, cep, numero) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, endereco.getCidade());
            pstmt.setString(2, endereco.getRua());
            pstmt.setString(3, endereco.getEstado());
            pstmt.setString(4, endereco.getCep());
            pstmt.setString(5, endereco.getNumero());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    endereco.setIdEndereco(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir endereço: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca um endereço com base nos seus dados.
     * @param endereco Objeto Endereco com dados para busca.
     * @return Endereco encontrado ou null se não encontrado.
     */
    public Endereco buscarEnderecoPorDados(Endereco endereco) {
        String sql = "SELECT * FROM endereco WHERE cidade = ? AND rua = ? AND estado = ? AND cep = ? AND numero = ?";
        Endereco enderecoEncontrado = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, endereco.getCidade());
            pstmt.setString(2, endereco.getRua());
            pstmt.setString(3, endereco.getEstado());
            pstmt.setString(4, endereco.getCep());
            pstmt.setString(5, endereco.getNumero());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                enderecoEncontrado = new Endereco(
                        rs.getInt("id_endereco"),
                        rs.getString("cidade"),
                        rs.getString("rua"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getString("numero")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar endereço por dados: " + e.getMessage());
        }

        return enderecoEncontrado;
    }
}
