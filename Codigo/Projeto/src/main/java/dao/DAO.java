package dao;

import java.sql.*;

public class DAO {
    public static Connection conectar() {
        String url = " ";
        String username = "trabalhoti2";
        String password = "Joaoepobe26@";
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
        }
        return conexao;
    }

    public boolean close(Connection conexao) {
        boolean status = false;
        try {
            conexao.close();
            status = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }
}