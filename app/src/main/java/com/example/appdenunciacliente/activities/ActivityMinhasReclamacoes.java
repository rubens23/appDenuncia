package com.example.appdenunciacliente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appdenunciacliente.Adapters.MinhasReclamacoesAdapterKotlin;
import com.example.appdenunciacliente.models.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.database.BancoController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityMinhasReclamacoes extends AppCompatActivity implements MinhasReclamacoesAdapterKotlin.CallbackInterface{

    //todo consertar o erro: activity minhas reclamacoes fecha quando o user n tem nenhuma reclamacao cadastrada
    //todo senha nao ta ficando com os asteriscos na activity registro
    //todo cadfastro deu sucesso e depois falha
    private final int PICK_IMAGE_REQUEST = 2;
    private Uri fileUriPathForFirebaseStorage;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private BancoController bd;

    private List<Minha_Reclamacao> listaReclamacoes;

    private String user_id;
    private List<Minha_Reclamacao> listaReclamacoesDoUserLogado = new ArrayList<>();
    private MinhasReclamacoesAdapterKotlin adapterMinhasReclamacoes;
    private RecyclerView recyclerViewReclamacoes;

    private String id_reclamacao_foto_atual;
    private String link_imagem_adicionada;
    private FloatingActionButton addImageButton;
    private MinhasReclamacoesAdapterKotlin.CallbackInterface callbackInterface;

    private TextView label_minhas_reclamacoes;

    private ImageView backButton;
    
    //todo arrumar o bug dos likes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reclamacoes);
        Log.d("ciclo1", "to no oncreate");


        initClassMembers();

        configFonts();

        mandarReclamacoesDoUsuarioParaAdapter();

        onClickListeners();



    }

    private void onClickListeners() {
        backButton.setOnClickListener(v->{
            startActivity(new Intent(this, MenuActivity.class));
        });
    }

    private void configFonts() {
        Typeface tfLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        Typeface tfHairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Hairline.ttf");
        label_minhas_reclamacoes.setTypeface(tfLight);

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

        this.callbackInterface = ((MinhasReclamacoesAdapterKotlin.CallbackInterface) this);

        recyclerViewReclamacoes = findViewById(R.id.recycler_minhas_reclamacoes);


        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewReclamacoes.setLayoutManager(lm);
        adapterMinhasReclamacoes = new MinhasReclamacoesAdapterKotlin(callbackInterface, this);
        recyclerViewReclamacoes.setAdapter(adapterMinhasReclamacoes);
        label_minhas_reclamacoes = findViewById(R.id.label_minhas_reclamacoes);

        backButton = findViewById(R.id.backButton);

        //todo como lidar com botoes nos itens da recycler view??

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
                //colocar uma progressbar aqui depois
                Log.d("uploadfeito", "imagem salva no firebase");
                getRecentlyTakenImageDownloadLink(firebaseStorageReference);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("falhanoupload", "ocorreu uma falha no salvamento da imagem");

            }
        });
    }

    private void getRecentlyTakenImageDownloadLink(StorageReference firebaseStorageReference) {
        firebaseStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                link_imagem_adicionada = uri.toString();
                Log.d("linkimagem", ""+link_imagem_adicionada);
                saveImageDataToDatabase();
                recreate();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("falhalink", "falha ao pegar o link de download da imagem");
            }
        });

    }

    private void saveImageDataToDatabase() {
        if(id_reclamacao_foto_atual != null && link_imagem_adicionada != null){
            Log.d("salvarnodb", "to pronto para salvar no database");
            bd.inserirNaTabelaImagens(id_reclamacao_foto_atual, FirebaseAuth.getInstance().getUid(), link_imagem_adicionada);
            Log.i("rubens", "o estado da imageView mudou");

        }


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

        if (retorno != null && total_rows>0) {
            do{
                Minha_Reclamacao mr = new Minha_Reclamacao();
                mr.setReclamacao(retorno.getString(0));
                mr.setStatus(retorno.getString(1));
                mr.setCodigo_reclamacao(retorno.getString(2));
                listaReclamacoesDoUserLogado.add(mr);
            }while(retorno.moveToNext());
        }
        return listaReclamacoesDoUserLogado;
    }

    @Override
    public void passResultCallback(@NonNull Intent intent) {
        id_reclamacao_foto_atual = intent.getStringExtra("id_reclamacao");
    }

}

