package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Material;
import com.google.android.material.textfield.TextInputEditText;

public class cadastroProdutoActivity extends AppCompatActivity {

    private EditText editNomeProduto, editDescricaoProduto, editTipoProduto, editCustoProduto, editTipoMedidaProduto, editEstoqueMinimoProduto;
    private Button btnCadastrar, btnCancelar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        editNomeProduto = findViewById(R.id.txNomeProduto);
        editDescricaoProduto = findViewById(R.id.txDescProduto);
        editTipoProduto = findViewById(R.id.txTipoProduto);
        editCustoProduto = findViewById(R.id.txCusto);
        editTipoMedidaProduto = findViewById(R.id.txMedidaProduto);
        editEstoqueMinimoProduto = findViewById(R.id.txEstoqueMinimo);


        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCancelar = findViewById(R.id.btnCancelar);

        db = AppDatabase.getDatabase(getApplicationContext());

        btnCadastrar.setOnClickListener(v -> {

            String nomeProduto = editNomeProduto.getText().toString();
            String descricaoProduto = editDescricaoProduto.getText().toString();
            String tipoProduto = editTipoProduto.getText().toString();
            String custoProduto = editCustoProduto.getText().toString();
            String tipoMedidaProduto = editTipoMedidaProduto.getText().toString();
            String estoqueMinimoProduto = editEstoqueMinimoProduto.getText().toString();

            if(nomeProduto.isEmpty() || descricaoProduto.isEmpty() || tipoProduto.isEmpty() || custoProduto.isEmpty() || tipoMedidaProduto.isEmpty() || estoqueMinimoProduto.isEmpty()){

                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;

            }

            cadastrarProduto(nomeProduto, descricaoProduto, tipoProduto, custoProduto, tipoMedidaProduto, estoqueMinimoProduto);

        });

    }

    private void cadastrarProduto(String nomeProduto, String descricaoProduto, String tipoProduto, String custoProduto, String tipoMedidaProduto, String estoqueMinimoProduto){

        try{

            Material novoMaterial = new Material(nomeProduto, descricaoProduto, Double.parseDouble(custoProduto), Integer.parseInt(estoqueMinimoProduto), tipoProduto, tipoMedidaProduto);

            new Thread(() -> {

                db.materialDao().insert(novoMaterial);

                runOnUiThread(() -> {

                    Toast.makeText(this, "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();

                });

            }).start();

        }catch(NumberFormatException e){

            Toast.makeText(this, "Numero preenchido de forma invalida", Toast.LENGTH_SHORT).show();

        }

    }
}
