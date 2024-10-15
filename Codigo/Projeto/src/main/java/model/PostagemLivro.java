package model;

public class PostagemLivro {
    private int idPostagem;
    private Livro livro;
    private Usuario usuarioPostante;
    private String dataPostagem; // Data em que o livro foi postado
    private String status; // Por exemplo: "Disponível", "Em Troca", etc.

    // Construtor vazio
    public PostagemLivro() {
    }

    // Construtor sem ID (para inserção)
    public PostagemLivro(Livro livro, Usuario usuarioPostante, String dataPostagem, String status) {
        this.livro = livro;
        this.usuarioPostante = usuarioPostante;
        this.dataPostagem = dataPostagem;
        this.status = status;
    }

    // Construtor com todos os campos
    public PostagemLivro(int idPostagem, Livro livro, Usuario usuarioPostante, String dataPostagem, String status) {
        this.idPostagem = idPostagem;
        this.livro = livro;
        this.usuarioPostante = usuarioPostante;
        this.dataPostagem = dataPostagem;
        this.status = status;
    }

    // Getters e Setters

    public int getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(int idPostagem) {
        this.idPostagem = idPostagem;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuarioPostante() {
        return usuarioPostante;
    }

    public void setUsuarioPostante(Usuario usuarioPostante) {
        this.usuarioPostante = usuarioPostante;
    }

    public String getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(String dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PostagemLivro [idPostagem=" + idPostagem + ", livro=" + livro + ", usuarioPostante=" + usuarioPostante
                + ", dataPostagem=" + dataPostagem + ", status=" + status + "]";
    }
}
