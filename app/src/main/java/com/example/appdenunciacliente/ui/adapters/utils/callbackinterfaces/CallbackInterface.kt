package com.example.appdenunciacliente.ui.adapters.utils.callbackinterfaces

import android.content.Intent
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao


interface CallbackInterface{
        fun selectImage(reclamacao: DenunciaComPosicao){}
        fun verSeUsuarioJaGostaDessaDenuncia(denuncia: Denuncias, position: Int){}
        fun clickBtnComentario(denuncia: Denuncias) {}

}
