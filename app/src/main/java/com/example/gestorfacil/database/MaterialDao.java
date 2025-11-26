package com.example.gestorfacil.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MaterialDao {

    @Insert
    void insert(Material material);

    @Update
    void update(Material material);

    @Delete
    void delete(Material material);

    @Query("SELECT * FROM Material")
    List<Material> getAllMaterial();

    @Query("Select nome from Material")
    List<String> getNomesMaterial();

    @Query("SELECT * FROM Material WHERE IdMaterial = :id")
    Material getMaterialById(int id);

    @Query("SELECT CustoUnitario FROM Material WHERE IdMaterial = :id")
    Double getCustoMaterial(int id);
}
