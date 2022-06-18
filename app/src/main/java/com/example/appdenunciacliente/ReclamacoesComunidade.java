package com.example.appdenunciacliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.ReclamacoesComunidadeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReclamacoesComunidade extends AppCompatActivity {

    RecyclerView recyclerView;
    BancoController bd;
    ReclamacoesComunidadeAdapter adapter;
    List<Minha_Reclamacao> listaTodasReclamacoes;
    int cont = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamacoes_comunidade);

        bd = new BancoController(this);
        adapter = new ReclamacoesComunidadeAdapter(this);
        recyclerView = findViewById(R.id.recycler_reclamacoes_comunidade);
        LinearLayoutManager lm = new LinearLayoutManager(this);






        listaTodasReclamacoes = getAllComplaintsList();
        adapter.setItemList(listaTodasReclamacoes);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);


    }

    public List<Minha_Reclamacao> getAllComplaintsList(){
        Cursor todasReclamacoes = bd.carregaTodasReclamacoes();
        if(todasReclamacoes != null){
            listaTodasReclamacoes = new ArrayList<>();
            do{
                Minha_Reclamacao reclamacao1 = new Minha_Reclamacao();
                reclamacao1.setReclamacao(todasReclamacoes.getString(0));//reclamacoes
                reclamacao1.setStatus(todasReclamacoes.getString(1));//status reclamacao
                reclamacao1.setCodigo_reclamacao(todasReclamacoes.getString(2));//codigo
                listaTodasReclamacoes.add(reclamacao1);
            }while(todasReclamacoes.moveToNext());
        }
        return listaTodasReclamacoes;

    }
}