package com.example.appdenunciacliente.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.ViewHolders.MinhaReclamacaoViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MinhasReclamacoesAdapter extends RecyclerView.Adapter<MinhaReclamacaoViewHolder> {
    private List<Minha_Reclamacao> lista = new ArrayList<Minha_Reclamacao>();
    private Context context;

    public MinhasReclamacoesAdapter(Context ctx){
        this.context = ctx;

    }
    @NonNull
    @Override
    public MinhaReclamacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_minha_reclamacao, parent, false);
        return new MinhaReclamacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MinhaReclamacaoViewHolder holder, int position) {
        Minha_Reclamacao mr = lista.get(position);
        holder.bindData(mr);


    }

    public void setItems(List<Minha_Reclamacao> arrayMR){
        lista.addAll(arrayMR);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


}
