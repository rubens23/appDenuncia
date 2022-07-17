package com.example.appdenunciacliente.activities

import android.content.Intent
import android.database.Cursor
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.database.BancoController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        configureFonts()

        getLoggedUserLikedComplaints()

        botoesMenu()



    }

    private fun configureFonts() {
        val tf: Typeface = Typeface.createFromAsset(assets, "fonts/Lato-Hairline.ttf")
        lb_title_adicionar_reclamacoes.setTypeface(tf, Typeface.BOLD)
        lb_title_minhas_reclamacoes.setTypeface(tf, Typeface.BOLD)
        lb_title_reclamacoes_comunidades.setTypeface(tf, Typeface.BOLD)
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ciclo", "to no onDestroy da MenuActivity")
    }
}