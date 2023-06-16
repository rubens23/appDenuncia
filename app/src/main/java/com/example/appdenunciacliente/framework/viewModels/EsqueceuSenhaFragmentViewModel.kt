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
class EsqueceuSenhaFragmentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _enviouEmail: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val enviouEmail: SharedFlow<Boolean> = _enviouEmail

    fun sendEmailToResetPassword(email: String) {
        firebaseRepository.sendEmailToResetPassword(email, enviouEmail = ::enviouEmail)

    }

    private fun enviouEmail(enviou: Boolean){
        viewModelScope.launch {
            _enviouEmail.emit(enviou)
        }
    }


}