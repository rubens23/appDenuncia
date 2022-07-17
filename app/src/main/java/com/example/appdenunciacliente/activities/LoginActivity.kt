package com.example.appdenunciacliente.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.appdenunciacliente.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onResume() {
        super.onResume()
        btnFazerConta.setOnClickListener{
            startActivity(Intent(this, ActivityRegistro::class.java))
        }
        btnLogin.setOnClickListener {
            fazerLogin()
        }
        btnEsqueciSenha.setOnClickListener {
            startActivity(Intent(this, EsqueciSenha::class.java))
        }

    }

    private fun fazerLogin() {
        auth = FirebaseAuth.getInstance()
        val emailLogin = email.text.toString().trim()
        val senhaLogin = senha.text.toString().trim()
        if(emailLogin.isEmpty()){
            var msg = "O email precisa ser preenchido"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches()){
            var msg ="Você não digitou um email válido"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            return
        }
        else if(senhaLogin.isEmpty()){
            var msg = "A senha precisa ser preenchida"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(emailLogin, senhaLogin).addOnCompleteListener(this){
                    task->
                if(task.isSuccessful){
                    startActivity(Intent(this, MenuActivity::class.java))
                }else{
                    Toast.makeText(this, "E-mail e/ou senha errado(a)", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}
