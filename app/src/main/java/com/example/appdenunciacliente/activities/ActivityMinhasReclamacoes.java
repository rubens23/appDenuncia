package com.example.appdenunciacliente.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.MinhasReclamacoesAdapter;
import com.example.appdenunciacliente.models.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.database.BancoController;
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
    private Uri fileUriPathForFirebaseStorage;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private BancoController bd;

    private List<Minha_Reclamacao> listaReclamacoes;

    private String user_id;
    private List<Minha_Reclamacao> listaReclamacoesDoUserLogado = new ArrayList<>();
    private MinhasReclamacoesAdapter adapterMinhasReclamacoes;
    private RecyclerView recyclerViewReclamacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reclamacoes);

        initClassMembers();

        mandarReclamacoesDoUsuarioParaAdapter();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(fileUriPathForFirebaseStorage != null){
            adapterMinhasReclamacoes.setUris(fileUriPathForFirebaseStorage);
            //uploadImage();

        }


    }

    private void mandarReclamacoesDoUsuarioParaAdapter() {
        listaReclamacoes = getDadosReclamacaoUsuario();
        adapterMinhasReclamacoes.setItems(listaReclamacoes);
    }

    private void initClassMembers() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerViewReclamacoes = findViewById(R.id.recycler_minhas_reclamacoes);


        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewReclamacoes.setLayoutManager(lm);
        adapterMinhasReclamacoes = new MinhasReclamacoesAdapter(this);
        recyclerViewReclamacoes.setAdapter(adapterMinhasReclamacoes);

        listaReclamacoes = new ArrayList();

        bd = new BancoController(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null){
            boolean tem = data.hasExtra("enviar");
            Bundle extras = data.getExtras();
            Log.d("extra", "getExtras: "+tem);
            fileUriPathForFirebaseStorage = data.getData();
            uploadImageToFirebaseStorage(fileUriPathForFirebaseStorage);

        }
    }

//2022-06-27 23:55:40.721 2996-2996/com.example.appdenunciacliente D/extra: getExtras: Bundle[mParcelledData.dataSize=292]
    private void uploadImageToFirebaseStorage(Uri photoUri) {
        String imageName = UUID.randomUUID().toString();
        StorageReference firebaseStorageReference = storageReference.child("images/"+ imageName);
        firebaseStorageReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ActivityMinhasReclamacoes.this, "imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                saveImageDataToDatabase();
            }
        });
    }

    private void saveImageDataToDatabase() {


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
                listaReclamacoesDoUserLogado.add(mr);
            }while(retorno.moveToNext());



        }else{
            Toast.makeText(this, "o cursor está vazio", Toast.LENGTH_SHORT).show();
        }
        return listaReclamacoesDoUserLogado;
    }


}

