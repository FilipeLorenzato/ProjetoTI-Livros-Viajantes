package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private String telefone;
    private String cidade;
    private String rua;
    private String estado;
    private String cep;
    private String numero;

    // Construtor vazio
    public Usuario() {
    }

    // Construtor sem ID (para inserção)
    public Usuario(String nome, String email, String senha, LocalDate dataNascimento,
            String telefone, String cidade, String rua, String estado, String cep, String numero) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.cidade = cidade;
        this.rua = rua;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
    }

    // Construtor com todos os campos (inclui o ID do usuário)
    public Usuario(int idUsuario, String nome, String email, String senha, LocalDate dataNascimento,
            String telefone, String cidade, String rua, String estado, String cep, String numero) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.cidade = cidade;
        this.rua = rua;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
    }

    // Getters e Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(String dataNascimentoStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // Endereço
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

    // Sobrescrevendo toString para formatar a exibição dos dados do usuário
    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", nome=" + nome + ", email=" + email +
                ", dataNascimento=" + dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", telefone=" + telefone + ", endereco=" + cidade + ", " + rua + ", " + estado +
                ", " + cep + ", " + numero + "]";
    }

    // Método para imprimir o usuário no console
    public void imprimir() {
        System.out.println(this.toString());
    }
}
