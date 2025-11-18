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
}