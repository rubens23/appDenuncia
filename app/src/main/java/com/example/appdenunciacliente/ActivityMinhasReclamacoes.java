package com.example.appdenunciacliente;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.MinhasReclamacoesAdapter;
import com.example.appdenunciacliente.ViewHolders.MinhaReclamacaoViewHolder;
import com.example.appdenunciacliente.databinding.ActivityMinhasReclamacoesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityMinhasReclamacoes extends AppCompatActivity {

    //variaveis globais

    private ActivityMinhasReclamacoesBinding binding;
    private Uri filePath;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private BancoController bd;

    String user_id;
    List<Minha_Reclamacao> lista = new ArrayList<>();
    List<DataComplaintsImages> listaLinkImagens = new ArrayList<>();
    MinhasReclamacoesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinhasReclamacoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bd = new BancoController(ActivityMinhasReclamacoes.this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        LinearLayoutManager lm = new LinearLayoutManager(this);
        binding.recyclerMinhasReclamacoes.setLayoutManager(lm);
        adapter = new MinhasReclamacoesAdapter(this);
        binding.recyclerMinhasReclamacoes.setAdapter(adapter);

        List<Minha_Reclamacao> listaReclamacoes = null;
        listaReclamacoes = getDadosReclamacaoUsuario();
        adapter.setItems(listaReclamacoes);

        Cursor dadosTabelaImagem = bd.getAllDataFromImagesTable();

        if(dadosTabelaImagem != null){
            do{
                DataComplaintsImages dci = new DataComplaintsImages();
                dci.setCodigo_reclamacao(dadosTabelaImagem.getString(0));
                dci.setLink_imagem(dadosTabelaImagem.getString(1));
                listaLinkImagens.add(dci);
            }while (dadosTabelaImagem.moveToNext());
            adapter.setImageComplaintsItems(listaLinkImagens);
        }

         //todo como vou fazer para inserir dados na tabela imagens, como vou ter acesso a todos os dados que preciso para preenche-la

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null){
            filePath = data.getData();
            Toast.makeText(this, "To no onActivity result", Toast.LENGTH_LONG).show();
            uploadImage();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(filePath != null){
            adapter.setUris(filePath);

            //uploadImage();

        }
        
        
    }

    private void uploadImage() {
        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        //TODOpega a uid do current user, e verifica se na tabela de imagens tem o id desse user junto com o id da reclamacao-se sim, ele esta autorizado a salvar essa imagem| fazer o codigo para inserir dados na tabela
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id_user = user.getUid();
                Toast.makeText(ActivityMinhasReclamacoes.this, "imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                //String retorno = bd.inserirNaTabelaImagens(adapter.getCurrentComplaintItemId());//id_reclamacao, user_id, link_imagem

            }
        });
    }

    public String getImageLink(Uri uri){
        return uri.toString();
    }

    public List<Minha_Reclamacao> getDadosReclamacaoUsuario(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//o view holder n ta funcionado 0.0//erro no cadastro de novos usuários
        if(user != null){
            user_id = user.getUid();
            //Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "não foi possível conseguir o id do usuario", Toast.LENGTH_SHORT).show();
        }
        BancoController bd = new BancoController(getBaseContext());
        Cursor cursorDadosReclamacao = bd.carregaDadoPeloUserId(user_id);


        if (cursorDadosReclamacao != null) {
            do{
                Minha_Reclamacao mr = new Minha_Reclamacao();
                mr.setReclamacao(cursorDadosReclamacao.getString(0));
                mr.setStatus(cursorDadosReclamacao.getString(1));
                mr.setCodigo_reclamacao(cursorDadosReclamacao.getString(2));
                lista.add(mr);
            }while(cursorDadosReclamacao.moveToNext());
        }else{
            Toast.makeText(this, "o cursor está vazio", Toast.LENGTH_SHORT).show();
            return null;
        }
        return lista;
    }
}
