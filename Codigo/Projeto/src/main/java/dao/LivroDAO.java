package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Livro;

public class LivroDAO {

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

    /**
     * Insere um novo livro no banco de dados.
     * 
     * @param livro Objeto Livro a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserirLivro(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autor, genero, sinopse) VALUES (?, ?, ?, ?)";

        try (Connection conn = this.conectar();
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

        try (Connection conn = this.conectar();
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

    // Método para buscar livros por ID de usuário

    public List<Livro> buscarLivrosPorUsuario(int idUsuario) {
        List<Livro> livros = new ArrayList<>();
        String query = "SELECT * FROM livros WHERE id_usuario = ?"; // Supondo que há uma coluna id_usuario na tabela
                                                                    // livros

        try (Connection conn = this.conectar();
                PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_livro"); // Supondo que a tabela de livros tem essa coluna
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String genero = resultSet.getString("genero");
                String sinopse = resultSet.getString("sinopse");

                // Cria um novo objeto Livro e adiciona à lista
                Livro livro = new Livro(id, titulo, autor, genero, sinopse);
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ou trate a exceção conforme necessário
        }

        return livros;
    }

    // Método para buscar livros em alta com um limite especificado

    public List<Livro> buscarLivrosEmAlta(int limite) {
        List<Livro> livrosEmAlta = new ArrayList<>();
        String query = "SELECT * FROM livros ORDER BY popularidade DESC LIMIT ?"; // Supondo que há uma coluna
                                                                                  // popularidade

        try (Connection conn = this.conectar();
                PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, limite);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_livro"); // Supondo que a tabela de livros tem essa coluna
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String genero = resultSet.getString("genero");
                String sinopse = resultSet.getString("sinopse");

                // Cria um novo objeto Livro e adiciona à lista
                Livro livro = new Livro(id, titulo, autor, genero, sinopse);
                livrosEmAlta.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ou trate a exceção conforme necessário
        }

        return livrosEmAlta;
    }

    // Método para obter o maior ID de livro no banco de dados
    public int getMaxId() {
        int maxId = 0; // Inicializa o valor do máximo ID
        String query = "SELECT MAX(id_livro) AS max_id FROM livros"; // Substitua 'id_livro' e 'livros' conforme suas
                                                                     // colunas e tabela

        try (Connection conn = this.conectar();
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt("max_id"); // Obtém o valor do máximo ID
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção conforme necessário
        }

        return maxId; // Retorna o maior ID encontrado
    }

    /**
     * Busca todos os livros cadastrados.
     * 
     * @return Lista de livros.
     */
    public List<Livro> buscarTodosLivros() {
        String sql = "SELECT * FROM livro";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = this.conectar();
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

        try (Connection conn = this.conectar();
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

        try (Connection conn = this.conectar();
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
