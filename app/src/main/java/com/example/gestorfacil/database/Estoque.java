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
}