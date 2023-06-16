package com.example.appdenunciacliente.framework.data.models

import java.io.Serializable


data class Usuarios(
    val idUsuario: String = "",
    val primeiroNome: String = "",
    val sobrenome: String = "",
    val email: String = "",
    val cidade: String = "",
): Serializable
