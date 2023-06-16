package com.example.appdenunciacliente.framework.data.repositories

import android.net.Uri
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem

interface FirebaseStorageRepository {
    fun passarImagemParaOFirebaseStorage(imageUri: Uri, denunciaComPosicao: DenunciaComPosicao, passouImagemParaFirebaseStorage: (passouImagem: DenunciaComPosicaoEImagem)->Unit)
}