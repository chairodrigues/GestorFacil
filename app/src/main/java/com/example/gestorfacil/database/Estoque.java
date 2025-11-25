package com.example.gestorfacil.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "Estoque", 
        foreignKeys = @ForeignKey(
                entity = Material.class,
                parentColumns = "IdMaterial",
                childColumns = "IdMaterial",
                onDelete = ForeignKey.RESTRICT,  
                onUpdate = ForeignKey.CASCADE   
        ), 
        indices = {@Index("IdMaterial")}
)
public class Estoque {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "PosicaoEstoque")
    public String posicaoEstoque;

    @ColumnInfo(name = "Quantidade")
    public int quantidade;

    @ColumnInfo(name = "IdMaterial")  
    public int idMaterial;

    @ColumnInfo(name = "ValorTotal")
    public double valorTotal;  

    @ColumnInfo(name = "DataAtualizacao")
    public Date dataAtualizacao;

    public int getId() {
        return Id;
    }

    public String getPosicaoEstoque() {
        return posicaoEstoque;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setPosicaoEstoque(String posicaoEstoque) {
        this.posicaoEstoque = posicaoEstoque;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}