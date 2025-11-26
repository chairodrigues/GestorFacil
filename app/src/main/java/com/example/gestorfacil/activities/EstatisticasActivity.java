package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;

import java.text.NumberFormat;

public class EstatisticasActivity extends AppCompatActivity {

    private TextView txEntrada, txSaida, txDiferenca, txValorTotal;
    private String totalEntradas;
    private Double valorTotal;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        db = AppDatabase.getDatabase(getApplicationContext());

        atualizarEstatistica();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarEstatistica();
    }

    private void atualizarEstatistica(){

        txEntrada = findViewById(R.id.txEntrada);
        txSaida = findViewById(R.id.txSaida);
        txDiferenca = findViewById(R.id.txDiferenca);
        txValorTotal = findViewById(R.id.txValorTotal);

        new Thread(() -> {

            Double entradaObj = db.movimentacaoDao().getTotalEntradas();
            Double saidaObj = db.movimentacaoDao().getTotalSaidas();
            Double estoqueObj = db.estoqueDao().getValorTotalEstoque();

            double entrada = (entradaObj != null) ? entradaObj : 0.0;
            double saida = (saidaObj != null) ? saidaObj : 0.0;
            double totalEstoque = (estoqueObj != null) ? estoqueObj : 0.0;

            double diferenca = entrada - saida;


            runOnUiThread(() ->{

                txEntrada.setText(String.valueOf(entrada));
                txSaida.setText(String.valueOf(saida));
                txDiferenca.setText(String.valueOf(diferenca));
                txValorTotal.setText(String.valueOf(totalEstoque));

            });

        }).start();

    }
}
