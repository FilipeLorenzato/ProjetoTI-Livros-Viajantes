package model;

public class Livro {
    private int idLivro;
    private String titulo;
    private String autor;
    private String genero;
    private String sinopse;

    // Construtor vazio
    public Livro() { }

    // Construtor sem ID (para inserção)
    public Livro(String titulo, String autor, String genero, String sinopse) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
    }

    // Construtor com todos os campos
    public Livro(int idLivro, String titulo, String autor, String genero, String sinopse) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
    }

    // Getters e Setters

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
    

    @Override
    public String toString() {
        return "Livro [idLivro=" + idLivro + ", titulo=" + titulo + ", autor=" + autor + 
               ", genero=" + genero + ", sinopse=" + sinopse + "]";
    }
}
