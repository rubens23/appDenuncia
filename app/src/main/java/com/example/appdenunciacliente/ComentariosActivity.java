package com.example.appdenunciacliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdenunciacliente.Adapters.ComentariosAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {//TODO implementar os comentarios na recycler view dessa activity.

    BancoController bd;
    EditText comentario;
    Button btn_enviar_comentario;
    FirebaseUser user;
    RecyclerView recyclerView_comentarios;
    List<ComentariosModel> listaComentarios;
    ComentariosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        TextView recl_comentario = findViewById(R.id.comentario_reclamcao_principal);

        bd = new BancoController(this);
        comentario = findViewById(R.id.caixa_comentarios);
        btn_enviar_comentario = findViewById(R.id.btn_enviar_comentario);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView_comentarios = findViewById(R.id.recycler_comentarios);
        listaComentarios = new ArrayList<>();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView_comentarios.setLayoutManager(lm);


        Intent intent = getIntent();
        String reclamacao = intent.getStringExtra("message_key");
        String id_reclamacao = intent.getStringExtra("id_reclamacao");

        Cursor cursor = bd.getComentariosReclamacao(id_reclamacao);
        if(cursor != null && cursor.getCount() > 0){
            do {
                ComentariosModel cm = new ComentariosModel();
                cm.setId_usuario(user.getUid());
                cm.setComentario(cursor.getString(0));
                cm.setId_reclamacao(id_reclamacao);
                listaComentarios.add(cm);
            }while(cursor.moveToNext());
            adapter = new ComentariosAdapter(this);
            adapter.setItems(listaComentarios);
            recyclerView_comentarios.setAdapter(adapter);
        }


        recl_comentario.setText(reclamacao);

        btn_enviar_comentario.setOnClickListener(v->{
            String comentario_enviado = comentario.getText().toString();
            String enviar_comentario = bd.adicionarComentario(user.getUid(), comentario_enviado, id_reclamacao);
            Toast.makeText(this, enviar_comentario, Toast.LENGTH_SHORT).show();
        });


    }
}