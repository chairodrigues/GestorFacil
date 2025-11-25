package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;

import java.text.NumberFormat;

public class EstatisticasActivity extends AppCompatActivity {

    private TextView txtTotalEstoque, txtTotalProdutos, txtTotalMovimentacoes;

    private AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        txtTotalEstoque = findViewById(R.id.txtTotalEstoque);
        txtTotalProdutos = findViewById(R.id.txtTotalProdutos);
        txtTotalMovimentacoes = findViewById(R.id.txtTotalMovimentacoes);

        //atualizarEstatisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //atualizarEstatisticas();
    }

    /**private void atualizarEstatisticas() {

        int totalEstoque = 0;
        int totalProdutos = 0;
        int totalMovimentacoes = 0;

        if (db != null) {
            // proteções contra possíveis retornos nulos
            Integer q = repo.getQuantidadeTotalEstoque();
            totalEstoque = q != null ? q : 0;

            totalProdutos = repo.getProdutos() != null ? repo.getProdutos().size() : 0;
            totalMovimentacoes = repo.getMovimentacoes() != null ? repo.getMovimentacoes().size() : 0;
        }

        NumberFormat nf = NumberFormat.getInstance();
        txtTotalEstoque.setText("Quantidade total em estoque: " + nf.format(totalEstoque));
        txtTotalProdutos.setText("Total de produtos cadastrados: " + totalProdutos);
        txtTotalMovimentacoes.setText("Movimentações registradas: " + totalMovimentacoes);
    }**/
}
