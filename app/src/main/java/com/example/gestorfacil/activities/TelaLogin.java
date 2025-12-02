package com.example.gestorfacil.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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

    private EditText etEmail;
    private EditText etPassword;
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

        Button btnLogin = new Button(this);
        btnLogin.setText("Entrar");
        root.addView(btnLogin, formLp());

        Button btnRegisterUser = new Button(this);
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

        // Derivar um nome simples a partir do e-mail se o usuário não informar
        String nome = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;

        new Thread(() -> {
            // Verifica se já existe um usuário com este e-mail
            Usuario existente = db.usuarioDao().getUsuarioByEmail(email);
            if (existente != null) {
                runOnUiThread(() -> toast("Esse e-mail já está cadastrado."));
                return;
            }

            // Insere o novo usuário no banco
            Usuario novo = new Usuario(nome, email, pass);
            db.usuarioDao().insert(novo);

            // Notifica a UI
            runOnUiThread(() -> {
                toast("Usuário cadastrado com sucesso. Use Entrar para acessar.");
                etEmail.setText("");
                etPassword.setText("");
            });
        }).start();
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

            // Verifica por e-mail (checkLoginExiste tinha uma assinatura incorreta que retorna boolean para uma query que seleciona uma entidade)
            Usuario existente = db.usuarioDao().getUsuarioByEmail(email);
            if (existente == null) {
                db.usuarioDao().insert(admin);

            }

        }).start();

    }
}