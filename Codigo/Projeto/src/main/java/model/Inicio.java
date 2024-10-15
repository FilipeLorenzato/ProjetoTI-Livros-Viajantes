package model;

public class Inicio {
    private int totalLivrosPostados;
    private int totalTrocasRealizadas;
    private int livrosDisponiveis;
    private int livrosEmTroca;
    private int livrosConcluidos;

    // Construtor vazio
    public Inicio() {
    }

    // Construtor com todos os campos
    public Inicio(int totalLivrosPostados, int totalTrocasRealizadas, int livrosDisponiveis, int livrosEmTroca,
            int livrosConcluidos) {
        this.totalLivrosPostados = totalLivrosPostados;
        this.totalTrocasRealizadas = totalTrocasRealizadas;
        this.livrosDisponiveis = livrosDisponiveis;
        this.livrosEmTroca = livrosEmTroca;
        this.livrosConcluidos = livrosConcluidos;
    }

    // Getters e Setters

    public int getTotalLivrosPostados() {
        return totalLivrosPostados;
    }

    public void setTotalLivrosPostados(int totalLivrosPostados) {
        this.totalLivrosPostados = totalLivrosPostados;
    }

    public int getTotalTrocasRealizadas() {
        return totalTrocasRealizadas;
    }

    public void setTotalTrocasRealizadas(int totalTrocasRealizadas) {
        this.totalTrocasRealizadas = totalTrocasRealizadas;
    }

    public int getLivrosDisponiveis() {
        return livrosDisponiveis;
    }

    public void setLivrosDisponiveis(int livrosDisponiveis) {
        this.livrosDisponiveis = livrosDisponiveis;
    }

    public int getLivrosEmTroca() {
        return livrosEmTroca;
    }

    public void setLivrosEmTroca(int livrosEmTroca) {
        this.livrosEmTroca = livrosEmTroca;
    }

    public int getLivrosConcluidos() {
        return livrosConcluidos;
    }

    public void setLivrosConcluidos(int livrosConcluidos) {
        this.livrosConcluidos = livrosConcluidos;
    }

    @Override
    public String toString() {
        return "Inicio [totalLivrosPostados=" + totalLivrosPostados + ", totalTrocasRealizadas="
                + totalTrocasRealizadas + ", livrosDisponiveis=" + livrosDisponiveis + ", livrosEmTroca="
                + livrosEmTroca + ", livrosConcluidos=" + livrosConcluidos + "]";
    }
}
