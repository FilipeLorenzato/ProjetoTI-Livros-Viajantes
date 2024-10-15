package model;

public class MeusLivros {
    private int idPostagem;
    private Livro livro;
    private String dataPostagem;
    private String status;

    // Construtor vazio
    public MeusLivros() {
    }

    // Construtor com todos os campos
    public MeusLivros(int idPostagem, Livro livro, String dataPostagem, String status) {
        this.idPostagem = idPostagem;
        this.livro = livro;
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
        return "MeusLivros [idPostagem=" + idPostagem + ", livro=" + livro + ", dataPostagem=" + dataPostagem
                + ", status=" + status + "]";
    }
}
