package com.example.appdenunciacliente.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appdenunciacliente.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro.*

class ActivityRegistro : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnCadastro.setOnClickListener {
            cadastrar()
        }
    }

    private fun cadastrar() {
        auth = FirebaseAuth.getInstance()
        val email = emailCadastro.text.toString().trim()
        val senha = senhaCadastro.text.toString().trim()
        if(email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this,"preencha todos os campos", Toast.LENGTH_SHORT).show()
        }else if(senha.length < 6){
            Toast.makeText(this, "a senha tem que ter pelo menos 6 caracteres", Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }else{
                    Toast.makeText(this, "falha no cadastro", Toast.LENGTH_LONG).show()
                }
            }
        }


    }
}