package com.example.gestorfacil.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "Material")
public class Material {

    @PrimaryKey(autoGenerate = true)
    public int IdMaterial;

    @ColumnInfo(name = "Nome")
    public String nome;

    @ColumnInfo(name = "Descricao")
    public String descricao;

    @ColumnInfo(name = "CustoUnitario")
    public double custoUnitario;  

    @ColumnInfo(name = "EstoqueMinimo")
    public int estoqueMinimo;

    @ColumnInfo(name = "TipoMaterial")
    public String tipoMaterial;

    @ColumnInfo(name = "DataCadastro")
    public Date dataCadastro; 

    @ColumnInfo(name = "Ativo")
    public int ativo;  

    @ColumnInfo(name = "TipoMedida")
    public String tipoMedida;

    public Material(String nome, String descricao, double custoUnitario, int estoqueMinimo, String tipoMaterial, String tipoMedida) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoUnitario = custoUnitario;
        this.estoqueMinimo = estoqueMinimo;
        this.tipoMaterial = tipoMaterial;
        this.ativo = 1;
        this.dataCadastro = new Date();
        this.tipoMedida = tipoMedida;
    }

    public Material() {
    }

    public int getIdMaterial() {
        return IdMaterial;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getCustoUnitario() {
        return custoUnitario;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public int getAtivo() {
        return ativo;
    }

    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setIdMaterial(int idMaterial) {
        IdMaterial = idMaterial;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCustoUnitario(double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }
}