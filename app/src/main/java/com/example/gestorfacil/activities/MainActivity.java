package com.example.gestorfacil.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestorfacil.R;

public class MainActivity extends AppCompatActivity {

    private Button btnCadastroProduto, btnRegistroMovimentacao, btnEstoque, btnEstatistica, btnCriacaoUsuario, btnIntegrantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCadastroProduto = findViewById(R.id.btnCadastrarProduto);

        btnCadastroProduto.setOnClickListener( v -> {

            Intent intent = new Intent(MainActivity.this, cadastroProdutoActivity.class);
            startActivity(intent);

        });

        btnRegistroMovimentacao = findViewById(R.id.btnRegistrarMovimentacao);

        btnRegistroMovimentacao.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, Movimentacao.class);
            startActivity(intent);

        });

        btnEstoque = findViewById(R.id.btnEstoque);

        btnEstoque.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, consultarEstoque.class);
            startActivity(intent);

        });

        btnIntegrantes = findViewById(R.id.btnIntegrantes);

        btnIntegrantes.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, IntegrantesActivity.class);
            startActivity(intent);

        });

        btnEstatistica = findViewById(R.id.btnEstatistica);

        btnEstatistica.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, EstatisticasActivity.class);
            startActivity(intent);

        });

    }
}