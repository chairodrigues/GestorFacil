package com.example.gestorfacil.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Query("SELECT * FROM Usuario ORDER BY Nome ASC")
    List<Usuario> getAllUsuarios();

    @Query("SELECT * FROM Usuario WHERE UsuarioId = :id")
    Usuario getUsuarioById(int id);

    @Query("SELECT * FROM Usuario WHERE Email = :email LIMIT 1")
    Usuario getUsuarioByEmail(String email);

    @Query("Select * from Usuario where email = :email and senha = :senha")
    Usuario checkLogin(String email, String senha);
}