package com.example.appdenunciacliente.framework.data.models

import java.io.Serializable

data class Denuncias(
    val userId: String = "",
    val denuncia: String = "",
    val statusDenuncia: String= "",
    val tipoDenuncia: String = "",
    val qntLikes: Long = 0L,
    val idDenuncia: String = "",
    val linkImagemDenuncia: String = "",
    val listaUsuariosQuGostaram: ArrayList<String> = arrayListOf(),
    val listaComentarios: ArrayList<Comentarios> = arrayListOf(),
    val nomeOp: String = ""
): Serializable


