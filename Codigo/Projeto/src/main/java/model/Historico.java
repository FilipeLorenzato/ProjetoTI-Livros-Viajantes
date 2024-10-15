package model;

public class Historico {
    private int idTroca;
    private Livro livro;
    private Usuario usuarioOfertante;
    private Usuario usuarioContemplado;
    private String status;
    private String dataTroca; // Supondo que exista uma data para a troca

    // Construtor vazio
    public Historico() {
    }

    // Construtor sem ID (para inserção, se necessário)
    public Historico(Livro livro, Usuario usuarioOfertante, Usuario usuarioContemplado, String status,
            String dataTroca) {
        this.livro = livro;
        this.usuarioOfertante = usuarioOfertante;
        this.usuarioContemplado = usuarioContemplado;
        this.status = status;
        this.dataTroca = dataTroca;
    }

    // Construtor com todos os campos
    public Historico(int idTroca, Livro livro, Usuario usuarioOfertante, Usuario usuarioContemplado, String status,
            String dataTroca) {
        this.idTroca = idTroca;
        this.livro = livro;
        this.usuarioOfertante = usuarioOfertante;
        this.usuarioContemplado = usuarioContemplado;
        this.status = status;
        this.dataTroca = dataTroca;
    }

    // Getters e Setters

    public int getIdTroca() {
        return idTroca;
    }

    public void setIdTroca(int idTroca) {
        this.idTroca = idTroca;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuarioOfertante() {
        return usuarioOfertante;
    }

    public void setUsuarioOfertante(Usuario usuarioOfertante) {
        this.usuarioOfertante = usuarioOfertante;
    }

    public Usuario getUsuarioContemplado() {
        return usuarioContemplado;
    }

    public void setUsuarioContemplado(Usuario usuarioContemplado) {
        this.usuarioContemplado = usuarioContemplado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataTroca() {
        return dataTroca;
    }

    public void setDataTroca(String dataTroca) {
        this.dataTroca = dataTroca;
    }

    @Override
    public String toString() {
        return "Historico [idTroca=" + idTroca + ", livro=" + livro + ", usuarioOfertante=" + usuarioOfertante
                + ", usuarioContemplado=" + usuarioContemplado + ", status=" + status + ", dataTroca=" + dataTroca
                + "]";
    }
}
