package com.example.gestorfacil.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MovimentacaoDao {
 
    @Insert
    void insert(Movimentacao movimentacao);
 
    @Query("SELECT * FROM Movimentacao ORDER BY DataMovimentacao DESC")
    List<Movimentacao> getAllMovimentacoes();
 
    @Query("SELECT * FROM Movimentacao WHERE IdMaterial = :materialId ORDER BY DataMovimentacao DESC")
    List<Movimentacao> getMovimentacoesPorMaterial(int materialId);
}