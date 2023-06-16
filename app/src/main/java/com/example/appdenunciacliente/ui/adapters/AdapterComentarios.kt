package com.example.appdenunciacliente.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appdenunciacliente.framework.data.models.Comentarios
import com.example.appdenunciacliente.databinding.ItemComentarioBinding
import com.example.appdenunciacliente.ui.adapters.utils.callbackinterfaces.InterfaceAdapterComentarios

class AdapterComentarios(var listaComentarios: ArrayList<Comentarios>, private val interfaceAdapterCometarios: InterfaceAdapterComentarios): RecyclerView.Adapter<AdapterComentarios.ViewHolder>() {

    inner class ViewHolder(val binding: ItemComentarioBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comentario: Comentarios){
            initViews(comentario)

        }

        private fun initViews(comentario: Comentarios) {
            binding.comentario.text = comentario.comentario
            binding.horaComentario.text = comentario.dataComentario
            binding.nomeUser.text = comentario.nomeUsuario

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemComentarioBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listaComentarios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaComentarios[listaComentarios.size - position - 1])
    }



}