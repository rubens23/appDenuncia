package com.example.appdenunciacliente.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.databinding.ActivityEsqueciSenhaBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EsqueciSenha : AppCompatActivity() {
    private lateinit var binding: ActivityEsqueciSenhaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsqueciSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getEmailForResettingPassword()


    }

    private fun getEmailForResettingPassword(){
        binding.btnResetPassword.setOnClickListener {
            if(!binding.emailReset.text.toString().isBlank() ||
                binding.emailReset.text.toString().isEmpty() ||
                binding.emailReset.text.toString().equals(null)){
                val email = binding.emailReset.text.toString()
                sendEmailToResetPassword(email)
            }
        }
    }

    private fun sendEmailToResetPassword(email: String) {
        if(!email.isEmpty() || email.equals(null)){
            Firebase.auth.setLanguageCode("pt")
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Um e-mail foi enviado para você mudar a sua senha!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }else{
                        Toast.makeText(this, "O e-mail que você digitou não foi encontrado em nossa base de dados!", Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(this, "Digite um e-mail válido!", Toast.LENGTH_LONG).show()
        }
    }
}