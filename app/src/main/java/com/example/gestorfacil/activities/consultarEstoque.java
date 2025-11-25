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
    private AppDatabase  db = AppDatabase.getDatabase(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_estoque);

        listViewEstoque = findViewById(R.id.listViewEstoque);

        List<String> linhas = new ArrayList<>();
        for (Estoque p : db.estoqueDao().getAllEstoque()) {
            String status;
            if (p.getQuantidade() == 0) status = "ESGOTADO";
            else if (p.getQuantidade() <= 5) status = "BAIXO ESTOQUE";
            else status = "NORMAL";

            String linha = p.getIdMaterial() + " - Qtd: " + p.getQuantidade() +
                    " (" + status + ")";
            linhas.add(linha);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, linhas);
        listViewEstoque.setAdapter(adapter);
    }
}
