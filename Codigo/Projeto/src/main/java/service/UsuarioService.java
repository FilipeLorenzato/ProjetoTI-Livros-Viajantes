package service;

import java.util.List;

import dao.UsuarioDAO;
import model.Usuario;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        // Validação do usuário e outros checks antes de inserção
        if (usuario == null || usuario.getNome().isEmpty() || usuario.getEmail().isEmpty()) {
            return false;
        }

        // Insere o usuário diretamente, assumindo que o endereço faz parte do modelo de
        // usuário
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
