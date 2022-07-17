package com.example.appdenunciacliente.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.ComentariosAdapter;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.database.BancoController;
import com.example.appdenunciacliente.models.ComentariosModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private BancoController bd;
    private EditText comentario;
    private Button btn_enviar_comentario;
    private FirebaseUser user;
    private TextView recl_comentario, label_comentario;
    private RecyclerView recyclerView_comentarios;
    private List<ComentariosModel> listaComentarios;
    private ComentariosAdapter adapterComentarios;
    private String reclamacao, id_reclamacao;

    private ImageView buttonBack4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        initClassMembers();
        configFonts();
        onClickListeners();

        manageComentariosRecyclerView();

        getIntentExtras();

        getComentariosDaDenunciaAndPutItInAList();

        showDenunciaQueEstaSendoComentada();

        enviarComentarios();

    }

    private void onClickListeners() {
        buttonBack4.setOnClickListener(v->{
            startActivity(new Intent(this, ReclamacoesComunidade.class));
        });
    }

    private void configFonts() {
        Typeface tfLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        Typeface tfHairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Hairline.ttf");
        label_comentario.setTypeface(tfLight);
    }

    private void initClassMembers() {
        recl_comentario = findViewById(R.id.denunciaQueEstaSendoComentada);

        bd = new BancoController(this);
        comentario = findViewById(R.id.caixa_comentarios);
        btn_enviar_comentario = findViewById(R.id.btn_enviar_comentario);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView_comentarios = findViewById(R.id.recycler_comentarios);
        listaComentarios = new ArrayList<>();

        label_comentario = findViewById(R.id.label_comentarios);
        buttonBack4 = findViewById(R.id.backButton4);
    }

    private void enviarComentarios() {
        btn_enviar_comentario.setOnClickListener(v->{
            String comentario_enviado = comentario.getText().toString();
            String enviar_comentario = bd.adicionarComentario(user.getUid(), comentario_enviado, id_reclamacao);
            Toast.makeText(this, enviar_comentario, Toast.LENGTH_SHORT).show();
            comentario.setText("");
            recreate();
        });
    }

    private void showDenunciaQueEstaSendoComentada() {
        recl_comentario.setText(reclamacao);
    }

    private void getComentariosDaDenunciaAndPutItInAList() {
        Cursor cursor = bd.getComentariosReclamacao(id_reclamacao);
        if(cursor != null && cursor.getCount() > 0){
            do {
                ComentariosModel cm = new ComentariosModel();
                cm.setId_usuario(user.getUid());
                cm.setComentario(cursor.getString(0));
                cm.setId_reclamacao(id_reclamacao);
                listaComentarios.add(cm);
            }while(cursor.moveToNext());
            sendListaComentariosToAdapter();
        }
    }

    private void sendListaComentariosToAdapter() {
        adapterComentarios = new ComentariosAdapter(this);
        adapterComentarios.setItems(listaComentarios);
        recyclerView_comentarios.setAdapter(adapterComentarios);
    }


    private void getIntentExtras() {
        Intent intent = getIntent();
        reclamacao = intent.getStringExtra("message_key");
        id_reclamacao = intent.getStringExtra("id_reclamacao");
    }

    private void manageComentariosRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView_comentarios.setLayoutManager(lm);
    }




}