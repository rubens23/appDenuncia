package com.example.appdenunciacliente.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.models.Minha_Reclamacao;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.ViewHolders.ReclamacoesComunidadeViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ReclamacoesComunidadeAdapter extends RecyclerView.Adapter<ReclamacoesComunidadeViewHolder> {

    private Context contexto;
    private List<Minha_Reclamacao> listaTodasReclamacoes = new ArrayList<>();

    public ReclamacoesComunidadeAdapter(Context ctx){
        contexto = ctx;
    }
    @NonNull
    @Override
    public ReclamacoesComunidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_minha_reclamacao, parent, false);
        return new ReclamacoesComunidadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamacoesComunidadeViewHolder holder, int position) {
        Minha_Reclamacao mr = listaTodasReclamacoes.get(position);
        holder.bindData(mr);
    }


    public void setItemList(List<Minha_Reclamacao> mr){
        listaTodasReclamacoes.addAll(mr);
    }

    @Override
    public int getItemCount() {
        return listaTodasReclamacoes.size();
    }
}
