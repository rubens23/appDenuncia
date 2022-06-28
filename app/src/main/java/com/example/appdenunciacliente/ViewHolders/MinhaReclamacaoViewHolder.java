package com.example.appdenunciacliente.ViewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appdenunciacliente.database.BancoController;
import com.example.appdenunciacliente.models.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

//TODO colocar o onclick do botao de abrir para comentar.

public class MinhaReclamacaoViewHolder extends RecyclerView.ViewHolder {
    private TextView text_view_reclamacao, text_view_label_status,
            text_view_status, cont_likes;
    private ImageView heart_btn, imagem_reclamacao;
    private FloatingActionButton btn_newImage;
    private Context ctx;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private BancoController bd;
    private Uri filePath;

    private OnPictureTakenPassViewHolderData onCardInfoListener;



    private final int PICK_IMAGE_REQUEST = 2;//eu coloquei qualquer valor aqui deliberadamente

    public MinhaReclamacaoViewHolder(@NonNull View itemView){
        super(itemView);


        initViewHolderItems();
        try{
            this.onCardInfoListener = ((OnPictureTakenPassViewHolderData) ctx);
        }catch(ClassCastException e){
            throw new ClassCastException(e.getMessage());
        }




    }

    private void initViewHolderItems() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ctx = itemView.getContext();
        imagem_reclamacao = itemView.findViewById(R.id.imagem_reclamacao);
        btn_newImage = itemView.findViewById(R.id.btn_newImage);
        text_view_reclamacao = itemView.findViewById(R.id.tv_minha_reclamacao);
        text_view_label_status = itemView.findViewById(R.id.label_status);
        text_view_status = itemView.findViewById(R.id.tv_status);
        cont_likes = itemView.findViewById(R.id.contador_likes);
        heart_btn = itemView.findViewById(R.id.img_view_like);
        user = FirebaseAuth.getInstance().getCurrentUser();
        bd = new BancoController(ctx);

    }



    public void bindData(Minha_Reclamacao mr, Uri pathUri){
        filePath = pathUri;
        if(user != null){
            if(filePath != null){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), filePath);
                    imagem_reclamacao.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            btn_newImage.setOnClickListener(v->{
                selectImage(mr.getCodigo_reclamacao());
            });
        }


        putComplaintsInRecyclerView(mr);
        verifyIfUserAlreadyLikedSpecificComplaint(mr);
        setLikesCounter(mr);
        if(user != null){

            heart_btn.setOnClickListener(v->{
                Cursor isThereALike = bd.seeIfUserLikedParticularComplaint(user.getUid(), mr.getCodigo_reclamacao());
                if (isThereALike.getCount()>0) {
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    String resultMinusOneLike = bd.subtractOneLike(mr.getCodigo_reclamacao());
                    String codReclamacaoTirarLike = mr.getCodigo_reclamacao();
                    Cursor cursor = bd.getQuantidadeLikes(codReclamacaoTirarLike);
                    if(cursor != null){
                        do{
                            String qtdLikes = cursor.getString(0);
                            cont_likes.setText(qtdLikes);
                        }while(cursor.moveToNext());
                        String excluirCurtida = bd.deleteLikedComplaint(user.getUid(), codReclamacaoTirarLike);
                    }
                    }else{
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                    String resultadoLike = bd.adicionarLike(mr.getCodigo_reclamacao());
                    String codReclamacao = mr.getCodigo_reclamacao();
                    Cursor cursor = bd.getQuantidadeLikes(codReclamacao);
                    if(cursor != null){
                        do{
                            String qtdLikes = cursor.getString(0);
                            cont_likes.setText(qtdLikes);
                        }while(cursor.moveToNext());
                        String armazenarNasCurtidas = bd.setLikedComplaintId(user.getUid(), codReclamacao);
                    }
                }
            });
        }
    }

    private void selectImage(String id_reclamacao){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("id_reclamacao", id_reclamacao);
        onCardInfoListener.onPictureTakenPassViewHolderData(intent);
        ((Activity)ctx).startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }




    private void putComplaintsInRecyclerView(Minha_Reclamacao mr) {
        text_view_reclamacao.setText(mr.getReclamacao());
        text_view_status.setText(mr.getStatus());
    }

    private void verifyIfUserAlreadyLikedSpecificComplaint(Minha_Reclamacao mr){
        Cursor complaintsLikedByTheUser = bd.getUsersLikedComplaints(user.getUid());
        if(complaintsLikedByTheUser != null && complaintsLikedByTheUser.getCount()>0){
            do{
                String likedComplaint = complaintsLikedByTheUser.getString(0);
                if(likedComplaint.equals(mr.getCodigo_reclamacao())){
                    heart_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                }

            }while(complaintsLikedByTheUser.moveToNext());
        }

    }

    private void setLikesCounter(Minha_Reclamacao mr){
        String codReclamacao = mr.getCodigo_reclamacao();
        Cursor cursor = bd.getQuantidadeLikes(codReclamacao);
        if(cursor != null){
            do{
                String qtdLikes = cursor.getString(0);
                cont_likes.setText(qtdLikes);
            }while(cursor.moveToNext());
        }
    }

    public interface OnPictureTakenPassViewHolderData {
        public void onPictureTakenPassViewHolderData(Intent intent);

    }

}