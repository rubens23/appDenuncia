package com.example.appdenunciacliente.framework.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.framework.data.repositories.FirebaseRepository
import com.example.appdenunciacliente.framework.data.repositories.FirebaseStorageRepository
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComQuantidadeDeLikesAtualizada
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.GostouDaDenuncia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MinhasDenunciasFragmentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository
): ViewModel() {

    private val _getUserDenunciasResult = MutableSharedFlow<MutableList<Denuncias>>(replay = 0)
    val getUserDenunciasResult: SharedFlow<MutableList<Denuncias>> = _getUserDenunciasResult

    private val _passouImagemParaFirebase = MutableSharedFlow<DenunciaComPosicaoEImagem>(replay = 0)
    val passouImagemParaFireabse: SharedFlow<DenunciaComPosicaoEImagem> = _passouImagemParaFirebase

    private val _passouImagemParaDenuncia = MutableSharedFlow<DenunciaComPosicaoEImagem>(replay = 0)
    val passouImagemParaDenuncia: SharedFlow<DenunciaComPosicaoEImagem> = _passouImagemParaDenuncia

    private val _usuarioGostouDaReclamacao = MutableSharedFlow<GostouDaDenuncia>(replay = 0)
    val usuarioGostouDaReclamacao: SharedFlow<GostouDaDenuncia> = _usuarioGostouDaReclamacao

    private val _atualizacaoDenuncia = MutableSharedFlow<DenunciaComPosicao>(replay = 0)
    val atualizacaoDenuncia: SharedFlow<DenunciaComPosicao> = _atualizacaoDenuncia

    private val _atualizacaoDenunciaComImagemEPosicao = MutableSharedFlow<DenunciaComPosicao>(replay = 0)
    val atualizacaoDenunciaComImagemEPosicao: SharedFlow<DenunciaComPosicao> = _atualizacaoDenunciaComImagemEPosicao



    private val _qntLikesDepoisDeTirar = MutableSharedFlow<DenunciaComQuantidadeDeLikesAtualizada>(replay = 0)
    val qntLikesDepoisDeTirar: SharedFlow<DenunciaComQuantidadeDeLikesAtualizada> = _qntLikesDepoisDeTirar

    private val _atualizarLikesDepoisDeTirarLike = MutableSharedFlow<DenunciaComQuantidadeDeLikesAtualizada>(replay = 0)
    val atualizarLikesDepoisDeTirarLike: SharedFlow<DenunciaComQuantidadeDeLikesAtualizada> = _atualizarLikesDepoisDeTirarLike

    private val _atualizarLikesDepoisDeColocarLike = MutableSharedFlow<DenunciaComQuantidadeDeLikesAtualizada>(replay = 0)
    val atualizarLikesDepoisDeColocarLike: SharedFlow<DenunciaComQuantidadeDeLikesAtualizada> = _atualizarLikesDepoisDeColocarLike

    private val _qntDeLikesDepoisDeColocar = MutableSharedFlow<DenunciaComQuantidadeDeLikesAtualizada>(replay = 0)
    val qntDeLikesDepoisDeColocar: SharedFlow<DenunciaComQuantidadeDeLikesAtualizada> = _qntDeLikesDepoisDeColocar



    fun getDenunciasDoUsuario(){
        firebaseRepository.getDenunciasUsuario(onSuccessGetDenunciasUsuario = ::onSuccessGetUserDenuncias, onFailureGetDenunciasUsuarioError = ::onFailureGetUserDenuncias)
    }

    fun verSeUsuarioGostouDaReclamacao(idDenuncia: String, position: Int, userId: String){
        firebaseRepository.verSeUsuarioGostouDaDenuncia(idDenuncia, userId, position, gostouDaDenuncia = ::verSeUsuarioGostouDaDenuncia)

    }

    private fun onSuccessGetUserDenuncias(list: MutableList<Denuncias>){
        viewModelScope.launch {
            list.forEach {
                Log.d("testandodenuncias", "to no viewModel ${it.denuncia}")
            }
            _getUserDenunciasResult.emit(list)
        }
    }

    private fun onFailureGetUserDenuncias(error: String){
        Log.d("errogetlista", error)

    }



    fun getQuantidadeDeLikesParaTirarLike(idDenuncia: String, idUser: String, gostou: GostouDaDenuncia) {
        firebaseRepository.getQuantidadeDeLikesParaTirarLike(idDenuncia, idUser, gostou, getQuantidadeLikes = ::getQuantidadeDeLikesAntesDeTirarLike)
    }

    fun tirarLike(novaQntDeLikes: Long, idDenuncia: String, uid: String, gostou: GostouDaDenuncia) {
        firebaseRepository.atualizarNumeroDeLikesDepoisDeTirarOLike(novaQntDeLikes, idDenuncia, uid, gostou, atualizarNumeroDeLikesDepoisDeRetirar = ::atualizarNumeroDeLikesDepoisDeRetirar)
    }

    fun darLike(novaQntDeLikes: Long, idDenuncia: String, idUsuario: String, gostou: GostouDaDenuncia) {
        firebaseRepository.atualizarNumeroDeLikesDepoisDeColocarOLike(novaQntDeLikes, idDenuncia, idUsuario, gostou, atualizarNumeroDeLikesDepoisDeColocar = ::atualizarNumeroDeLikesDepoisDeColocar)
    }

    fun getQuantidadeDeLikesParaColocarLike(idDenuncia: String, idUser: String, gostou: GostouDaDenuncia) {
        firebaseRepository.getQuantidadeDeLikesParaColocarOLike(idDenuncia, idUser, gostou, getQntLikes = ::getQuantidadeDepoisDeColocarOLike)
    }



    private fun verSeUsuarioGostouDaDenuncia(gostou: GostouDaDenuncia){
        viewModelScope.launch {
            _usuarioGostouDaReclamacao.emit(gostou)
        }

    }

    private fun getQuantidadeDeLikesAntesDeTirarLike(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada){
        viewModelScope.launch {
            _qntLikesDepoisDeTirar.emit(denunciaComLikesAtualizados)
        }
    }

    private fun atualizarNumeroDeLikesDepoisDeRetirar(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada){
        viewModelScope.launch {
            _atualizarLikesDepoisDeTirarLike.emit(denunciaComLikesAtualizados)
        }
    }

    private fun atualizarNumeroDeLikesDepoisDeColocar(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada){
        viewModelScope.launch {
            _atualizarLikesDepoisDeColocarLike.emit(denunciaComLikesAtualizados)
        }
    }

    private fun getQuantidadeDepoisDeColocarOLike(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada){
        viewModelScope.launch {
            _qntDeLikesDepoisDeColocar.emit(denunciaComLikesAtualizados)
        }
    }



    fun pegarDenunciaAtualizada(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada) {
        firebaseRepository.pegarDenunciaAtualizada(denunciaComLikesAtualizados, atualizarDenuncia = ::atualizarDenuncia)

    }

    fun pegarDenunciaComImagemAtualizada(denunciaComImagemAtualizada: DenunciaComPosicaoEImagem) {
        firebaseRepository.pegarDenunciaComImagemAtualizada(denunciaComImagemAtualizada, pegarDenunciaComImagemAtualizada = ::pegarDenunciaComImagemAtualizadaCallback)

    }

    private fun pegarDenunciaComImagemAtualizadaCallback(denunciaComPosicao: DenunciaComPosicao){
        viewModelScope.launch {
            _atualizacaoDenunciaComImagemEPosicao.emit(denunciaComPosicao)
        }
    }

    private fun atualizarDenuncia(denunciaComPosicao: DenunciaComPosicao){
        viewModelScope.launch {
            _atualizacaoDenuncia.emit(denunciaComPosicao)
        }
    }

    fun passarImagemParaOFirebaseStorage(imageUri: Uri, denunciaComPosicao: DenunciaComPosicao) {
        firebaseStorageRepository.passarImagemParaOFirebaseStorage(imageUri, denunciaComPosicao, passouImagemParaFirebaseStorage = ::passouImagemParaFirebaseStorage)
    }

    private fun passouImagemParaFirebaseStorage(denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem){
        viewModelScope.launch {
            _passouImagemParaFirebase.emit(denunciaComPosicaoEImagem)

        }

    }

    fun salvarImagemNaDenuncia(denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem) {
        firebaseRepository.salvarImagemNaDenuncia(denunciaComPosicaoEImagem, passouImagemParaDenuncia = ::passouImagemParaDenuncia)

    }

    private fun passouImagemParaDenuncia(denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem){
        viewModelScope.launch {
            _passouImagemParaDenuncia.emit(denunciaComPosicaoEImagem)
        }
    }
}