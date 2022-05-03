package com.example.appdenunciacliente.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.Minha_Reclamacao;
import com.example.appdenunciacliente.R;

public class MinhaReclamacaoViewHolder extends RecyclerView.ViewHolder {

    TextView text_view_reclamacao, text_view_label_status, text_view_status, cont_likes;
    ImageView heart_btn;
    boolean btn_pressed = false;

    public MinhaReclamacaoViewHolder(@NonNull View itemView) {
        super(itemView);
        //aqui vão as views lá do layout de item
        //a view será enviada la do adaptador

        //ids dos elementos que serão modificados
        /*
        tv_minha_reclamacao
        label_status
        tv_status
        img_view_like
        contador_likes
         */
        text_view_reclamacao = itemView.findViewById(R.id.tv_minha_reclamacao);
        text_view_label_status = itemView.findViewById(R.id.label_status);
        text_view_status = itemView.findViewById(R.id.tv_status);
        cont_likes = itemView.findViewById(R.id.contador_likes);
        heart_btn = itemView.findViewById(R.id.img_view_like);

        heart_btn.setOnClickListener(v->{
            String imgName = heart_btn.getResources().getResourceEntryName(R.drawable.ic_baseline_favorite_border_24);
            if(imgName.equals("ic_baseline_favorite_border_24")){
                heart_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
            }else if(imgName.equals("ic_baseline_favorite_24")){
                heart_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
        });

    }

    public void bindData(Minha_Reclamacao mr) {
        text_view_reclamacao.setText(mr.getReclamacao());
        text_view_status.setText(mr.getStatus());
    }
}
