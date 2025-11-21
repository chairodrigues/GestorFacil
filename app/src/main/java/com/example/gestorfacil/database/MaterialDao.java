package com.example.gestorfacil.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface MaterialDao {

    @Insert
    void insert(Material material);

    @Update
    void update(Material material);

    @Delete
    void delete(Material material);

    @Query("SELECT * FROM Material")
    List<Estoque> getAllMaterial();

    @Query("SELECT * FROM Material WHERE IdMaterial = :id")
    Material getMaterialById(int id);
}
