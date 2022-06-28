package com.example.appdenunciacliente.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

//TODO CRIAR A TABELA QUE DEFINI NO PLANEJAMENTO
public class CriaBanco extends SQLiteOpenHelper {

    String numVersaoAntiga;

    private static final String NOME_BANCO = "bancoAppDenunciasCliente.db";
    private static final int VERSAO = 12;
    Context ctx;

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE clientes ("
                + "id_cliente integer primary key autoincrement,"
                + "nome text,"
                + "email text,"
                + "tel_contato text,"
                + "bairro text,"
                + "cidade text,"
                + "estado text)";

        db.execSQL(sql);
        String sql2 = "CREATE TABLE denuncias ("
                + "codigo_reclamacao integer primary key autoincrement,"
                + "user_id text,"
                + "reclamacao text,"
                + "status_reclamacao text,"
                + "tipo_reclamacao," +
                "contador_likes)";

        db.execSQL(sql2);


        Toast.makeText(ctx, "On create", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        if(newVersion > oldVersion){
            String sql3 = "CREATE TABLE CurtidasUsuarios ("
                    + "id_usuario text,"
                    + "id_reclamacao_curtida text)";
            db.execSQL(sql3);
        }

         */
        /*
        if(newVersion > oldVersion){
            String sql4 = "CREATE TABLE Comentarios("
                    +"id_usuario text,"
                    +"comentario text,"
                    +"id_reclamacao text)";
            db.execSQL(sql4);
        }

         */
        if(newVersion > oldVersion){
            String sql5 = "CREATE TABLE imagens_denuncias("
                    +"id_reclamacao integer primary key," +
                    "user_id text,"
                    +"link_imagem text)";

            db.execSQL(sql5);
        }

    }

}
