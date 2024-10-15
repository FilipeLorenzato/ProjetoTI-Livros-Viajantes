package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import app.DatabaseConnection;
import model.Livro;
import model.PostagemLivro;
import model.Usuario;

public class PostagemLivroDAO {

    /**
     * Insere uma nova postagem de livro no banco de dados.
     * 
     * @param postagemLivro Objeto PostagemLivro a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserirPostagemLivro(PostagemLivro postagemLivro) {
        String sql = "INSERT INTO postagem_livro (id_livro, id_usuario_postante, data_postagem, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, postagemLivro.getLivro().getIdLivro());
            pstmt.setInt(2, postagemLivro.getUsuarioPostante().getIdUsuario());
            pstmt.setString(3, postagemLivro.getDataPostagem());
            pstmt.setString(4, postagemLivro.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    postagemLivro.setIdPostagem(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir postagem de livro: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca todas as postagens de livros.
     * 
     * @return Lista de PostagemLivro.
     */
    public List<PostagemLivro> buscarTodasPostagens() {
        String sql = "SELECT pl.id_postagem, pl.data_postagem, pl.status, " +
                "l.id_livro, l.titulo, l.autor, l.genero, l.sinopse, " +
                "u.id_usuario, u.nome, u.email, u.senha, u.data_nascimento, u.telefone " +
                "FROM postagem_livro pl " +
                "JOIN livro l ON pl.id_livro = l.id_livro " +
                "JOIN usuario u ON pl.id_usuario_postante = u.id_usuario " +
                "ORDER BY pl.data_postagem DESC";
        List<PostagemLivro> postagens = new ArrayList<>();

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

                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        LocalDate.parse(rs.getString("data_nascimento")),
                        rs.getString("telefone"),
                        null, // Endereço pode ser adicionado se necessário
                        null, null, null, null // Adicione valores padrão ou necessários para os outros campos
                );

                PostagemLivro postagem = new PostagemLivro(
                        rs.getInt("id_postagem"),
                        livro,
                        usuario,
                        rs.getString("data_postagem"),
                        rs.getString("status"));

                postagens.add(postagem);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar postagens de livros: " + e.getMessage());
        }

        return postagens;
    }

    /**
     * Busca postagens de livros por usuário.
     * 
     * @param idUsuario ID do usuário.
     * @return Lista de PostagemLivro.
     */
    public List<PostagemLivro> buscarPostagensPorUsuario(int idUsuario) {
        String sql = "SELECT pl.id_postagem, pl.data_postagem, pl.status, " +
                "l.id_livro, l.titulo, l.autor, l.genero, l.sinopse, " +
                "u.id_usuario, u.nome, u.email, u.senha, u.data_nascimento, u.telefone " +
                "FROM postagem_livro pl " +
                "JOIN livro l ON pl.id_livro = l.id_livro " +
                "JOIN usuario u ON pl.id_usuario_postante = u.id_usuario " +
                "WHERE u.id_usuario = ? " +
                "ORDER BY pl.data_postagem DESC";
        List<PostagemLivro> postagens = new ArrayList<>();

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

                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        LocalDate.parse(rs.getString("data_nascimento")),
                        rs.getString("telefone"),
                        null, null, null, null, null // Adicione valores padrão ou necessários para os outros campos
                );

                PostagemLivro postagem = new PostagemLivro(
                        rs.getInt("id_postagem"),
                        livro,
                        usuario,
                        rs.getString("data_postagem"),
                        rs.getString("status"));

                postagens.add(postagem);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar postagens de livros por usuário: " + e.getMessage());
        }

        return postagens;
    }

    // Métodos adicionais como atualizar status, deletar postagem etc., podem ser
    // adicionados conforme necessário
}
