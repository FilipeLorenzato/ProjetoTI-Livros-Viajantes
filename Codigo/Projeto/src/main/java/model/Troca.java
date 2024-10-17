package model;

public class Troca {
    private int idTroca;
    private int idLivro;
    private int usuarioOfertante;
    private int usuarioContemplado;
    private String status;

    // Construtor vazio
    public Troca() {
    }

    // Construtor completo
    public Troca(int idTroca, int idLivro, int usuarioOfertante, int usuarioContemplado, String status) {
        this.idTroca = idTroca;
        this.idLivro = idLivro;
        this.usuarioOfertante = usuarioOfertante;
        this.usuarioContemplado = usuarioContemplado;
        this.status = status;
    }

    // Getters e Setters
    public int getIdTroca() {
        return idTroca;
    }

    public void setIdTroca(int idTroca) {
        this.idTroca = idTroca;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getUsuarioOfertante() {
        return usuarioOfertante;
    }

    public void setUsuarioOfertante(int usuarioOfertante) {
        this.usuarioOfertante = usuarioOfertante;
    }

    public int getUsuarioContemplado() {
        return usuarioContemplado;
    }

    public void setUsuarioContemplado(int usuarioContemplado) {
        this.usuarioContemplado = usuarioContemplado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método para imprimir informações da troca
    public void imprimir() {
        System.out.println("ID Troca: " + idTroca);
        System.out.println("ID Livro: " + idLivro);
        System.out.println("Usuário Ofertante: " + usuarioOfertante);
        System.out.println("Usuário Contemplado: " + usuarioContemplado);
        System.out.println("Status: " + status);
    }
}
