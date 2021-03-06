package com.example.appdenunciacliente.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.database.BancoController;
import com.example.appdenunciacliente.activities.ComentariosActivity;
import com.example.appdenunciacliente.models.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

//TODO colocar o onclick do botao de abrir para comentar.

public class ReclamacoesComunidadeViewHolder extends RecyclerView.ViewHolder{

    Context ctx;
    TextView tvReclamacao, tvStatus, cont_likes, recl_comentario, btn_comentario2;
    FirebaseUser user;
    BancoController bd;
    ImageView heart_btn, btn_comentario1, imagem_reclamacao;
    FloatingActionButton btnNewImage;

    public ReclamacoesComunidadeViewHolder(@NonNull View itemView) {
        super(itemView);

        initClassMembers();


    }

    private void initClassMembers() {

        ctx = itemView.getContext();
        user = FirebaseAuth.getInstance().getCurrentUser();
        bd = new BancoController(ctx);

        tvReclamacao = itemView.findViewById(R.id.tv_minha_reclamacao);
        tvStatus = itemView.findViewById(R.id.tv_status);
        heart_btn = itemView.findViewById(R.id.img_view_like);
        cont_likes = itemView.findViewById(R.id.contador_likes);
        recl_comentario = itemView.findViewById(R.id.denunciaQueEstaSendoComentada);
        btn_comentario1 = itemView.findViewById(R.id.btn_comentarios1);
        btn_comentario2 = itemView.findViewById(R.id.btn_comentario2);
        imagem_reclamacao = itemView.findViewById(R.id.imagem_reclamacao);
        btnNewImage = itemView.findViewById(R.id.btn_newImage);
        btnNewImage.setVisibility(View.INVISIBLE);
    }

    public void bindData(Minha_Reclamacao mr) {
        dealWithComplaintImages(mr);
        putComplaintsInRecyclerView(mr);
        verifyIfUserAlreadyLikedSpecificComplaint(mr);
        setLikesCounter(mr);
        if(user != null){
            heart_btn.setOnClickListener(v->{
                Cursor isThereALike = bd.seeIfUserLikedParticularComplaint(user.getUid(), mr.getCodigo_reclamacao());
                if(isThereALike.getCount() > 0){
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    bd.subtractOneLike(mr.getCodigo_reclamacao());
                    String codReclamacaoTirarLike = mr.getCodigo_reclamacao();
                    Cursor cursor = bd.getQuantidadeLikes(codReclamacaoTirarLike);
                    if(cursor != null){
                        do{
                            String qtdLikes = cursor.getString(0);
                            cont_likes.setText(qtdLikes);
                        }while(cursor.moveToNext());
                        bd.deleteLikedComplaint(user.getUid(), codReclamacaoTirarLike);
                    }
                }else{
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                    bd.adicionarLike(mr.getCodigo_reclamacao());
                    String codReclamacao = mr.getCodigo_reclamacao();
                    Cursor cursor = bd.getQuantidadeLikes(codReclamacao);
                    if(cursor != null){
                        do{
                            String qtdLikes = cursor.getString(0);
                            cont_likes.setText(qtdLikes);
                        }while(cursor.moveToNext());
                        bd.setLikedComplaintId(user.getUid(), codReclamacao);
                    }
                }
            });

            btn_comentario1.setOnClickListener(v->{
                Cursor reclamacoesParaComentarios = bd.carregaTodasReclamacoes();
                String idReclamacao = mr.getCodigo_reclamacao();
                if(reclamacoesParaComentarios != null){
                    do {
                        if(reclamacoesParaComentarios.getString(2).equals(idReclamacao)){
                            Intent openComentarios = new Intent(ctx, ComentariosActivity.class);
                            openComentarios.putExtra("message_key", mr.getReclamacao());
                            openComentarios.putExtra("id_reclamacao", mr.getCodigo_reclamacao());
                            ctx.startActivity(openComentarios);
                        }
                    }while(reclamacoesParaComentarios.moveToNext());
                }
            });

            btn_comentario2.setOnClickListener(v->{
                Cursor reclamacoesParaComentarios = bd.carregaTodasReclamacoes();
                String idReclamacao = mr.getCodigo_reclamacao();
                if(reclamacoesParaComentarios != null){
                    do {
                        if(reclamacoesParaComentarios.getString(2).equals(idReclamacao)){
                            Intent openComentarios = new Intent(ctx, ComentariosActivity.class);
                            openComentarios.putExtra("message_key", mr.getReclamacao());
                            openComentarios.putExtra("id_reclamacao", mr.getCodigo_reclamacao());
                            ctx.startActivity(openComentarios);
                        }
                    }while(reclamacoesParaComentarios.moveToNext());
                }
            });
        }

    }

    private void dealWithComplaintImages(Minha_Reclamacao mr) {
        Cursor temLinkImagem = bd.getComplaintImage(mr.getCodigo_reclamacao());
        if(temLinkImagem == null){
            imagem_reclamacao.setImageResource(R.drawable.ic_launcher_background);

        }

        Cursor cursorTemImagens = bd.getComplaintImage(mr.getCodigo_reclamacao());
        if(cursorTemImagens.getCount() >0){
            putPhotoInComplaint(mr, cursorTemImagens);
        }
    }

    private void putPhotoInComplaint(Minha_Reclamacao mr, Cursor c) {
        Picasso.get().load(c.getString(0)).into(imagem_reclamacao);
    }

    public void putComplaintsInRecyclerView(Minha_Reclamacao mr){
        tvReclamacao.setText(mr.getReclamacao());
        tvStatus.setText(mr.getStatus());
    }

    private void verifyIfUserAlreadyLikedSpecificComplaint(Minha_Reclamacao mr){
        Cursor ListOfComplaintsLikedByTheUser = bd.getUsersLikedComplaints(user.getUid());
        if(ListOfComplaintsLikedByTheUser != null && ListOfComplaintsLikedByTheUser.getCount() > 1){
            do{
                String likedComplaint = ListOfComplaintsLikedByTheUser.getString(0);
                if(likedComplaint.equals(mr.getCodigo_reclamacao())){
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }while(ListOfComplaintsLikedByTheUser.moveToNext());
        }
    }

    private void setLikesCounter(Minha_Reclamacao mr){
        String codReclamcao = mr.getCodigo_reclamacao();
        Cursor cursor = bd.getQuantidadeLikes(codReclamcao);
        if(cursor != null){
            do{
                String qtdLikes = cursor.getString(0);
                cont_likes.setText(qtdLikes);
            }while(cursor.moveToNext());
        }
    }
}
