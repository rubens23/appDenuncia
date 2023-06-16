package com.example.appdenunciacliente.ui.adapters.utils.dataclasses

import com.example.appdenunciacliente.framework.data.models.Denuncias

data class DenunciaComPosicaoEImagem(

    val denuncia: Denuncias,
    val recyclerPosition: Int,
    val linkImagemRecemSalva: String


)
