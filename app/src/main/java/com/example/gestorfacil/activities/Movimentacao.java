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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorfacil.database.AppDatabase;
import com.example.gestorfacil.database.Estoque;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movimentacao extends AppCompatActivity {

    private Spinner produtoSpinner, posicaoSpinner, tipoSpinner;
    private EditText produtoEditText;
    private EditText quantidadeEditText;
    private EditText observacaoEditText;
    private Button salvarButton;
    private Button adicionarProdutoButton;
    private AppDatabase db;
    private int idSelecionado, quantidadeProduto;
    private String posicaoEstoque, entradaSaida, observacao;
    private Estoque novoEstoque;
    private com.example.gestorfacil.database.Movimentacao novaMovimentacao;
    private double custoMaterialUnitario, custoMaterialTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            //long id = dbHelper.ensureProdutoExists(nome);
            refreshProdutoSpinner();
            selectProdutoInSpinner(nome);
            Toast.makeText(this, "Produto selecionado: " + nome, Toast.LENGTH_SHORT).show();
        });

        tipoSpinner = new Spinner(this);
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("Entrada");
        tipos.add("Saída");
        tipoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos));

        posicaoSpinner = new Spinner(this);
        ArrayList<String> posicoes = new ArrayList<>();
        posicoes.add("1A");
        posicoes.add("2A");
        posicoes.add("3A");
        posicoes.add("1B");
        posicoes.add("2B");
        posicoes.add("3B");
        posicaoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, posicoes));

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
        root.addView(posicaoSpinner,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
        quantidadeProduto = Integer.parseInt(String.valueOf(quantidadeEditText.getText()));
        posicaoEstoque = posicaoSpinner.getSelectedItem().toString();
        entradaSaida = tipoSpinner.getSelectedItem().toString();
        observacao = String.valueOf(observacaoEditText.getText());
        selecionarIdProduto();

        if(entradaSaida.equals("Entrada")){

            new Thread(() ->{

                if(db.estoqueDao().checkProdutoPosicao(idSelecionado, posicaoEstoque)){

                    custoMaterialUnitario = db.materialDao().getCustoMaterial(idSelecionado);
                    custoMaterialTotal = custoMaterialUnitario * quantidadeProduto;

                    db.estoqueDao().entradaMaterialEstoque(quantidadeProduto, idSelecionado, posicaoEstoque);
                    db.estoqueDao().atualizarValorTotal(custoMaterialTotal, db.estoqueDao().getIdPosicao(posicaoEstoque));

                    novaMovimentacao = new com.example.gestorfacil.database.Movimentacao(idSelecionado, 1, quantidadeProduto, entradaSaida, custoMaterialTotal, observacao);

                    db.movimentacaoDao().insert(novaMovimentacao);

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Movimentacao cadastrada com sucesso! Material " + idSelecionado + " Posicao " + posicaoEstoque + " Quantidade " + quantidadeProduto , Toast.LENGTH_SHORT).show();
                        finish();

                    });

                }else if(!db.estoqueDao().checkPosicao(posicaoEstoque)){

                    custoMaterialUnitario = db.materialDao().getCustoMaterial(idSelecionado);
                    custoMaterialTotal = custoMaterialUnitario * quantidadeProduto;

                    novoEstoque = new Estoque(posicaoEstoque, quantidadeProduto, idSelecionado, custoMaterialTotal);

                    db.estoqueDao().insert(novoEstoque);

                    novaMovimentacao = new com.example.gestorfacil.database.Movimentacao(idSelecionado, 1, quantidadeProduto, entradaSaida, custoMaterialTotal, observacao);

                    db.movimentacaoDao().insert(novaMovimentacao);

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Movimentacao cadastrada com sucesso! Material " + idSelecionado + " Posicao " + posicaoEstoque + " Quantidade " + quantidadeProduto , Toast.LENGTH_SHORT).show();
                        finish();

                    });

                }else if(db.estoqueDao().checkPosicaoLivre(posicaoEstoque)){

                    custoMaterialUnitario = db.materialDao().getCustoMaterial(idSelecionado);
                    custoMaterialTotal = custoMaterialUnitario * quantidadeProduto;

                    novoEstoque = new Estoque(db.estoqueDao().getIdPosicao(posicaoEstoque),posicaoEstoque, quantidadeProduto, idSelecionado, custoMaterialTotal);

                    db.estoqueDao().update(novoEstoque);

                    novaMovimentacao = new com.example.gestorfacil.database.Movimentacao(idSelecionado, 1, quantidadeProduto, entradaSaida, custoMaterialTotal, observacao);

                    db.movimentacaoDao().insert(novaMovimentacao);

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Movimentacao cadastrada com sucesso! Material " + idSelecionado + " Posicao " + posicaoEstoque + " Quantidade " + quantidadeProduto , Toast.LENGTH_SHORT).show();
                        finish();

                    });

                }else{

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Houve um problema para realizar a movimentacao", Toast.LENGTH_SHORT).show();

                    });

                }

            }).start();

        }else{

            new Thread(() ->{

                if(db.estoqueDao().checkProdutoPosicaoQuantidade(idSelecionado, posicaoEstoque, quantidadeProduto)){

                    db.estoqueDao().baixarMaterialEstoque(quantidadeProduto, idSelecionado, posicaoEstoque);

                    novaMovimentacao = new com.example.gestorfacil.database.Movimentacao(idSelecionado, 1, quantidadeProduto, entradaSaida, custoMaterialTotal, observacao);

                    db.movimentacaoDao().insert(novaMovimentacao);

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Movimentacao cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();

                    });

                }else {

                    runOnUiThread(() -> {

                        Toast.makeText(this, "Houve um problema para realizar a movimentacao!", Toast.LENGTH_SHORT).show();
                        finish();

                    });

                }

            }).start();

        }

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
                idSelecionado = i + 1;

                Toast.makeText(this, idSelecionado, Toast.LENGTH_SHORT).show();

                return;
            }
        }
    }

    private void selecionarIdProduto() {
        ArrayAdapter adapter = (ArrayAdapter) produtoSpinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            Object it = adapter.getItem(i);
            if (it != null && produtoSpinner.getSelectedItem().equals(it.toString())) {
                produtoSpinner.setSelection(i);
                idSelecionado = i + 1;

                return;
            }
        }
    }


    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}