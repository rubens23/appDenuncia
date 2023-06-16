package com.example.appdenunciacliente.framework.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdenunciacliente.framework.data.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroFragmentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _resultadoCadastro: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val resultadoCadastro: SharedFlow<String> = _resultadoCadastro


    fun cadastrarNovoUsuario(email: String, senha: String, primeiroNome: String, sobrenome: String, cidade: String) {
        firebaseRepository.cadastrarNovoUsuario(email, senha, primeiroNome, sobrenome, cidade, cadastrouComSucesso= ::cadastrouComSucesso)
    }

    private fun cadastrouComSucesso(resultadoCadastro: String){
        viewModelScope.launch {
            _resultadoCadastro.emit(resultadoCadastro)
        }
    }

}