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

    @Query("UPDATE Estoque SET quantidade = quantidade - :quantidadeMovimentada where IdMaterial = :idMaterial and PosicaoEstoque = :posicaoEstoque")
    void baixarMaterialEstoque(int quantidadeMovimentada, int idMaterial, String posicaoEstoque);

    @Query("UPDATE Estoque SET quantidade = quantidade + :quantidadeMovimentada where IdMaterial = :idMaterial and PosicaoEstoque = :posicaoEstoque")
    void entradaMaterialEstoque(int quantidadeMovimentada, int idMaterial, String posicaoEstoque);

    @Query("SELECT * from Estoque where IdMaterial = :idProduto and PosicaoEstoque = :posicaoEstoque")
    boolean checkProdutoPosicao(int idProduto, String posicaoEstoque);

}