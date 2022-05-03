package com.example.appdenunciacliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btn_novas_reclamacoes.setOnClickListener{
            startActivity(Intent(this, NovasReclamacoes::class.java))
        }
        btn_minhas_reclamacoes.setOnClickListener {
            startActivity(Intent(this, ActivityMinhasReclamacoes::class.java))
        }
    }
}