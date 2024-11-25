package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO extends DAO {
	private List<Usuario> usuarios;
	private Connection conexao;
	private int maxId = 0;

	public UsuarioDAO() {
		conexao = null;
		this.usuarios = new ArrayList<Usuario>();
	}

	public int getMaxId() {
		return this.maxId;
	}

	public void incrementMaxId() {
		this.maxId++;
	}

	public Connection conectarUsuario() {
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

	public boolean close() {
		boolean status = false;

		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public Usuario getByEmail(String email) {
		for (Usuario usuario : usuarios) {
			if (email.equals(usuario.getEmail())) {
				return usuario;
			}
		}
		return null;
	}

	public Usuario buscarPorEmail(String email) {
		Usuario usuario = null;
		String sql = "SELECT * FROM Usuario WHERE email = ?";
		try (Connection conn = this.conectarUsuario();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				usuario = new Usuario(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("senha"),
						rs.getString("nome"),
						rs.getString("telefone"),
						rs.getString("rua"),
						rs.getString("cidade"),
						rs.getString("estado"),
						rs.getString("cep"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuario;
	}

	public Usuario get(String nome) {
		for (Usuario usuario : usuarios) {
			if (nome.equals(usuario.getNome())) {
				return usuario;
			}
		}
		return null;
	}

	public List<Usuario> getAll() {
		return usuarios;
	}

	private List<Usuario> getFromDB() {
		usuarios.clear();
		Usuario usuario = null;
		try {
			Usuario usuarios[] = this.getUsuarios();
			System.out.println(usuarios);
			for (int i = 0; i < usuarios.length; i++) {
				usuario = (Usuario) usuarios[i];
				this.usuarios.add(usuario);
			}
		} catch (Exception e) {
			System.out.println("ERRO ao gravar usuario no disco!");
			e.printStackTrace();
		}
		return usuarios;
	}

	private Connection getConexao() {
		if (conexao == null) {
			conectar();
		}
		return conexao;
	}

	
public boolean inserirUsuario(Usuario usuario) {
    String sql = "INSERT INTO usuario (nome, email, senha, telefone, rua, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = conectar();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        
        // Criptografando a senha
        String hashedPassword = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());

        statement.setString(1, usuario.getNome());
        statement.setString(2, usuario.getEmail());
        statement.setString(3, hashedPassword); // Armazena a senha criptografada
        statement.setString(4, usuario.getTelefone());
        statement.setString(5, usuario.getrua());
        statement.setString(6, usuario.getCidade());
        statement.setString(7, usuario.getEstado());
        statement.setString(8, usuario.getCep());

        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        System.out.println("Erro ao inserir usuário: " + e.getMessage());
        return false;
    }
}
	

	public boolean atualizarUsuario(Usuario usuario) {
		boolean status = false;
		try {
			String sql = "UPDATE usuario SET nome = ?,  email = ?, senha = ?, telefone = ?, rua = ?, cidade = ?, estado = ?, cep = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, usuario.getNome());
			st.setString(2, usuario.getEmail());
			st.setString(3, usuario.getSenha());
			st.setString(4, usuario.getTelefone());
			st.setString(5, usuario.getrua());
			st.setString(6, usuario.getCidade());
			st.setString(7, usuario.getEstado());
			st.setString(8, usuario.getCep());
			int rowsUpdated = st.executeUpdate();
			st.close();

			if (rowsUpdated > 0) {
				status = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return status;
	}

	public boolean excluirUsuario(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public Usuario[] getUsuarios() {
		Usuario[] usuarios = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario");
			if (rs.next()) {
				rs.last();
				usuarios = new Usuario[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					usuarios[i] = new Usuario(rs.getInt("id"), rs.getString("email"), rs.getString("senha"),
							rs.getString("nome"), rs.getString("telefone"), rs.getString("rua"),
							rs.getString("cidade"), rs.getString("estado"), rs.getString("cep"));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuarios;
	}

	public Usuario[] getUsuariosByCidade(String cidade) {
		Usuario[] usuarios = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE cidade = '" + cidade + "'");
			if (rs.next()) {
				rs.last();
				usuarios = new Usuario[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					usuarios[i] = new Usuario(rs.getInt("id"), rs.getString("email"), rs.getString("senha"),
							rs.getString("nome"), rs.getString("telefone"), rs.getString("rua"),
							rs.getString("cidade"), rs.getString("estado"), rs.getString("cep"));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuarios;
	}

	public Usuario getById(int id) {
		Usuario usuario = null;
		try {
			Statement st = conexao.createStatement();
			String sql = "SELECT * FROM usuario WHERE id = " + id;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				// Extrair os dados do usuário do ResultSet
				String email = rs.getString("email");
				String senha = rs.getString("senha");
				String nome = rs.getString("nome");
				String telefone = rs.getString("telefone");
				String rua = rs.getString("rua");
				String cidade = rs.getString("cidade");
				String estado = rs.getString("estado");
				String cep = rs.getString("cep");
				// Criar o objeto Usuario com os dados obtidos
				usuario = new Usuario(id, email, senha, nome, telefone, rua, cidade, estado, cep);
			}
			st.close();
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return usuario;
	}

	public List<Usuario> listarUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();
		String sql = "SELECT id, email, senha, nome, telefone, rua, cidade, estado, cep FROM usuario";

		try (PreparedStatement stmt = conexao.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String email = rs.getString("email");
				String senha = rs.getString("senha");
				String nome = rs.getString("nome");
				String telefone = rs.getString("telefone");
				String rua = rs.getString("rua");
				String cidade = rs.getString("cidade");
				String estado = rs.getString("estado");
				String cep = rs.getString("cep");

				Usuario usuario = new Usuario(id, email, senha, nome, telefone, rua, cidade, estado, cep);
				usuarios.add(usuario);
			}

		} catch (SQLException e) {
			System.out.println("Erro ao listar usuários: " + e.getMessage());
		}

		return usuarios;
	}

}
