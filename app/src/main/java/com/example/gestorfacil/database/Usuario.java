package com.example.gestorfacil.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "Usuario",
        indices = {@Index(value = {"Email"}, unique = true)}) 
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int UsuarioId;

    @ColumnInfo(name = "Nome")
    public String nome;

    @ColumnInfo(name = "Email")
    public String email;

    @ColumnInfo(name = "Senha")
    public String senha; 

    @ColumnInfo(name = "DataCriacao")
    public Date dataCriacao;  

    @ColumnInfo(name = "Tipo")
    public String tipo;  
}