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
class AdicionarDenunciaFragmentViewModel @Inject constructor(
    private var realtimeDatabaseRepository: FirebaseRepository
): ViewModel() {

    private val _envioResult = MutableSharedFlow<String>(replay = 0)
    val envioResult: SharedFlow<String> = _envioResult


    fun enviarReclamacao(reclamacao: String, selectedType: String){
         realtimeDatabaseRepository.enviarReclamacao(reclamacao, selectedType, onSuccess = ::onSuccess, onFailure = ::onFailure)


    }

    private fun onSuccess(){
        viewModelScope.launch {
            _envioResult.emit("Denúncia salva com sucesso")

        }

    }

    private fun onFailure(){
        viewModelScope.launch {
            _envioResult.emit("Erro ao salvar a denúncia")

        }
    }
}