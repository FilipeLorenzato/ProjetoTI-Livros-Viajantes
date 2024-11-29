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

public class LivroDAO extends DAO {

    private Connection conexao;

    public Connection conectarLivro() {
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

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "ti2cc";
    private static final String PASS = "ti@cc";

    /**
     * Insere um novo livro no banco de dados.
     */
    public boolean inserirLivro(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autor, genero, sinopse, imagem, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.conectarLivro(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getSinopse());
            pstmt.setString(5, livro.getImagem()); // Base64 string
            pstmt.setInt(6, livro.getidUsuario());

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
     */
    public Livro buscarLivroPorId(int idLivro) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        Livro livro = null;

        try (Connection conn = this.conectarLivro(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLivro);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"),
                        rs.getString("imagem"),
                        rs.getInt("id_usuario")); // Recupera a imagem do banco
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
        }

        return livro;
    }

    // Método para salvar o livro com a imagem base64
    public static void salvarLivroComImagem(String titulo, String autor, String genero, String sinopse, String imagemBase64, int idUsuario) {
        String sql = "INSERT INTO livro (titulo, autor, genero, sinopse, imagem, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, genero);
            pstmt.setString(4, sinopse);
            pstmt.setString(5, imagemBase64);  // Armazenar a base64 da imagem
            pstmt.setInt(6, idUsuario);

            pstmt.executeUpdate();
            System.out.println("Livro salvo com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar o livro com a imagem base64
    public static boolean buscarLivroPorImagem(String imagemBase64) {
        String sql = "SELECT * FROM livro WHERE imagem = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imagemBase64); // Comparar a base64 da imagem

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Livro encontrado
                System.out.println("Livro encontrado: " + rs.getString("titulo"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Livro não encontrado
    }

    // Método para buscar livros por título
    public Livro buscarPorTitulo(String titulo) {
        Livro livro = null;
        String sql = "SELECT * FROM public.livro WHERE titulo = ?"; // Busca exata

        try (Connection conn = this.conectarLivro(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Normalizar o título antes de buscar: remover espaços extras, converter para minúsculas
            titulo = titulo.replaceAll("\\s+", " ").trim();  // Remove espaços extras
            titulo = titulo.toLowerCase(); // Converte para minúsculas
            System.out.println("Título enviado para a consulta (após normalização): '" + titulo + "'");

            // Buscar no banco com correspondência exata
            stmt.setString(1, titulo);  // Passa o título normalizado

            // Executa a consulta
            ResultSet rs = stmt.executeQuery();

            // Verifica se encontrou o livro
            if (rs.next()) {
                livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"),
                        rs.getString("imagem"),
                        rs.getInt("id_usuario")
                );
                System.out.println("Livro encontrado: " + livro.getTitulo());
            } else {
                System.out.println("Livro não encontrado com o título exato: " + titulo);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar livro por título: " + e.getMessage());
        }

        return livro;
    }

    /**
     * Busca livros por ID de usuário.
     */
    public List<Livro> buscarLivrosPorUsuario(int idUsuario) {
        List<Livro> livros = new ArrayList<>();
        String query = "SELECT * FROM livro WHERE id_usuario = ?";

        try (Connection conn = this.conectarLivro(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Livro livro = new Livro(
                        resultSet.getInt("id_livro"),
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getString("genero"),
                        resultSet.getString("sinopse"),
                        resultSet.getString("imagem"),
                        resultSet.getInt("id_usuario")); // Recupera a imagem do banco

                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por ID de usuário: " + e.getMessage());
        }

        return livros;
    }

    /**
     * Busca livros em alta.
     */
    public List<Livro> buscarLivrosEmAlta(int limite) {
        List<Livro> livrosEmAlta = new ArrayList<>();
        String query = "SELECT * FROM livro ORDER BY popularidade DESC LIMIT ?";

        try (Connection conn = this.conectarLivro(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, limite);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Livro livro = new Livro(
                        resultSet.getInt("id_livro"),
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getString("genero"),
                        resultSet.getString("sinopse"),
                        resultSet.getString("imagem"),
                        resultSet.getInt("id_usuario")); // Recupera a imagem do banco

                livrosEmAlta.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros em alta: " + e.getMessage());
        }

        return livrosEmAlta;
    }

    /**
     * Busca todos os livros cadastrados.
     */
    public List<Livro> buscarTodosLivros() {
        String sql = "SELECT * FROM livro";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = this.conectarLivro(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("sinopse"),
                        rs.getString("imagem"),
                        rs.getInt("id_usuario")); // Recupera a imagem do banco

                livros.add(livro);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os livros: " + e.getMessage());
        }

        return livros;
    }

    /**
     * Atualiza as informações de um livro existente.
     */
    public boolean atualizarLivro(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, genero = ?, sinopse = ?, imagem = ? WHERE id_livro = ?";

        try (Connection conn = this.conectarLivro(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getSinopse());
            pstmt.setString(5, livro.getImagem());
            pstmt.setInt(6, livro.getIdLivro());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deleta um livro do banco de dados.
     */
    public boolean deletarLivro(int idLivro) {
        String sql = "DELETE FROM livro WHERE id_livro = ?";

        try (Connection conn = this.conectarLivro(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLivro);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar todos os livros no banco de dados
    public List<Livro> buscarTodos() {
        List<Livro> livros = new ArrayList<>(); // Lista para armazenar os livros
        String sql = "SELECT * FROM livro"; // Consulta SQL para buscar todos os livros

        try (Connection conn = this.conectarLivro(); PreparedStatement statement = conn.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) { // Executa a consulta

            while (resultSet.next()) { // Percorre o ResultSet
                // Cria um novo objeto Livro para cada registro
                Livro livro = new Livro();
                livro.setIdLivro(resultSet.getInt("id_livro")); // Supondo que você tenha um campo id_livro
                livro.setTitulo(resultSet.getString("titulo"));
                livro.setAutor(resultSet.getString("autor"));
                livro.setGenero(resultSet.getString("genero"));
                livro.setSinopse(resultSet.getString("sinopse"));

                livros.add(livro); // Adiciona o livro à lista
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage()); // Exibe erro, se houver
            throw new RuntimeException("Erro ao buscar livros: " + e.getMessage());
        }

        return livros; // Retorna a lista de livros
    }

    public boolean atualizarUsuarioLivro(int idLivro, int novoUsuarioId) {
        String sql = "UPDATE livro SET id_usuario = ? WHERE id_livro = ?";

        try (Connection conn = this.conectarLivro(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, novoUsuarioId);
            pstmt.setInt(2, idLivro);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar id_usuario do livro: " + e.getMessage());
            return false;
        }
    }

    // Método público para verificar se a imagem está no banco de dados
    public boolean isImageInDatabase(String imageName) {
        String sql = "SELECT COUNT(*) FROM livro WHERE imagem = ?";

        try (Connection conn = conectarLivro(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imageName.trim());  // Remover espaços extras
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);  // Obtém o valor de COUNT(*)
                    System.out.println("Contagem de imagens encontradas: " + count);  // Para debug
                    return count > 0;  // Retorna verdadeiro se a imagem existir
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Caso contrário, retorna falso
    }
}

/*
 * package dao;
 * 
 * import java.sql.Connection;
 * import java.sql.DriverManager;
 * import java.sql.PreparedStatement;
 * import java.sql.ResultSet;
 * import java.sql.SQLException;
 * import java.sql.Statement;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 * import model.Livro;
 * 
 * public class LivroDAO extends DAO {
 * 
 * private Connection conexao;
 * 
 * public Connection conectarLivro() {
 * String driverName = "org.postgresql.Driver";
 * String serverName = "localhost";
 * String mydatabase = "postgres";
 * int porta = 5432;
 * String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" +
 * mydatabase;
 * String username = "ti2cc";
 * String password = "ti@cc";
 * Connection conn = null;
 * 
 * try {
 * Class.forName(driverName);
 * conexao = DriverManager.getConnection(url, username, password);
 * conn = conexao;
 * System.out.println("Conexão efetuada com o postgres!");
 * } catch (ClassNotFoundException e) {
 * System.err.
 * println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " +
 * e.getMessage());
 * } catch (SQLException e) {
 * System.err.println("Conexão NÃO efetuada com o postgres -- " +
 * e.getMessage());
 * }
 * 
 * return conn;
 * }
 * 
 * /**
 * Insere um novo livro no banco de dados.
 * 
 * @param livro Objeto Livro a ser inserido.
 * 
 * @return true se a inserção for bem-sucedida, false caso contrário.
 * 
 * public boolean inserirLivro(Livro livro) {
 * String sql =
 * "INSERT INTO livro (titulo, autor, genero, sinopse, imagem) VALUES (?, ?, ?, ?, ?)"
 * ;
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement pstmt = conn.prepareStatement(sql,
 * Statement.RETURN_GENERATED_KEYS)) {
 * 
 * pstmt.setString(1, livro.getTitulo());
 * pstmt.setString(2, livro.getAutor());
 * pstmt.setString(3, livro.getGenero());
 * pstmt.setString(4, livro.getSinopse());
 * 
 * // Adiciona a imagem como um array de bytes
 * pstmt.setBytes(5, livro.getImagem()); // supondo que você tenha um método
 * getImagem() em Livro
 * 
 * int rowsAffected = pstmt.executeUpdate();
 * 
 * if (rowsAffected > 0) {
 * ResultSet rs = pstmt.getGeneratedKeys();
 * if (rs.next()) {
 * livro.setIdLivro(rs.getInt(1));
 * }
 * return true;
 * }
 * 
 * } catch (SQLException e) {
 * System.err.println("Erro ao inserir livro: " + e.getMessage());
 * }
 * 
 * return false;
 * }
 * 
 * /**
 * Busca um livro por ID.
 * 
 * @param idLivro ID do livro a ser buscado.
 * 
 * @return Objeto Livro se encontrado, null caso contrário.
 * 
 * public Livro buscarLivroPorId(int idLivro) {
 * String sql = "SELECT * FROM livro WHERE id_livro = ?";
 * Livro livro = null;
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement pstmt = conn.prepareStatement(sql)) {
 * 
 * pstmt.setInt(1, idLivro);
 * ResultSet rs = pstmt.executeQuery();
 * 
 * if (rs.next()) {
 * livro = new Livro(
 * rs.getInt("id_livro"),
 * rs.getString("titulo"),
 * rs.getString("autor"),
 * rs.getString("genero"),
 * rs.getString("sinopse"));
 * }
 * 
 * } catch (SQLException e) {
 * System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
 * }
 * 
 * return livro;
 * }
 * 
 * // Método para buscar livros por ID de usuário
 * 
 * public List<Livro> buscarLivrosPorUsuario(int idUsuario) {
 * List<Livro> livros = new ArrayList<>();
 * String query = "SELECT * FROM livros WHERE id_usuario = ?"; // Supondo que há
 * uma coluna id_usuario na tabela
 * // livros
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement preparedStatement = conn.prepareStatement(query)) {
 * preparedStatement.setInt(1, idUsuario);
 * ResultSet resultSet = preparedStatement.executeQuery();
 * 
 * while (resultSet.next()) {
 * int id = resultSet.getInt("id_livro"); // Supondo que a tabela de livros tem
 * essa coluna
 * String titulo = resultSet.getString("titulo");
 * String autor = resultSet.getString("autor");
 * String genero = resultSet.getString("genero");
 * String sinopse = resultSet.getString("sinopse");
 * 
 * // Cria um novo objeto Livro e adiciona à lista
 * Livro livro = new Livro(id, titulo, autor, genero, sinopse);
 * livros.add(livro);
 * }
 * } catch (SQLException e) {
 * e.printStackTrace(); // Ou trate a exceção conforme necessário
 * }
 * 
 * return livros;
 * }
 * 
 * // Método para buscar livros em alta com um limite especificado
 * 
 * public List<Livro> buscarLivrosEmAlta(int limite) {
 * List<Livro> livrosEmAlta = new ArrayList<>();
 * String query = "SELECT * FROM livros ORDER BY popularidade DESC LIMIT ?"; //
 * Supondo que há uma coluna
 * // popularidade
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement preparedStatement = conn.prepareStatement(query)) {
 * preparedStatement.setInt(1, limite);
 * ResultSet resultSet = preparedStatement.executeQuery();
 * 
 * while (resultSet.next()) {
 * int id = resultSet.getInt("id_livro"); // Supondo que a tabela de livros tem
 * essa coluna
 * String titulo = resultSet.getString("titulo");
 * String autor = resultSet.getString("autor");
 * String genero = resultSet.getString("genero");
 * String sinopse = resultSet.getString("sinopse");
 * 
 * // Cria um novo objeto Livro e adiciona à lista
 * Livro livro = new Livro(id, titulo, autor, genero, sinopse);
 * livrosEmAlta.add(livro);
 * }
 * } catch (SQLException e) {
 * e.printStackTrace(); // Ou trate a exceção conforme necessário
 * }
 * 
 * return livrosEmAlta;
 * }
 * 
 * // Método para obter o maior ID de livro no banco de dados
 * public int getMaxId() {
 * int maxId = 0; // Inicializa o valor do máximo ID
 * String query = "SELECT MAX(id_livro) AS max_id FROM livros"; // Substitua
 * 'id_livro' e 'livros' conforme suas
 * // colunas e tabela
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement preparedStatement = conn.prepareStatement(query);
 * ResultSet resultSet = preparedStatement.executeQuery()) {
 * 
 * if (resultSet.next()) {
 * maxId = resultSet.getInt("max_id"); // Obtém o valor do máximo ID
 * }
 * } catch (SQLException e) {
 * e.printStackTrace(); // Trate a exceção conforme necessário
 * }
 * 
 * return maxId; // Retorna o maior ID encontrado
 * }
 * 
 * /**
 * Busca todos os livros cadastrados.
 * 
 * @return Lista de livros.
 * 
 * public List<Livro> buscarTodosLivros() {
 * String sql = "SELECT * FROM livro";
 * List<Livro> livros = new ArrayList<>();
 * 
 * try (Connection conn = this.conectar();
 * Statement stmt = conn.createStatement();
 * ResultSet rs = stmt.executeQuery(sql)) {
 * 
 * while (rs.next()) {
 * Livro livro = new Livro(
 * rs.getInt("id_livro"),
 * rs.getString("titulo"),
 * rs.getString("autor"),
 * rs.getString("genero"),
 * rs.getString("sinopse"));
 * livros.add(livro);
 * }
 * 
 * } catch (SQLException e) {
 * System.err.println("Erro ao buscar todos os livros: " + e.getMessage());
 * }
 * 
 * return livros;
 * }
 * 
 * /**
 * Atualiza as informações de um livro existente.
 * 
 * @param livro Objeto Livro com as informações atualizadas.
 * 
 * @return true se a atualização for bem-sucedida, false caso contrário.
 * 
 * public boolean atualizarLivro(Livro livro) {
 * String sql =
 * "UPDATE livro SET titulo = ?, autor = ?, genero = ?, sinopse = ? WHERE id_livro = ?"
 * ;
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement pstmt = conn.prepareStatement(sql)) {
 * 
 * pstmt.setString(1, livro.getTitulo());
 * pstmt.setString(2, livro.getAutor());
 * pstmt.setString(3, livro.getGenero());
 * pstmt.setString(4, livro.getSinopse());
 * pstmt.setInt(5, livro.getIdLivro());
 * 
 * int rowsAffected = pstmt.executeUpdate();
 * return rowsAffected > 0;
 * 
 * } catch (SQLException e) {
 * System.err.println("Erro ao atualizar livro: " + e.getMessage());
 * return false;
 * }
 * }
 * 
 * /**
 * Deleta um livro do banco de dados.
 * 
 * @param idLivro ID do livro a ser deletado.
 * 
 * @return true se a deleção for bem-sucedida, false caso contrário.
 * 
 * public boolean deletarLivro(int idLivro) {
 * String sql = "DELETE FROM livro WHERE id_livro = ?";
 * 
 * try (Connection conn = this.conectar();
 * PreparedStatement pstmt = conn.prepareStatement(sql)) {
 * 
 * pstmt.setInt(1, idLivro);
 * int rowsAffected = pstmt.executeUpdate();
 * return rowsAffected > 0;
 * 
 * } catch (SQLException e) {
 * System.err.println("Erro ao deletar livro: " + e.getMessage());
 * return false;
 * }
 * }
 * }
 */
