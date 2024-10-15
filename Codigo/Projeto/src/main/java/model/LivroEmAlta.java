package model;

public class LivroEmAlta {
    private Livro livro;
    private int quantidadeTrocas;

    public LivroEmAlta() { }

    public LivroEmAlta(Livro livro, int quantidadeTrocas) {
        this.livro = livro;
        this.quantidadeTrocas = quantidadeTrocas;
    }

    // Getters e Setters

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public int getQuantidadeTrocas() {
        return quantidadeTrocas;
    }

    public void setQuantidadeTrocas(int quantidadeTrocas) {
        this.quantidadeTrocas = quantidadeTrocas;
    }

    @Override
    public String toString() {
        return "LivroEmAlta [livro=" + livro + ", quantidadeTrocas=" + quantidadeTrocas + "]";
    }
}
