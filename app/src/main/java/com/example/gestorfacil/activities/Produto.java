package com.example.gestorfacil.activities;

public class Produto {
    private String id;
    private String nome;
    private String categoria;
    private int quantidade;
    private double precoCusto;
    private String fornecedor;

    // Construtor vazio (necessário para algumas bibliotecas como Firebase)
    public Produto() {}

    public Produto(String id, String nome, String categoria, int quantidade,
                   double precoCusto, String fornecedor) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.fornecedor = fornecedor;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoCusto() { return precoCusto; }
    public String getFornecedor() { return fornecedor; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
}
