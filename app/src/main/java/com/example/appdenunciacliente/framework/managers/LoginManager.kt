package com.example.appdenunciacliente.framework.managers

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.example.appdenunciacliente.ui.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow

interface LoginManager {
    suspend fun login(email: String, senha: String, activityContext: Activity)
    var preencherEmailLD: MutableLiveData<String>
    var emailInvalidoLD: MutableLiveData<String>
    var preencherSenhaLD: MutableLiveData<String>
    var emailOuSenhaIncorretaLD: MutableLiveData<String>
    var loginBemSucedidoLD: MutableLiveData<String>
    suspend fun login2(email: String, senha: String): Boolean
}