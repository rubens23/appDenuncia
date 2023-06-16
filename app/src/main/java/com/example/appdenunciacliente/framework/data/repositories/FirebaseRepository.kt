package com.example.appdenunciacliente.framework.data.repositories

import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComQuantidadeDeLikesAtualizada
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DeveMudarCorDoCoracao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.GostouDaDenuncia

interface FirebaseRepository {
    fun enviarReclamacao(reclamacao: String, selectedType: String, onSuccess: () -> Unit, onFailure: () -> Unit)

    fun getDenunciasUsuario(onSuccessGetDenunciasUsuario: (list: MutableList<Denuncias>) -> Unit, onFailureGetDenunciasUsuarioError: (error: String) -> Unit)
    fun verSeUsuarioGostouDaDenuncia(idDenuncia: String, idUser: String, position: Int, gostouDaDenuncia: (gostou: GostouDaDenuncia) -> Unit)
    fun getQuantidadeDeLikesParaTirarLike(idDenuncia: String, idUser: String, gostouDaDenuncia: GostouDaDenuncia, getQuantidadeLikes: (denunciaComQuantidadeDeLikesAtualizada: DenunciaComQuantidadeDeLikesAtualizada) -> Unit)
    fun atualizarNumeroDeLikesDepoisDeTirarOLike(novaQntDeLikes: Long, idDenuncia: String, uid: String, gostou: GostouDaDenuncia,atualizarNumeroDeLikesDepoisDeRetirar: (denunciaComQuantidadeAtualizada: DenunciaComQuantidadeDeLikesAtualizada)-> Unit)
    fun getQuantidadeDeLikesParaColocarOLike(idDenuncia: String, idUser:String, gostouDaDenuncia: GostouDaDenuncia, getQntLikes: (denunciaComQuantidadeDeLikesAtualizada: DenunciaComQuantidadeDeLikesAtualizada) -> Unit)
    fun atualizarNumeroDeLikesDepoisDeColocarOLike(novaQntDeLikes: Long, idDenuncia: String, idUsuario:String, gostou: GostouDaDenuncia, atualizarNumeroDeLikesDepoisDeColocar: (denunciaComQuantidadeAtualizada: DenunciaComQuantidadeDeLikesAtualizada)->Unit)
    fun getQuantidadeDeLikes(idDenuncia: String, callbackQuantidadeLikes: (qntLikes: Long)->Unit)
    fun verSeDevePintarCoracaoDeVermelho(denunciaAtual: Denuncias, uid: String, recyclerViewPosition: Int, devePintarDeVermelho: (deve: DeveMudarCorDoCoracao)->Unit)
    fun pegarDenunciaAtualizada(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada, atualizarDenuncia: (denunciaComPosicao: DenunciaComPosicao)->Unit)
    fun salvarImagemNaDenuncia(denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem, passouImagemParaDenuncia: (denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem)->Unit)
    fun pegarDenunciaComImagemAtualizada(denunciaComImagemAtualizada: DenunciaComPosicaoEImagem, pegarDenunciaComImagemAtualizada: (denunciaComPosicao: DenunciaComPosicao)->Unit)
    fun cadastrarNovoUsuario(email: String, senha: String, primeiroNome: String, sobrenome: String, cidade: String, cadastrouComSucesso: (mensagemCadastro: String)->Unit) {

    }

    fun colocarComentarioaDenuncia(comentario: String, idDenuncia: String, comentarioFoiPostadoNaDenuncia: (comentarioFoiPostado: Boolean)->Unit)
    fun pegarDenunciaAtualizadaPorId(idDenuncia: String, denunciaAtualizada: (denuncia: Denuncias)->Unit)
    fun getTodasDenuncias(onSuccessGetDenunciasUsuario: (listaDenuncias: MutableList<Denuncias>)->Unit, onFailureGetDenunciasUsuarioError: (failureGetDenuncias: String)->Unit)
    fun sendEmailToResetPassword(email: String, enviouEmail: (enviouEmail: Boolean) ->Unit)


}