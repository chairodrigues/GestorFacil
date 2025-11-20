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

    private Button btnCadastroProduto, btnRegistroMovimentacao, btnEstoque, btnIndicadores, btnCriacaoUsuario;

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

        //Aqui deve vir o ID dos componentes -Felipe
        btnCadastroProduto = findViewById();

        btnCadastroProduto.setOnClickListener( v -> {

            Intent intent = new Intent(MainActivity.this, cadastroProdutoActivity.class);
            startActivity(intent);
            finish();

        });

    }
}