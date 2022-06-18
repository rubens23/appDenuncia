package com.example.appdenunciacliente.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdenunciacliente.ComentariosModel;
import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.ViewHolders.ComentariosViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosViewHolder> {

    Context ctx;
    List<ComentariosModel> listaComents = new ArrayList<>();
    public ComentariosAdapter(Context context){
        ctx = context;
    }
    @NonNull
    @Override
    public ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentariosViewHolder(view);
    }

    public void setItems(List<ComentariosModel> lc){
        listaComents.addAll(lc);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentariosViewHolder holder, int position) {
        ComentariosModel cm = listaComents.get(position);
        holder.bindData(cm);
    }

    @Override
    public int getItemCount() {
        return listaComents.size();
    }
}
