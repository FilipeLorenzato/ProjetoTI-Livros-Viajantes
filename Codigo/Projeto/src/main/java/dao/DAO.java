/*package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "ti2cc";
    private static final String PASSWORD = "ti@cc";

    /**
     * Abre uma conexão com o banco de dados PostgreSQL.
     * 
     * @return Objeto Connection ou null se houver erro.

    public static Connection conectar() {
        String url = " ";
        String username = "trabalhoti2";
        String password = "Joaoepobe26@";
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados.
     * 
     * @param conexao Objeto Connection a ser fechado.
     * @return true se a conexão foi fechada com sucesso, false caso contrário.
     
    public boolean close(Connection conexao) {
        boolean status = false;
        if (conexao != null) {
            try {
                conexao.close();
                status = true;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return status;
    }
}