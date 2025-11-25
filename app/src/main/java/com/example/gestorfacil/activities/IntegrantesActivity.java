package com.example.gestorfacil.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;

public class IntegrantesActivity extends AppCompatActivity {

    private ListView listViewIntegrantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrantes);

        listViewIntegrantes = findViewById(R.id.listViewIntegrantes);

        String[] nomes = new String[] {
                "CHAIANNE RODRIGUES FRANÇA",
                "VANESSA DA ROCHA COIN",
                "FELIPE CERRI",
                "JOEL LUIS STEFFEN",
                "LUCAS BRESOLIN"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes);
        listViewIntegrantes.setAdapter(adapter);
    }
}