package com.example.appdenunciacliente.framework.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.appdenunciacliente.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LoginManagerImpl: LoginManager{
    private lateinit var auth : FirebaseAuth
    override var preencherEmailLD: MutableLiveData<String> = MutableLiveData()
    override var emailInvalidoLD: MutableLiveData<String> = MutableLiveData()
    override var preencherSenhaLD: MutableLiveData<String> = MutableLiveData()
    override var emailOuSenhaIncorretaLD: MutableLiveData<String> = MutableLiveData()
    override var loginBemSucedidoLD: MutableLiveData<String> = MutableLiveData()

    override suspend fun login2(email: String, senha: String): Boolean =
        withContext(Dispatchers.IO){
            initFirebaseAuth()
            return@withContext try{
                val result = auth.signInWithEmailAndPassword(email, senha).await()
                result.user != null
            }catch (e: Exception){
                false
            }

        }




    override suspend fun login(email: String, senha: String, activityContext: Activity) {
        Log.d("entendendbug", "entrei aqui no metodo de login que vai disparar os livedatas")
        initFirebaseAuth()
        if(email.isEmpty()){
            val msg = "O email precisa ser preenchido"
            Log.d("entendendbug", "enviei a mensagem lá para o asFlow")



            //preencherEmailLD.value = msg



        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            val msg ="Você não digitou um email válido"
            emailInvalidoLD.value = msg
            return
        }
        else if(senha.isEmpty()){
            val msg = "A senha precisa ser preenchida"
            preencherSenhaLD.value = msg
        }
        else{



            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(activityContext){
                        task->
                    if(task.isSuccessful){
                        //loginBemSucedidoLD.value = "Login Bem Sucedido!"







                    }else{
                        emailOuSenhaIncorretaLD.value = "E-mail e/ou senha errado(a)"
                    }
                }


        }

    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()

    }


}