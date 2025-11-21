package com.example.gestorfacil.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "Movimentacao", 
        foreignKeys = {
            @ForeignKey(
                entity = Material.class,
                parentColumns = "IdMaterial",
                childColumns = "IdMaterial",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE
            ),
            @ForeignKey(
                entity = Usuario.class,
                parentColumns = "UsuarioId",
                childColumns = "UsuarioId",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE
            )
        }, 
        indices = {@Index("IdMaterial"), @Index("UsuarioId")}
)
public class Movimentacao {

    @PrimaryKey(autoGenerate = true)
    public int IdMovimentacao;

    @ColumnInfo(name = "DataCriacao")
    public Date dataCriacao;

    @ColumnInfo(name = "IdMaterial")  
    public int idMaterial;

    @ColumnInfo(name = "UsuarioId") 
    public int usuarioId;

    @ColumnInfo(name = "Quantidade")
    public int quantidade;

    @ColumnInfo(name = "EntradaOuSaida")
    public String entradaOuSaida;  

    @ColumnInfo(name = "CustoTotal")
    public double custoTotal;

    @ColumnInfo(name = "DataMovimentacao")
    public Date dataMovimentacao;

    @ColumnInfo(name = "Observacao")
    public String observacao;

    public Movimentacao(int idMaterial, int usuarioId, int quantidade, String entradaOuSaida, double custoTotal, String observacao) {

        this.dataCriacao = new Date();
        this.idMaterial = idMaterial;
        this.usuarioId = usuarioId;
        this.quantidade = quantidade;
        this.entradaOuSaida = entradaOuSaida;
        this.custoTotal = custoTotal;
        this.dataMovimentacao = new Date();
        this.observacao = observacao;
    }

    public Movimentacao() {
    }
}