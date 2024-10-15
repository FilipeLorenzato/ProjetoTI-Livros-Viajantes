package service;

import java.util.List;

import dao.EnderecoDAO;
import dao.UsuarioDAO;
import model.Endereco;
import model.Usuario;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;
    private EnderecoDAO enderecoDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.enderecoDAO = new EnderecoDAO();
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        // Primeiro, insere o endereço
        Endereco endereco = usuario.getEndereco();
        boolean enderecoInserido = enderecoDAO.inserirEndereco(endereco);
        if (!enderecoInserido) {
            return false;
        }

        // Agora, insere o usuário com o ID do endereço
        return usuarioDAO.inserirUsuario(usuario);
    }

    public Usuario getUsuarioById(int idUsuario) {
        return usuarioDAO.buscarUsuarioPorId(idUsuario);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioDAO.buscarTodosUsuarios();
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioDAO.buscarUsuarioPorEmail(email);
        if (usuario != null) {
            // Em produção, a senha deve ser hasheada e comparada
            if (usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        // Validações ou lógica adicional podem ser adicionadas aqui
        return usuarioDAO.atualizarUsuario(usuario);
    }

    public boolean deletarUsuario(int idUsuario) {
        return usuarioDAO.deletarUsuario(idUsuario);
    }
}
