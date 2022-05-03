package com.example.appdenunciacliente;

public class Minha_Reclamacao {
    String reclamacao;
    String status;

    public Minha_Reclamacao(){}

    public Minha_Reclamacao(String reclamacao, String status) {
        this.reclamacao = reclamacao;
        this.status = status;
    }

    public String getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(String reclamacao) {
        this.reclamacao = reclamacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
