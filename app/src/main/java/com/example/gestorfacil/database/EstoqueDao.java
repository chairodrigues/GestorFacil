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

    @Query("Delete from estoque where Id = :id")
    void deletarId(int id);

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

    @Query("SELECT * FROM Estoque where PosicaoEstoque = :posicaoEstoque")
    boolean checkPosicao (String posicaoEstoque);

    @Query("SELECT Id FROM Estoque WHERE PosicaoEstoque = :posicaoEstoque")
    int getIdPosicao (String posicaoEstoque);

    @Query("SELECT * FROM Estoque Where PosicaoEstoque = :posicaoEstoque and Quantidade = 0")
    boolean checkPosicaoLivre(String posicaoEstoque);

    @Query("SELECT * from Estoque where IdMaterial = :idProduto and PosicaoEstoque = :posicaoEstoque and Quantidade >= :quantidadeProduto")
    boolean checkProdutoPosicaoQuantidade(int idProduto, String posicaoEstoque, int quantidadeProduto);

    @Query("UPDATE Estoque set ValorTotal = ValorTotal + :valorTotalProduto where Id = :idEstoque")
    void atualizarValorTotal(double valorTotalProduto, int idEstoque);

}