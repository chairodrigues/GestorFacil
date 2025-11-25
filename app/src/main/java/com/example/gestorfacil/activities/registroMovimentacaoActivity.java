package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Estoque;
import com.example.gestorfacil.database.Material;
import com.example.gestorfacil.database.Movimentacao;

public class registroMovimentacaoActivity extends AppCompatActivity {

    private String editProduto, editPosicaoEstoque, editQuantidadeMovimentada, editObservacao, entradaSaida;
    private int idUsuario = 123;
    private Button btnRegistrar, btnCancelar;
    private RadioButton btnSaida, btnEntrada;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao);

        /**editProduto = findViewById();
        editPosicaoEstoque = findViewById();
        editQuantidadeMovimentada = findViewById();
        editObservacao = findViewById();**/

        btnSaida = findViewById(R.id.btnEntrada);
        btnEntrada = findViewById(R.id.btnSaida);

        db = AppDatabase.getDatabase(getApplicationContext());

        btnEntrada.setOnCheckedChangeListener((buttonView, isChecked) -> {

            entradaSaida = "Entrada";

        });

        btnSaida.setOnCheckedChangeListener((buttonView, isChecked) -> {

            entradaSaida = "Saida";

        });

        btnRegistrar.setOnClickListener(v -> {

            /**String produto = editProduto.getText().toString();
            String posicaoEstoque = editPosicaoEstoque.getText().toString();
            String quantidadeMovimentada = editQuantidadeMovimentada.getText().toString();
            String observacao = editObservacao.getText().toString();**/

            String produto = "1";
            String posicaoEstoque = "teste";
            String quantidadeMovimentada = "1";
            String observacao = "teste";

            if(produto.isEmpty() || posicaoEstoque.isEmpty() || quantidadeMovimentada.isEmpty() || entradaSaida.isEmpty()){

                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;

            }

            realizarMovimentacaoEstoque(produto, posicaoEstoque, quantidadeMovimentada, observacao, entradaSaida);

        });

    }

    private void realizarMovimentacao(String produto, String posicaoEstoque, String quantidadeMovimentada, String observacao, String entradaSaida){

        new Thread(() ->{

            Material material = db.materialDao().getMaterialById(Integer.parseInt(produto));

            if(material != null){

                Double custoUnitario = material.getCustoUnitario();
                Double custoTotal = custoUnitario * Integer.parseInt(quantidadeMovimentada);

                Movimentacao novaMovimentacao = new Movimentacao(Integer.parseInt(produto), idUsuario, Integer.parseInt(quantidadeMovimentada), entradaSaida, custoTotal, observacao);

                db.movimentacaoDao().insert(novaMovimentacao);

                runOnUiThread(() -> {

                    Toast.makeText(this, "Movimentacao realizada com sucesso", Toast.LENGTH_SHORT).show();

                });

            }

        }).start();

    }

    private void realizarBaixaEstoque(String idProduto, String entradaSaida, String quantidadeMovimentada, String posicaoEstoque){

        if(entradaSaida == "Entrada"){

            new Thread(() -> {

                db.estoqueDao().entradaMaterialEstoque(Integer.parseInt(quantidadeMovimentada), Integer.parseInt(idProduto), posicaoEstoque);

            }).start();

        }else{

            new Thread(() -> {

                db.estoqueDao().baixarMaterialEstoque(Integer.parseInt(quantidadeMovimentada), Integer.parseInt(idProduto), posicaoEstoque);

            }).start();

        }

    }

    private void realizarMovimentacaoEstoque(String idProduto, String posicaoEstoque, String quantidadeMovimentada, String observacao, String entradaSaida){

        new Thread(() ->{

            boolean existeProdutoPosicaoEstoque = db.estoqueDao().checkProdutoPosicao(Integer.parseInt(idProduto), posicaoEstoque);

            runOnUiThread(() ->{

                if(existeProdutoPosicaoEstoque){

                    realizarMovimentacao(idProduto, posicaoEstoque, quantidadeMovimentada, observacao, entradaSaida);
                    realizarBaixaEstoque(idProduto, entradaSaida, quantidadeMovimentada, posicaoEstoque);
                    finish();

                }else{

                    Toast.makeText(registroMovimentacaoActivity.this, "Produto nao existe nessa posicao", Toast.LENGTH_SHORT).show();

                }

            });

        }).start();

    }

}
