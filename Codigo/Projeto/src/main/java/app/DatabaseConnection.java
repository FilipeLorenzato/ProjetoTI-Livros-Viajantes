package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL de conexão corrigida para PostgreSQL
    private static final String URL = "http://localhost/phppgadmin/";
    private static final String USER = "ti2cc"; // Seu usuário do PostgreSQL
    private static final String PASSWORD = "ti@cc"; // Sua senha do PostgreSQL

    static {
        try {
            // Carrega o driver PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver PostgreSQL: " + e.getMessage());
        }
    }

    /**
     * Obtém uma conexão com o banco de dados.
     * 
     * @return Objeto Connection.
     * @throws SQLException Se ocorrer um erro ao conectar.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
