package com.example.appdenunciacliente.framework.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdenunciacliente.framework.managers.LoginManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val loginManager: LoginManager
): ViewModel() {

    private val _loginResult = MutableSharedFlow<String>(replay = 0)
    val loginResult: SharedFlow<String> = _loginResult

    fun fazerLogin2(email: String, senha: String){
        viewModelScope.launch {
            val success = loginManager.login2(email, senha)

            _loginResult.emit(if (success) "Login bem-sucedido!" else "Falha no login")
        }

    }


}