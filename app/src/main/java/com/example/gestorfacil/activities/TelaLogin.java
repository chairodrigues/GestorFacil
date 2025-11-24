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

package com.example.gestorfacil.activities;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout principal (vertical)
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Título
        TextView tvTitle = new TextView(this);
        tvTitle.setText("Login - GestorFácil");
        tvTitle.setTextSize(22f);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.setMargins(0, 0, 0, dp(16));
        root.addView(tvTitle, titleLp);

        // Email
        etEmail = new EditText(this);
        etEmail.setHint("E-mail");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        root.addView(etEmail, formLp());

        // Senha
        etPassword = new EditText(this);
        etPassword.setHint("Senha");
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        root.addView(etPassword, formLp());

        // Botão Entrar
        btnLogin = new Button(this);
        btnLogin.setText("Entrar");
        root.addView(btnLogin, formLp());

        // Botão Cadastrar Usuário (cria credenciais locais)
        btnRegisterUser = new Button(this);
        btnRegisterUser.setText("Cadastrar usuário");
        root.addView(btnRegisterUser, formLp());

        setContentView(root);

        // Eventos
        btnRegisterUser.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> attemptLogin());
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

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            toast("Preencha e-mail e senha.");
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String storedEmail = prefs.getString(PREF_EMAIL, null);
        String storedPass = prefs.getString(PREF_PASS, null);

        if (storedEmail == null || storedPass == null) {
            toast("Nenhum usuário cadastrado. Cadastre-se primeiro.");
            return;
        }

        if (email.equals(storedEmail) && pass.equals(storedPass)) {
            toast("Login bem-sucedido!");
            // Abrir tela de cadastro de produtos (crie essa activity: TelaCadastroProduto)
            try {
                Intent it = new Intent(this, Class.forName("com.example.gestorfacil.activities.TelaCadastroProduto"));
                startActivity(it);
            } catch (ClassNotFoundException e) {
                // Se a activity não existir ainda, informar ao desenvolvedor/usuário
                toast("TelaCadastroProduto não encontrada. Crie a Activity de cadastro de produtos.");
            }
        } else {
            toast("E-mail ou senha inválidos.");
        }
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
}