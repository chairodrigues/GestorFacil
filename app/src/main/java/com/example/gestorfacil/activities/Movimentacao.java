package com.example.gestorfacil.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.database.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movimentacao extends AppCompatActivity {

    private MovimentosDbHelper dbHelper;
    private Spinner tipoSpinner;
    private Spinner produtoSpinner;
    private EditText produtoEditText;
    private EditText quantidadeEditText;
    private EditText observacaoEditText;
    private Button salvarButton;
    private Button adicionarProdutoButton;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MovimentosDbHelper(this);

        db = AppDatabase.getDatabase(getApplicationContext());

        // Criar layout básico programaticamente para facilitar integração
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dpToPx(16);
        root.setPadding(pad, pad, pad, pad);

        produtoSpinner = new Spinner(this);
        produtoEditText = new EditText(this);
        produtoEditText.setHint("Nome do produto (ou selecione abaixo)");
        produtoEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        produtoSpinner.setPrompt("Produtos existentes");
        refreshProdutoSpinner();

        adicionarProdutoButton = new Button(this);
        adicionarProdutoButton.setText("Usar nome do campo acima");
        adicionarProdutoButton.setOnClickListener(v -> {
            String nome = produtoEditText.getText().toString().trim();
            if (nome.isEmpty()) {
                Toast.makeText(this, "Informe o nome do produto.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Se produto não existir, cria com estoque 0
            long id = dbHelper.ensureProdutoExists(nome);
            refreshProdutoSpinner();
            selectProdutoInSpinner(nome);
            Toast.makeText(this, "Produto selecionado: " + nome, Toast.LENGTH_SHORT).show();
        });

        tipoSpinner = new Spinner(this);
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("Entrada");
        tipos.add("Saída");
        tipoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos));

        quantidadeEditText = new EditText(this);
        quantidadeEditText.setHint("Quantidade");
        quantidadeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        observacaoEditText = new EditText(this);
        observacaoEditText.setHint("Observação (opcional)");
        observacaoEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        salvarButton = new Button(this);
        salvarButton.setText("Registrar movimentação");
        salvarButton.setOnClickListener(v -> registrarMovimentacao());

        // Adiciona views ao layout
        root.addView(produtoEditText,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(adicionarProdutoButton,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(produtoSpinner,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(tipoSpinner,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(quantidadeEditText,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        root.addView(observacaoEditText,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout buttonWrap = new LinearLayout(this);
        buttonWrap.setOrientation(LinearLayout.HORIZONTAL);
        buttonWrap.setGravity(Gravity.END);
        buttonWrap.addView(salvarButton);
        root.addView(buttonWrap);

        setContentView(root);
    }

    private void registrarMovimentacao() {
        String produtoNome = getSelectedProdutoName();
        if (produtoNome == null || produtoNome.trim().isEmpty()) {
            Toast.makeText(this, "Selecione ou informe um produto.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipo = tipoSpinner.getSelectedItem().toString();
        String quantidadeStr = quantidadeEditText.getText().toString().trim();
        if (quantidadeStr.isEmpty()) {
            Toast.makeText(this, "Informe a quantidade.", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                Toast.makeText(this, "Quantidade deve ser positiva.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Quantidade inválida.", Toast.LENGTH_SHORT).show();
            return;
        }

        String observacao = observacaoEditText.getText().toString().trim();
        String data = nowAsIso();

        // Garante que o produto exista
        long produtoId = dbHelper.ensureProdutoExists(produtoNome);

        // Verifica estoque atual se for saída
        int estoqueAtual = dbHelper.getEstoque(produtoId);
        if ("Saída".equalsIgnoreCase(tipo) && estoqueAtual < quantidade) {
            Toast.makeText(this, "Estoque insuficiente. Atual: " + estoqueAtual, Toast.LENGTH_SHORT).show();
            return;
        }

        // Calcula novo estoque
        int novoEstoque = estoqueAtual + ("Entrada".equalsIgnoreCase(tipo) ? quantidade : -quantidade);

        boolean inserted = dbHelper.insertMovimentacao(produtoId, produtoNome, tipo, quantidade, data, observacao);
        if (!inserted) {
            Toast.makeText(this, "Erro ao registrar movimentação.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = dbHelper.updateEstoqueProduto(produtoId, novoEstoque);
        if (!updated) {
            Toast.makeText(this, "Movimentação registrada mas falha ao atualizar estoque.", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Movimentação registrada com sucesso. Estoque agora: " + novoEstoque, Toast.LENGTH_LONG).show();
        quantidadeEditText.setText("");
        observacaoEditText.setText("");
        refreshProdutoSpinner();
    }

    private String getSelectedProdutoName() {
        // Retorna o valor do spinner (se tiver seleção) ou do campo livre
        String manual = produtoEditText.getText().toString().trim();
        if (!manual.isEmpty()) return manual;
        Object sel = produtoSpinner.getSelectedItem();
        if (sel != null) return sel.toString();
        return null;
    }

    private void refreshProdutoSpinner() {

        new Thread(() -> {

            List<String> nomesBD = db.materialDao().getNomesMaterial();

            ArrayList<String> listaNomes = new ArrayList<>();

            if (nomesBD.isEmpty()) {

                listaNomes.add("Sem produtos cadastrados");

            }else{

                listaNomes.addAll(nomesBD);

            }

            runOnUiThread(() -> {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaNomes);
                produtoSpinner.setAdapter(adapter);

            });

        }).start();

    }

    private void selectProdutoInSpinner(String nome) {
        ArrayAdapter adapter = (ArrayAdapter) produtoSpinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            Object it = adapter.getItem(i);
            if (it != null && nome.equals(it.toString())) {
                produtoSpinner.setSelection(i);
                return;
            }
        }
    }

    private String nowAsIso() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // SQLite helper interno simples
    private static class MovimentosDbHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "gestor.db";
        private static final int DB_VERSION = 1;

        MovimentosDbHelper(Context ctx) {
            super(ctx, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Tabela de produtos
            db.execSQL("CREATE TABLE IF NOT EXISTS produtos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT UNIQUE NOT NULL," +
                    "estoque INTEGER NOT NULL DEFAULT 0" +
                    ");");

            // Tabela de movimentações
            db.execSQL("CREATE TABLE IF NOT EXISTS movimentacoes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "produto_id INTEGER," +
                    "produto_nome TEXT," +
                    "tipo TEXT," + // Entrada / Saída
                    "quantidade INTEGER," +
                    "data TEXT," +
                    "observacao TEXT" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // No momento não há versão superior
        }

        long ensureProdutoExists(String nome) {
            SQLiteDatabase db = getWritableDatabase();
            // Tenta buscar
            Cursor c = db.rawQuery("SELECT id FROM produtos WHERE nome = ?", new String[]{nome});
            try {
                if (c.moveToFirst()) {
                    return c.getLong(0);
                }
            } finally {
                c.close();
            }
            // Inserir novo produto com estoque 0
            ContentValues cv = new ContentValues();
            cv.put("nome", nome);
            cv.put("estoque", 0);
            return db.insert("produtos", null, cv);
        }

        ArrayList<String> getTodosNomesProdutos() {
            ArrayList<String> res = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT nome FROM produtos ORDER BY nome COLLATE NOCASE", null);
            try {
                while (c.moveToNext()) {
                    res.add(c.getString(0));
                }
            } finally {
                c.close();
            }
            return res;
        }

        int getEstoque(long produtoId) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT estoque FROM produtos WHERE id = ?", new String[]{String.valueOf(produtoId)});
            try {
                if (c.moveToFirst()) {
                    return c.getInt(0);
                }
            } finally {
                c.close();
            }
            return 0;
        }

        boolean updateEstoqueProduto(long produtoId, int novoEstoque) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("estoque", novoEstoque);
            int updated = db.update("produtos", cv, "id = ?", new String[]{String.valueOf(produtoId)});
            return updated > 0;
        }

        boolean insertMovimentacao(long produtoId, String produtoNome, String tipo, int quantidade, String data, String observacao) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("produto_id", produtoId);
            cv.put("produto_nome", produtoNome);
            cv.put("tipo", tipo);
            cv.put("quantidade", quantidade);
            cv.put("data", data);
            cv.put("observacao", observacao);
            long id = db.insert("movimentacoes", null, cv);
            return id != -1;
        }
    }
}