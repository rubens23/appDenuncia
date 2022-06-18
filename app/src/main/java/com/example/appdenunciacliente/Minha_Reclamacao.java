package com.example.appdenunciacliente;

import androidx.recyclerview.widget.RecyclerView;

public class Minha_Reclamacao{
    String reclamacao;
    String status;
    String codigo_reclamacao;

    public Minha_Reclamacao(){}

    public String getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(String reclamacao) {
        this.reclamacao = reclamacao;
    }

    public String getCodigo_reclamacao(){
        return codigo_reclamacao;
    }

    public void setCodigo_reclamacao(String codigo){
        this.codigo_reclamacao = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
