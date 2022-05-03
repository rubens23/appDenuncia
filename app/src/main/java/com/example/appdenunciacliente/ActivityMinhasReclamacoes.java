package com.example.appdenunciacliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.MinhasReclamacoesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ActivityMinhasReclamacoes extends AppCompatActivity {

    String user_id;
    List<Minha_Reclamacao> lista = new ArrayList<>();
    MinhasReclamacoesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reclamacoes);

        recyclerView = findViewById(R.id.recycler_minhas_reclamacoes);


        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        adapter = new MinhasReclamacoesAdapter(this);
        recyclerView.setAdapter(adapter);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
           user_id = user.getUid();
        }else{
            Toast.makeText(this, "não foi possível conseguir o id do usuario", Toast.LENGTH_SHORT).show();
        }

        List<Minha_Reclamacao> listaReclamacoes = null;
        listaReclamacoes = getDadosReclamacaoUsuario();
        adapter.setItems(listaReclamacoes);




    }

    public List<Minha_Reclamacao> getDadosReclamacaoUsuario(){
        BancoController bd = new BancoController(getBaseContext());
        Cursor retorno = bd.carregaDadoPeloUserId(user_id);
        int total_rows = retorno.getCount();
        String total_linhas = Integer.toString(total_rows);
        if (retorno != null) {
            do{
                Minha_Reclamacao mr = new Minha_Reclamacao();
                mr.setReclamacao(retorno.getString(0));
                mr.setStatus(retorno.getString(1));
                lista.add(mr);
            }while(retorno.moveToNext());
            /*
            Minha_Reclamacao mr = new Minha_Reclamacao();
            String recl1 = retorno.getString(0);
            String status1 = retorno.getString(1);
            tv_retorno.setText(recl1);
            tv_retorno_status.setText(status1);

             */


        }else{
            Toast.makeText(this, "o cursor está vazio", Toast.LENGTH_SHORT).show();
        }
        return lista;
    }
}