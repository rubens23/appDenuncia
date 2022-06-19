package com.example.appdenunciacliente;

public class DataComplaintsImages extends Minha_Reclamacao {
    private String user_id;
    private String link_imagem;

    public String getUser_id(){
        return user_id;
    }

    public String getLink_imagem(){
        return link_imagem;
    }

    public void setUser_id(String uid){
        this.user_id = uid;
    }

    public void setLink_imagem(String li){
        this.link_imagem = li;
    }
}
