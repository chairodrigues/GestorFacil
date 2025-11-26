package com.example.gestorfacil.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.R;
import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Usuario;

public class loginActivity  extends AppCompatActivity {

    private String editEmail, editSenha;
    private Button btnEntrar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getDatabase(getApplicationContext());

        btnEntrar.setOnClickListener(v -> {

            String email = "teste";
            String senha = "teste";

            if(email.isEmpty() || senha.isEmpty()) {

                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;

            }

            realizarLogin(email, senha);

        });
    }

    private void realizarLogin(String email, String senha){

        new Thread(() -> {

            Usuario usuarioEncontrado = db.usuarioDao().checkLogin(email, senha);

            runOnUiThread(() -> {

                if(usuarioEncontrado != null){

                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{

                    Toast.makeText(loginActivity.this, "Email ou senha invalidos", Toast.LENGTH_SHORT).show();

                }

            });

        }).start();

    }

}
