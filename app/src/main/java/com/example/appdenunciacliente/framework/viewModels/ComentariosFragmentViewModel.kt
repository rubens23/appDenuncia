package com.example.appdenunciacliente.framework.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.framework.data.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComentariosFragmentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _comentarioFoiPostadoNaDenuncia: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val comentarioFoiPostadoNaDenuncia: SharedFlow<Boolean> = _comentarioFoiPostadoNaDenuncia

    private val _denunciaAtualizada: MutableSharedFlow<Denuncias> = MutableSharedFlow(replay = 0)
    val denunciaAtualizada: SharedFlow<Denuncias> = _denunciaAtualizada

    fun enviarComentarioParaDenuncia(comentario: String, idDenuncia: String) {
        firebaseRepository.colocarComentarioaDenuncia(comentario, idDenuncia, comentarioFoiPostadoNaDenuncia = ::comentarioFoiPostadoNaDenuncia)



    }

    private fun comentarioFoiPostadoNaDenuncia(foi: Boolean){
        viewModelScope.launch {
            _comentarioFoiPostadoNaDenuncia.emit(foi)
        }
    }

    fun carregarDenunciaAtualizada(idDenuncia: String) {
        firebaseRepository.pegarDenunciaAtualizadaPorId(idDenuncia, denunciaAtualizada= ::denunciaAtualizada)

    }

    private fun denunciaAtualizada(denunciaAtualizada: Denuncias){
        viewModelScope.launch {
            _denunciaAtualizada.emit(denunciaAtualizada)

        }

    }
}