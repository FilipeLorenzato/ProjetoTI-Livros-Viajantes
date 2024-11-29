package model;

public class Livro {

    private int idLivro;
    private String titulo;
    private String autor;
    private String genero;
    private String sinopse;
    private String imagem; // Adiciona um atributo para a imagem
    private Integer id_usuario;
    private String imagemBase64; // Add this field

    // Construtor vazio
    public Livro() {
    }

    // Construtor sem ID (para inserção)
    public Livro(String titulo, String autor, String genero, String sinopse, String imagem, Integer id_usuario) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
        this.imagem = imagem; // Inicializa o atributo imagem como null
        this.id_usuario = id_usuario;
    }

    // Construtor com todos os campos
    public Livro(int idLivro, String titulo, String autor, String genero, String sinopse, String imagem,
            Integer id_usuario) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
        this.imagem = imagem; // Inicializa o atributo imagem como null
        this.id_usuario = id_usuario;
    }

    // Getters e Setters
    public String getImagemBase64() {

        return imagemBase64;

    }

    public void setImagemBase64(String imagemBase64) {

        this.imagemBase64 = imagemBase64;

    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setidUsuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getidUsuario() {
        return id_usuario;
    }

    @Override
    public String toString() {
        return "Livro [idLivro=" + idLivro + ", titulo=" + titulo + ", autor=" + autor
                + ", genero=" + genero + ", sinopse=" + sinopse + ", imagem=" + imagem + "]";
    }
}
