package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Estoque;

import java.util.ArrayList;
import java.util.List;

public class consultarEstoque extends AppCompatActivity {

    private ListView listViewEstoque;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_estoque);

        listViewEstoque = findViewById(R.id.listViewEstoque);

        // inicializa o banco aqui
        db = AppDatabase.getDatabase(getApplicationContext());

        // busca os dados em background e atualiza a UI na thread principal
        new Thread(() -> {
            List<String> linhas = new ArrayList<>();
            List<Estoque> estoqueList = db.estoqueDao().getAllEstoque();
            if (estoqueList != null) {
                for (Estoque p : estoqueList) {
                    String status;
                    if (p.getQuantidade() == 0) status = "ESGOTADO";
                    else if (p.getQuantidade() <= 5) status = "BAIXO ESTOQUE";
                    else status = "NORMAL";

                    String linha = p.getIdMaterial() + " - Qtd: " + p.getQuantidade() +
                            " (" + status + ")";
                    linhas.add(linha);
                }
            }

            if (linhas.isEmpty()) {
                linhas.add("Sem itens em estoque");
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(consultarEstoque.this, android.R.layout.simple_list_item_1, linhas);
                listViewEstoque.setAdapter(adapter);
            });
        }).start();
    }
}
