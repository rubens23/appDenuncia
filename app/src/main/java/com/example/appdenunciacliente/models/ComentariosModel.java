package com.example.appdenunciacliente.models;

public class ComentariosModel {
    String id_usuario;
    String comentario;
    String id_reclamacao;

    public ComentariosModel(){}

    public String getId_usuario() {
        return id_usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public String getId_reclamacao() {
        return id_reclamacao;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setId_reclamacao(String id_reclamacao) {
        this.id_reclamacao = id_reclamacao;
    }
}
