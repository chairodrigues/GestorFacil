package com.example.gestorfacil.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Usuario;

/**
 * Tela de login simples programática.
 * - Permite cadastrar um usuário localmente (SharedPreferences) e entrar.
 * - Ao entrar, tenta abrir TelaCadastroProduto (crie essa Activity separadamente).
 */
public class TelaLogin extends AppCompatActivity {

    private static final String PREFS_NAME = "gestor_prefs";
    private static final String PREF_EMAIL = "user_email";
    private static final String PREF_PASS = "user_pass";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegisterUser;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getDatabase(getApplicationContext());

        cadastrarUsuario();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        TextView tvTitle = new TextView(this);
        tvTitle.setText("Login - GestorFácil");
        tvTitle.setTextSize(22f);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.setMargins(0, 0, 0, dp(16));
        root.addView(tvTitle, titleLp);

        etEmail = new EditText(this);
        etEmail.setHint("E-mail");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        root.addView(etEmail, formLp());

        etPassword = new EditText(this);
        etPassword.setHint("Senha");
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        root.addView(etPassword, formLp());

        btnLogin = new Button(this);
        btnLogin.setText("Entrar");
        root.addView(btnLogin, formLp());

        btnRegisterUser = new Button(this);
        btnRegisterUser.setText("Cadastrar usuário");
        root.addView(btnRegisterUser, formLp());

        setContentView(root);

        btnRegisterUser.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString();
            String senha = etPassword.getText().toString();


            if(email.isEmpty() || senha.isEmpty()) {

                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;

            }

            realizarLogin(email, senha);

        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            toast("Preencha e-mail e senha para cadastrar.");
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(PREF_EMAIL, email)
                .putString(PREF_PASS, pass)
                .apply();

        toast("Usuário cadastrado localmente. Use Entrar para acessar.");
    }

    private void realizarLogin(String email, String senha){

        new Thread(() -> {

            Usuario usuarioEncontrado = db.usuarioDao().checkLogin(email, senha);

            runOnUiThread(() -> {

                if(usuarioEncontrado != null){

                    Intent intent = new Intent(TelaLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{

                    Toast.makeText(TelaLogin.this, "Email ou senha invalidos", Toast.LENGTH_SHORT).show();

                }

            });

        }).start();

    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private LinearLayout.LayoutParams formLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(8), 0, dp(8));
        return lp;
    }

    private int dp(int value) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    private void cadastrarUsuario(){

        String nome, email, senha;

        nome = "admin";
        email = "admin@gmail.com";
        senha = "admin";

        Usuario admin = new Usuario(nome, email, senha);

        new Thread(() ->{

            if(!db.usuarioDao().checkLoginExiste(email, senha)){

                db.usuarioDao().insert(admin);

            }

        }).start();

    }
}