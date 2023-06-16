package com.example.appdenunciacliente.framework.data.repositories

import android.net.Uri
import android.util.Log
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class FIrebaseStorageRepositoryImpl : FirebaseStorageRepository {
    private val user = FirebaseAuth.getInstance().currentUser
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("imagensDenuncias/")

    override fun passarImagemParaOFirebaseStorage(
        imageUri: Uri,
        denunciaComPosicao: DenunciaComPosicao,
        passouImagemParaFirebaseStorage: (passouImagem: DenunciaComPosicaoEImagem) -> Unit
    ) {
        val nomeImagem = "imagem_${System.currentTimeMillis()}.jpg"
        val novaImagemRef = imagesRef.child(nomeImagem)
        val uploadTask = novaImagemRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            val downloadUrlTask = novaImagemRef.downloadUrl
            downloadUrlTask.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                passouImagemParaFirebaseStorage(
                    DenunciaComPosicaoEImagem(
                        denunciaComPosicao.denuncia,
                        denunciaComPosicao.recyclerPosition,
                        imageUrl
                    )
                )

            }
        }.addOnFailureListener {
            Log.d("errosavestorage", it.message.toString())
            passouImagemParaFirebaseStorage(
                DenunciaComPosicaoEImagem(
                    denunciaComPosicao.denuncia,
                    denunciaComPosicao.recyclerPosition,
                    "erro ao salvar"
                )
            )
        }
    }
}