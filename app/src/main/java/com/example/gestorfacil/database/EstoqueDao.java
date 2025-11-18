package com.example.gestorfacil.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface EstoqueDao {

    @Insert
    void insert(Estoque estoque);

    @Update
    void update(Estoque estoque);

    @Delete
    void delete(Estoque estoque);

    @Query("SELECT * FROM Estoque")
    List<Estoque> getAllEstoque();
 
    @Query("SELECT * FROM Estoque WHERE IdMaterial = :materialId LIMIT 1")
    Estoque getEstoquePorMaterialId(int materialId);
}