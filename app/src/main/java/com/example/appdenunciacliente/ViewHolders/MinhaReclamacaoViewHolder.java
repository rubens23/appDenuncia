package com.example.appdenunciacliente.ViewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;



import com.example.appdenunciacliente.BancoController;
import com.example.appdenunciacliente.DataComplaintsImages;
import com.example.appdenunciacliente.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.ReclamacoesCurtidosPorUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO colocar o onclick do botao de abrir para comentar.

public class MinhaReclamacaoViewHolder extends RecyclerView.ViewHolder {
    private final TextView text_view_reclamacao;
    private final TextView text_view_label_status;
    private final TextView text_view_status;
    private final TextView cont_likes;
    private ImageView heart_btn, imagem_reclamacao;
    private final FloatingActionButton btn_newImage;
    private final Context ctx;
    private final FirebaseUser user;
    private final FirebaseStorage storage;
    private final StorageReference storageReference;
    private final BancoController bd;
    private Uri filePath;



    public MinhaReclamacaoViewHolder(@NonNull View itemView){
        super(itemView);

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


    public void bindData(Minha_Reclamacao mr, Uri pathUri, DataComplaintsImages dci){
        String retornoInsercao = bd.inserirNaTabelaImagens(dci.getCodigo_reclamacao(),
                FirebaseAuth.getInstance().getUid(), dci.getLink_imagem());
        Toast.makeText(ctx, "retorno da inserção: "+retornoInsercao, Toast.LENGTH_LONG).show();
        if(mr.getCodigo_reclamacao().equals(dci.getCodigo_reclamacao())){
            //Toast.makeText(ctx, "os itens tem o mesmo complaint id", Toast.LENGTH_LONG).show();
            //Chamo o picasso para passar o link da imagem para a imageView
            //do item atual
        }
        filePath = pathUri;
        if(user != null){
            if(filePath != null){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), filePath);
                    //imagem_reclamacao.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            btn_newImage.setOnClickListener(v->{
                String idReclamacao = mr.getCodigo_reclamacao();
                selectImage(idReclamacao);
                //uploadImage2();
                //aqui eu posso implementar a intent para o usuario escolher a imagem

                //aqui eu posso usar um método para pegar o id da reclamação associado a essa reclamacao que o usuario acabou de clicar
                //aí fica a pergunta: qual método eu utilizo?

            });
        }

        putComplaintImages(mr);
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

    private void putComplaintImages(Minha_Reclamacao mr) {
        //aqui vai o codigo para colocar as imagens
        //nos itens da recycler view
    }

    private void selectImage(String id_reclamacao){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)ctx).startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem para a sua denúncia"), 2);

    }

    public void uploadImage2(){
        //outro metodo para fazer upload de imagens...para que eu fiz dois
        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        //ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
       //     @Override
        //    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        //        String id_user = user.getUid();
        //        Toast.makeText(ctx, "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
        //    }
    //    });
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

}

//https://stackoverflow.com/questions/18573774/how-to-reduce-an-image-file-size-before-uploading-to-a-server