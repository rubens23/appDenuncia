package com.example.appdenunciacliente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {

    private SQLiteDatabase db;
    private CriaBanco banco;
    private Context ctx;


    public BancoController(Context context){
        banco = new CriaBanco(context);
        ctx = context;

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

    public String adicionarComentario(String user_id, String reclamacao, String id_reclamacao){
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("id_usuario", user_id);
        valores.put("comentario", reclamacao);
        valores.put("id_reclamacao", id_reclamacao);

        resultado = db.insert("Comentarios", null, valores);
        if(resultado == -1){
            return "Erro ao adicionar comentário";
        }else{
            return "Comentário adicionado com sucesso";
        }
    }

    public Cursor carregaDadoPeloUserId(String id){
        Cursor cursor;
        String[] campos = {"reclamacao", "status_reclamacao", "codigo_reclamacao"};
        String where = "user_id= '"+id+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("denuncias", campos, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getComentariosReclamacao(String id_reclamacao){
        Cursor cursor;
        String[] campos = {"comentario"};
        String where = "id_reclamacao= '"+id_reclamacao+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("Comentarios", campos, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor carregaTodasReclamacoes(){
        Cursor cursor;
        String[] campos = {"reclamacao", "status_reclamacao", "codigo_reclamacao"};

        db = banco.getReadableDatabase();
        cursor = db.query("denuncias", campos, null, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public String adicionarLike(String codigo){//TODO arrumar esse método
        BancoController bd = new BancoController(ctx);
        Cursor c = bd.getQuantidadeLikes(codigo);
        int contador_likes = Integer.parseInt(c.getString(0));
        String msg = "Like adicionado com sucesso!";

        db = banco.getReadableDatabase();

        ContentValues valores = new ContentValues();

        valores.put("quantidade_likes", contador_likes+1);
        String condicao = "codigo_reclamacao = '"+codigo+"'";

        int linha;
        linha = db.update("denuncias", valores, condicao, null);

        if(linha < 1){
            msg = "Erro ao adicionar o like";
        }
        return msg;
    }

    public String inserirNaTabelaImagens(String id_reclamacao, String id_user, String link){
        long retorno;
        String msg = "link da imagem adicionado com sucesso";
        db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id_reclamacao", id_reclamacao);
        valores.put("user_id", id_user);
        valores.put("link_imagem", link);
        retorno = db.insert("imagens_denuncias", null, valores);
        if(retorno == -1){
            return "Erro ao adicionar esses dados";
        }else{
            return msg;
        }

    }

    public String subtractOneLike(String codigo){
        BancoController bd = new BancoController(ctx);
        Cursor c = bd.getQuantidadeLikes(codigo);
        int contador_likes = Integer.parseInt(c.getString(0));
        String msg = "Like retirado com sucesso";

        db = banco.getReadableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("quantidade_likes", contador_likes - 1);
        String condicao = "codigo_reclamacao = '"+codigo+"'";

        int linha;
        linha = db.update("denuncias", valores, condicao, null);

        if(linha < 1){
            msg = "Erro ao retirar o Like";
        }
        return msg;
    }

    public Cursor getQuantidadeLikes(String codigo){
        Cursor cursor;
        String[] campo = {"quantidade_likes"};
        String where = "codigo_reclamacao= '"+codigo+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("denuncias", campo, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getCodigoReclamacao(String recl){
        Cursor cursor;
        String[] campo = {"codigo_reclamacao"};
        String where = "reclamacao= '"+recl+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("denuncias",campo, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;//esse cursor vai retornar os codigos da reclamacao passada
        //por parametro
    }
    //metodo de colocar o codigo na tabela de curtidas
    public String setLikedComplaintId(String userId, String complaintLikedCode){
        String msg = "O código da reclamação foi adicionado com sucesso";
        ContentValues cv;
        cv = new ContentValues();
        long resultadoInsercao;
        db = banco.getWritableDatabase();

        cv.put("id_usuario", userId);
        cv.put("id_reclamacao_curtida", complaintLikedCode);
        resultadoInsercao = db.insert("CurtidasUsuarios", null, cv);

        if(resultadoInsercao == -1){
            return "Erro ao inserir codigo da reclamação curtida";
        }else{
            return "Código inserido com sucesso";
        }

    }

    public String deleteLikedComplaint(String userId, String complaintLikedCode){
        String msg = "Like excluído com sucesso";

        db = banco.getReadableDatabase();

        String condicao = "id_usuario= '"+userId+"' and id_reclamacao_curtida= '"+complaintLikedCode+"'";

        int linhas;
        linhas = db.delete("CurtidasUsuarios", condicao, null);

        if(linhas < 1){
            msg = "Erro ao excluir like";
        }

        return msg;
    }

    public Cursor getUsersLikedComplaints(String uid){
        Cursor cursor;
        String[] campo = {"id_reclamacao_curtida"};
        String where = "id_usuario = '"+uid+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("CurtidasUsuarios", campo, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor;
        }
        return null;

    }

    public Cursor seeIfUserLikedParticularComplaint(String userId, String ComplaintCode){
        Cursor cursor;
        String[] campo = {"id_reclamacao_curtida"};
        String condicao = "id_usuario= '"+userId+"' and id_reclamacao_curtida= '"+ComplaintCode+"'";

        db = banco.getReadableDatabase();
        cursor = db.query("CurtidasUsuarios", campo, condicao, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


}
