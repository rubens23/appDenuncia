package com.example.appdenunciacliente.activities

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.database.BancoController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        getLoggedUserLikedComplaints()

        botoesMenu()

    }

    private fun botoesMenu() {
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

    private fun getLoggedUserLikedComplaints() {
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val bd : BancoController = BancoController(this)
            val cursor : Cursor = bd.getUsersLikedComplaints(user.uid);
        }
    }
}