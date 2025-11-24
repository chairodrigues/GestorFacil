import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ConsultaEstoqueActivity extends AppCompatActivity {

    private ListView listViewEstoque;
    private EstoqueRepository repo = EstoqueRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_estoque);

        listViewEstoque = findViewById(R.id.listViewEstoque);

        List<String> linhas = new ArrayList<>();
        for (Produto p : repo.getProdutos()) {
            String status;
            if (p.getQuantidade() == 0) status = "ESGOTADO";
            else if (p.getQuantidade() <= 5) status = "BAIXO ESTOQUE";
            else status = "NORMAL";

            String linha = p.getNome() + " - Qtd: " + p.getQuantidade() +
                    " (" + status + ")";
            linhas.add(linha);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, linhas);
        listViewEstoque.setAdapter(adapter);
    }
}
