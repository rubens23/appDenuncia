package com.example.appdenunciacliente.framework.data.models

import android.os.Parcelable
import java.io.Serializable

data class Comentarios(
    var id_usuario: String = "",
    var nomeUsuario: String = "",
    var id_comentario: String = "",
    var comentario: String = "",
    var id_reclamacao: String = "",
    var dataComentario: String = ""
): Serializable
