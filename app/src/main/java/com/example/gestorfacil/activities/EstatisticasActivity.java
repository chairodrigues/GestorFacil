import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class EstatisticasActivity extends AppCompatActivity {

    private TextView txtTotalEstoque, txtTotalProdutos, txtTotalMovimentacoes;

    private EstoqueRepository repo = EstoqueRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        txtTotalEstoque = findViewById(R.id.txtTotalEstoque);
        txtTotalProdutos = findViewById(R.id.txtTotalProdutos);
        txtTotalMovimentacoes = findViewById(R.id.txtTotalMovimentacoes);

        atualizarEstatisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarEstatisticas();
    }

    private void atualizarEstatisticas() {
        if (repo == null) {
            repo = EstoqueRepository.getInstance();
        }

        int totalEstoque = 0;
        int totalProdutos = 0;
        int totalMovimentacoes = 0;

        if (repo != null) {
            // proteções contra possíveis retornos nulos
            Integer q = repo.getQuantidadeTotalEstoque();
            totalEstoque = q != null ? q : 0;

            totalProdutos = repo.getProdutos() != null ? repo.getProdutos().size() : 0;
            totalMovimentacoes = repo.getMovimentacoes() != null ? repo.getMovimentacoes().size() : 0;
        }

        NumberFormat nf = NumberFormat.getInstance();
        txtTotalEstoque.setText("Quantidade total em estoque: " + nf.format(totalEstoque));
        txtTotalProdutos.setText("Total de produtos cadastrados: " + totalProdutos);
        txtTotalMovimentacoes.setText("Movimentações registradas: " + totalMovimentacoes);
    }
}
