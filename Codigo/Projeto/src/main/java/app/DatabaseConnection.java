package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL de conexão corrigida para PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/livrosviajantes";
    private static final String USER = "postgres"; // Seu usuário do PostgreSQL
    private static final String PASSWORD = "1321"; // Sua senha do PostgreSQL

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
