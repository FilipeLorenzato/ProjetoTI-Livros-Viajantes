package model;

public class Endereco {
    private int idEndereco;
    private String cidade;
    private String rua;
    private String estado;
    private String cep;
    private String numero;

    // Construtor vazio
    public Endereco() {
    }

    // Construtor sem ID (para inserção)
    public Endereco(String cidade, String rua, String estado, String cep, String numero) {
        this.cidade = cidade;
        this.rua = rua;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
    }

    // Construtor com ID
    public Endereco(int idEndereco, String cidade, String rua, String estado, String cep, String numero) {
        this.idEndereco = idEndereco;
        this.cidade = cidade;
        this.rua = rua;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
    }

    // Getters e Setters

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Endereco [idEndereco=" + idEndereco + ", cidade=" + cidade + ", rua=" + rua +
                ", estado=" + estado + ", cep=" + cep + ", numero=" + numero + "]";
    }
}
