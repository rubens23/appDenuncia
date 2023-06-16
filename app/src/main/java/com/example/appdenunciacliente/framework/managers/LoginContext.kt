package com.example.appdenunciacliente.framework.managers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class LoginContext(
    var preencherEmailFlow: Flow<String?>,
    var loginBemSucedidoFlow: Flow<String?>

)