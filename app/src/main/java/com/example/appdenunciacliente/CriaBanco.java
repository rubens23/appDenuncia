package com.example.appdenunciacliente;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "bancoAppDenunciasCliente.db";
    private static final int VERSAO = 1;

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE clientes ("
                +"id_cliente integer primary key autoincrement,"
                +"nome text,"
                +"email text,"
                +"tel_contato text,"
                +"bairro text,"
                +"cidade text,"
                +"estado text)";

        db.execSQL(sql);
        String sql2 = "CREATE TABLE denuncias ("
                +"codigo_reclamacao integer primary key autoincrement,"
                +"user_id text,"
                +"reclamacao text,"
                +"status_reclamacao text,"
                +"tipo_reclamacao)";

        db.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       db.execSQL("DROP TABLE IF EXISTS clientes");
       db.execSQL("DROP TABLE IF EXISTS denuncias");
       onCreate(db);
    }
}
