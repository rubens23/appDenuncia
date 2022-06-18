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

    private final int PICK_IMAGE_REQUEST = 2;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    String user_id;
    List<Minha_Reclamacao> lista = new ArrayList<>();
    MinhasReclamacoesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null){
            filePath = data.getData();



            //TODO DEnovo o problema da resposta assíncrona....será que eu já sei resolver isso?

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
                BancoController bd = new BancoController(ActivityMinhasReclamacoes.this);
                //String retorno = bd.inserirNaTabelaImagens();//id_reclamacao, user_id, link_imagem
                //TODO fazer uma restrição. O usuário só pode salvar a imagem se no banco de dados id_user e id_reclamacao estiverem associados a esse user, se sim pode salvar na tabela de imagens
                //fazer o metodo para salvar as inforamções no banco de dados

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reclamacoes);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.recycler_minhas_reclamacoes);


        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        adapter = new MinhasReclamacoesAdapter(this);
        recyclerView.setAdapter(adapter);






        List<Minha_Reclamacao> listaReclamacoes = null;
        listaReclamacoes = getDadosReclamacaoUsuario();
        adapter.setItems(listaReclamacoes);










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
        Cursor retorno = bd.carregaDadoPeloUserId(user_id);
        int total_rows = retorno.getCount();

        String total_linhas = Integer.toString(total_rows);
        if (retorno != null) {
            do{
                Minha_Reclamacao mr = new Minha_Reclamacao();
                mr.setReclamacao(retorno.getString(0));
                mr.setStatus(retorno.getString(1));
                mr.setCodigo_reclamacao(retorno.getString(2));
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

    public class CallApi extends AsyncTask<String, String, String> {
        public AsyncResponse delegate = null;//Call back interface

        public CallApi(AsyncResponse asyncResponse){
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //Your network call or background process comes here
            return "res";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            delegate.processFinish(aVoid);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}

//token github 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

//dar um passo atras e tentar entender melhor sobre esses bancos de dados
//começar a considerar fazer só os wireframes mesmo. Pq é provavel que n de tempo.
//eu ainda tenho que gravar o video do projeto.

//responder essas perguntas:
//1- pq o criabanco2 n funcionou?
//2- pq o pnUpgrade n funciona direito
//e funciona só quando quer?