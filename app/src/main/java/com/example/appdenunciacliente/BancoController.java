package com.example.appdenunciacliente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context){
        banco = new CriaBanco(context);
    }

    public String inserirDadosReclamacoes(String user_id, String reclamacao,String status_reclamacao, String tipo_reclamacao){
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("user_id", user_id);
        valores.put("reclamacao", reclamacao);
        valores.put("status_reclamacao", status_reclamacao);
        valores.put("tipo_reclamacao", tipo_reclamacao);

        resultado = db.insert("denuncias", null, valores);
        db.close();

        if(resultado == -1){
            return "Erro ao inserir denúncia";
        }else{
            return "denúncia inserida com sucesso";
        }
    }

    public Cursor carregaDadoPeloUserId(String id){
        Cursor cursor;
        String[] campos = {"reclamacao", "status_reclamacao"};
        String where = "user_id= '"+id+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("denuncias", campos, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }



}
