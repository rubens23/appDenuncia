package com.example.appdenunciacliente.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appdenunciacliente.Adapters.ReclamacoesComunidadeAdapter;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.database.BancoController;
import com.example.appdenunciacliente.models.Minha_Reclamacao;

import java.util.ArrayList;
import java.util.List;

public class ReclamacoesComunidade extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BancoController bd;
    private ReclamacoesComunidadeAdapter adapterReclamacoesComunidade;
    private List<Minha_Reclamacao> listaTodasReclamacoes;
    private LinearLayoutManager linearLayoutManager;
    private final int CONT = 1;

    private TextView label_reclamacoes_comunidade;

    private ImageView backButton3;

    //todo remover o botao de adicionar imagem a denuncia
    //todo arrumar os likes da minha conta rubens@teste.com

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamacoes_comunidade);

        initClassMembers();
        onClickListeners();
        configFonts();

        getAllComplaintsSentByEveryoneAndSendItToAdapter();

    }

    private void configFonts() {
        Typeface tfLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        Typeface tfHairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Hairline.ttf");
        label_reclamacoes_comunidade.setTypeface(tfLight);
    }

    private void onClickListeners() {
        backButton3.setOnClickListener(v->{
            startActivity(new Intent(this, MenuActivity.class));
        });
    }



    private void initClassMembers() {
        bd = new BancoController(this);
        adapterReclamacoesComunidade = new ReclamacoesComunidadeAdapter(this);
        recyclerView = findViewById(R.id.recycler_reclamacoes_comunidade);
        linearLayoutManager = new LinearLayoutManager(this);

        backButton3 = findViewById(R.id.backButton3);
        label_reclamacoes_comunidade = findViewById(R.id.lb_reclamacoes_comunidade);
    }

    private void getAllComplaintsSentByEveryoneAndSendItToAdapter() {
        listaTodasReclamacoes = getAllComplaintsList();
        adapterReclamacoesComunidade.setItemList(listaTodasReclamacoes);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterReclamacoesComunidade);
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
