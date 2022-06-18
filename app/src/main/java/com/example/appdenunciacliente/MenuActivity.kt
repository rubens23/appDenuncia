package com.example.appdenunciacliente

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        var user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user != null){
            var bd : BancoController = BancoController(this)
            var id_usuario : String = user.uid
            var cursor : Cursor = bd.getUsersLikedComplaints(id_usuario);
            if(cursor != null){

            }

        }

        btn_novas_reclamacoes.setOnClickListener{
            startActivity(Intent(this, NovasReclamacoes::class.java))
        }
        btn_minhas_reclamacoes.setOnClickListener {
            startActivity(Intent(this, ActivityMinhasReclamacoes::class.java))
        }
        btn_reclamacoes_comunidade.setOnClickListener {
            startActivity(Intent(this, ReclamacoesComunidade::class.java))
        }

    }
}