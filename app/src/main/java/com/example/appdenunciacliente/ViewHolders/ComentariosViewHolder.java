package com.example.appdenunciacliente.ViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.database.BancoController;
import com.example.appdenunciacliente.models.ComentariosModel;
import com.example.appdenunciacliente.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ComentariosViewHolder extends RecyclerView.ViewHolder {

    private TextView comentario;
    private BancoController bd;
    private Context ctx;
    private FirebaseUser user;

    public ComentariosViewHolder(@NonNull View itemView) {
        super(itemView);

        ctx = itemView.getContext();
        bd = new BancoController(ctx);
        comentario = itemView.findViewById(R.id.comentario);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void bindData(ComentariosModel cm) {
        String comment = cm.getComentario();
        comentario.setText(comment);
    }
}
