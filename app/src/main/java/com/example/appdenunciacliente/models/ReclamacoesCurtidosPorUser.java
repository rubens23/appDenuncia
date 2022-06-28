package com.example.appdenunciacliente.models;

public class ReclamacoesCurtidosPorUser {
    String codigo_reclamacao_curtida;

    public ReclamacoesCurtidosPorUser(){}

    public String getCodigo_reclamacao_curtida(){
        return codigo_reclamacao_curtida;
    }

    public void setCodigo_reclamacao_curtida(String codigo){
        this.codigo_reclamacao_curtida = codigo;
    }

}
