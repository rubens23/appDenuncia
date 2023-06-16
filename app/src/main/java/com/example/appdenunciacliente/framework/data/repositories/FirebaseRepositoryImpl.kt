package com.example.appdenunciacliente.framework.data.repositories

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.appdenunciacliente.framework.data.models.Comentarios
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.framework.data.models.Usuarios
import com.example.appdenunciacliente.framework.utils.CalendarTimeDateUtilitary
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComQuantidadeDeLikesAtualizada
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DeveMudarCorDoCoracao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.GostouDaDenuncia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val calendarTimeDateUtilitary: CalendarTimeDateUtilitary
): FirebaseRepository {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseRefUsuarios: DatabaseReference
    private val user = FirebaseAuth.getInstance().currentUser
    private var userName = ""


    init{
        initFirebaseAuth()
        initFirebaseDatabase()
        getUserNameByUid()

    }

    private fun getUserNameByUid() {

        if(user != null){
            databaseRefUsuarios.orderByChild("idUsuario").equalTo(user.uid).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (denunciaSnapshot in snapshot.children){
                        val user = denunciaSnapshot.getValue(Usuarios::class.java)
                        if (user != null) {
                            userName = user.primeiroNome
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("errogetusername", error.message)
                }

            })
        }

    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }

    override fun cadastrarNovoUsuario(
        email: String,
        senha: String,
        primeiroNome: String,
        sobrenome: String,
        cidade: String,
        cadastrouComSucesso: (mensagemCadastro: String) -> Unit
    ) {
        if(email.isEmpty() || senha.isEmpty() || primeiroNome.isEmpty() || sobrenome.isEmpty() || cidade.isEmpty()){
            cadastrouComSucesso("preencha todos os campos")
        }else if(senha.length < 6){
            cadastrouComSucesso("a senha tem que ter pelo menos 6 caracteres!")
        }else{
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{
                if(it.isSuccessful){
                    if(it.result.user != null){
                        val usuario = Usuarios(it.result.user!!.uid, primeiroNome, sobrenome, email, cidade)
                        databaseRefUsuarios.child(it.result.user!!.uid).setValue(usuario).addOnCompleteListener {
                            cadastrarNoDatabaseTask->
                            if(cadastrarNoDatabaseTask.isSuccessful){
                                cadastrouComSucesso("cadastrado com sucesso")

                            }else{
                                cadastrouComSucesso("falha no cadastro")

                            }
                        }
                    }

                }else{
                    cadastrouComSucesso("falha no cadastro")
                }
            }
        }
    }

    override fun colocarComentarioaDenuncia(
        comentario: String,
        idDenuncia: String,
        comentarioFoiPostadoNaDenuncia: (comentarioFoiPostado: Boolean) -> Unit
    ) {
        databaseReference.orderByChild("idDenuncia").equalTo(idDenuncia).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val comentariosDenuncia = arrayListOf<Comentarios>()
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        comentariosDenuncia.addAll(denuncia.listaComentarios)
                        val comentarioKey = denunciaSnapshot.ref.child("listaComentarios").push().key

                        if(user != null && userName != "" && comentarioKey != null){
                            comentariosDenuncia.add(Comentarios(id_usuario = user.uid, nomeUsuario =  userName,comentario = comentario, id_reclamacao =  idDenuncia, id_comentario =  comentarioKey, dataComentario = calendarTimeDateUtilitary.pegarDataAtualFormatada()))
                            denunciaSnapshot.ref.child("listaComentarios").setValue(comentariosDenuncia)
                            comentarioFoiPostadoNaDenuncia(true)

                        }else{
                            comentarioFoiPostadoNaDenuncia(false)
                        }

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("erropostcoment", error.message)
                comentarioFoiPostadoNaDenuncia(false)
            }

        })

    }

    override fun pegarDenunciaAtualizadaPorId(
        idDenuncia: String,
        denunciaAtualizada: (denuncia: Denuncias) -> Unit
    ) {
        databaseReference.orderByChild("idDenuncia").equalTo(idDenuncia).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val comentariosDenuncia = arrayListOf<Comentarios>()
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                       denunciaAtualizada(denuncia)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetdenuncia", error.message)

            }

        })
    }

    override fun getTodasDenuncias(
        onSuccessGetDenunciasUsuario: (listaDenuncias: MutableList<Denuncias>) -> Unit,
        onFailureGetDenunciasUsuarioError: (failureGetDenuncias: String) -> Unit
    ) {
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val denuncias = mutableListOf<Denuncias>()
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        denuncias.add(denuncia)
                    }
                }

                onSuccessGetDenunciasUsuario(denuncias)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailureGetDenunciasUsuarioError(error.message)
            }

        })
    }

    override fun sendEmailToResetPassword(
        email: String,
        enviouEmail: (enviouEmail: Boolean) -> Unit
    ) {
        if(!email.isEmpty() || email.equals(null)){
            Firebase.auth.setLanguageCode("pt")
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        enviouEmail(true)

                    }else{
                        enviouEmail(false)
                    }
                }
        }else{
            enviouEmail(false)
        }
    }

    override fun enviarReclamacao(reclamacao: String, selectedType: String, onSuccess: () -> Unit, onFailure: () -> Unit){
        var uid: String? = null
        if (user != null) {
            uid = user.uid
        } else {
            onFailure()

        }


        if (reclamacao.isNotEmpty() && uid != null) {
            initFirebaseDatabase()
            val denunciaKey = databaseReference.push().key
            if (denunciaKey != null) {
                val denuncia = Denuncias(userId = uid, denuncia = reclamacao, statusDenuncia = "pendente", tipoDenuncia = selectedType, 0, idDenuncia = denunciaKey, nomeOp = userName)

                databaseReference.child(denunciaKey).setValue(denuncia).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    onFailure()
                }

            }else{
                onFailure()
            }

        }else{
            onFailure()
        }
    }

    override fun getDenunciasUsuario(onSuccessGetDenunciasUsuario: (list: MutableList<Denuncias>) -> Unit, onFailureGetDenunciasUsuarioError: (error: String) -> Unit){
        if(user != null){
            databaseReference.orderByChild("userId").equalTo(user.uid).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userDenuncias = mutableListOf<Denuncias>()
                    for (denunciaSnapshot in snapshot.children){
                        val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                        if (denuncia != null) {
                            userDenuncias.add(denuncia)
                        }
                    }
                    userDenuncias.forEach {
                        Log.d("testandodenuncias", "${it.denuncia}")
                    }
                    onSuccessGetDenunciasUsuario(userDenuncias)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailureGetDenunciasUsuarioError(error.message)
                }

            })
        }
    }

    override fun verSeUsuarioGostouDaDenuncia(idDenuncia: String, idUser: String, position: Int, gostouDaDenuncia: (gostou: GostouDaDenuncia) -> Unit) {
        var gostou = false
        databaseReference.orderByChild("userId").equalTo(idUser).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        denuncia.listaUsuariosQuGostaram.forEach {
                            userIdDaLista->
                            if(userIdDaLista == idUser && denuncia.idDenuncia == idDenuncia){
                                Log.d("testeclique2", "atribuí true a variavel gostou, denuncia: ${denuncia.denuncia} idUser: $idUser")

                                gostou = true
                            }
                        }
                    }
                }
                Log.d("testeclique", "valor que será chamado no calback $gostou")

                gostouDaDenuncia(GostouDaDenuncia(idDenuncia, gostou, position))

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun getQuantidadeDeLikesParaTirarLike(
        idDenuncia: String, idUser:String, gostouDaDenuncia: GostouDaDenuncia, getQuantidadeLikes: (denunciaComQuantidadeDeLikesAtualizada: DenunciaComQuantidadeDeLikesAtualizada)->Unit
    ) {
        var estaNaLista = false
        var qntLikes = 0L
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == idDenuncia){
                            denuncia.listaUsuariosQuGostaram.forEach {
                                    userIdDaLista->
                                if(userIdDaLista == idUser){
                                    estaNaLista = true

                                        val novaListaDeUsuariosQueGostaram = arrayListOf<String>()
                                        novaListaDeUsuariosQueGostaram.addAll(denuncia.listaUsuariosQuGostaram)
                                        novaListaDeUsuariosQueGostaram.remove(idUser)
                                        denunciaSnapshot.ref.child("listaUsuariosQuGostaram").setValue(novaListaDeUsuariosQueGostaram)


                                }
                            }
                            qntLikes = denuncia.qntLikes
                        }

                    }
                }
                if(estaNaLista){
                    getQuantidadeLikes(DenunciaComQuantidadeDeLikesAtualizada(idDenuncia,qntLikes, gostouDaDenuncia))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })

    }

    override fun atualizarNumeroDeLikesDepoisDeTirarOLike(
        novaQntDeLikes: Long,
        idDenuncia: String,
        idUser: String,
        gostou: GostouDaDenuncia,
        atualizarNumeroDeLikesDepoisDeRetirar: (denunciaComQuantidadeAtualizada: DenunciaComQuantidadeDeLikesAtualizada) -> Unit
    ) {
        var qntLikes = 0L
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == idDenuncia){
                            qntLikes = novaQntDeLikes
                            denunciaSnapshot.ref.child("qntLikes").setValue(qntLikes)
                        }

                    }
                }
                atualizarNumeroDeLikesDepoisDeRetirar(DenunciaComQuantidadeDeLikesAtualizada(idDenuncia, qntLikes, gostou))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun getQuantidadeDeLikesParaColocarOLike(
        idDenuncia: String, idUser:String, gostouDaDenuncia: GostouDaDenuncia, getQntLikes: (denunciaComQuantidadeDeLikesAtualizada: DenunciaComQuantidadeDeLikesAtualizada)->Unit

    ) {
        var qntLikes = 0L
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == idDenuncia){
                            qntLikes = denuncia.qntLikes
                        }

                    }
                }
                getQntLikes(DenunciaComQuantidadeDeLikesAtualizada(idDenuncia, qntLikes, gostouDaDenuncia))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun verSeDevePintarCoracaoDeVermelho(
        denunciaAtual: Denuncias,
        uid: String,
        rvPosition: Int,
        devePintarDeVermelho: (deve: DeveMudarCorDoCoracao) -> Unit
    ) {
        var deve = false
        databaseReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        denuncia.listaUsuariosQuGostaram.forEach {
                                userIdDaLista->
                            if(userIdDaLista == uid){
                                deve = true
                            }
                        }


                    }
                }
                Log.d("fluxo6", "chamei o callback devePintar de vermelho")
                devePintarDeVermelho(DeveMudarCorDoCoracao(deve, rvPosition))
            }


            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })

    }

    override fun pegarDenunciaAtualizada(
        denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada,
        atualizarDenuncia: (denunciaComPosicao: DenunciaComPosicao) -> Unit
    ) {
        var denunciaFinal = Denuncias()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == denunciaComLikesAtualizados.idDenuncia){
                            denunciaFinal = denuncia
                        }

                    }
                }
                atualizarDenuncia(DenunciaComPosicao(denunciaFinal, denunciaComLikesAtualizados.gostouDaDenuncia.recyclerPosition))

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun salvarImagemNaDenuncia(
        denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem,
        passouImagemParaDenuncia: (denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem) -> Unit
    ) {
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == denunciaComPosicaoEImagem.denuncia.idDenuncia){
                            if(user != null){
                                if(user.uid == denuncia.userId){
                                    //pode passar link para denuncia
                                    denunciaSnapshot.ref.child("linkImagemDenuncia").setValue(denunciaComPosicaoEImagem.linkImagemRecemSalva)
                                    passouImagemParaDenuncia(denunciaComPosicaoEImagem)

                                }
                            }


                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errosavelink", error.message)
            }

        })
    }

    override fun pegarDenunciaComImagemAtualizada(
        denunciaComImagemAtualizada: DenunciaComPosicaoEImagem,
        pegarDenunciaComImagemAtualizada: (denunciaComPosicao: DenunciaComPosicao) -> Unit
    ) {
        var denunciaFinal = Denuncias()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == denunciaComImagemAtualizada.denuncia.idDenuncia){
                            denunciaFinal = denuncia
                        }

                    }
                }
                pegarDenunciaComImagemAtualizada(DenunciaComPosicao(denunciaFinal, denunciaComImagemAtualizada.recyclerPosition))

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun atualizarNumeroDeLikesDepoisDeColocarOLike(
        novaQntDeLikes: Long,
        idDenuncia: String,
        idUsuario:String,
        gostou: GostouDaDenuncia,
        atualizarNumeroDeLikesDepoisDeColocar: (denunciaComQuantidadeDeLikesAtulizado: DenunciaComQuantidadeDeLikesAtualizada) -> Unit
    ) {
        var qntLikes = 0L
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == idDenuncia){
                            qntLikes = novaQntDeLikes
                            denunciaSnapshot.ref.child("qntLikes").setValue(qntLikes)
                            val listaDeUsuariosQueJaGostaram = denuncia.listaUsuariosQuGostaram
                            if(!listaDeUsuariosQueJaGostaram.contains(idUsuario)){
                                val novaListaDeUsuariosQueGostaram = arrayListOf<String>()
                                novaListaDeUsuariosQueGostaram.addAll(denuncia.listaUsuariosQuGostaram)
                                novaListaDeUsuariosQueGostaram.add(idUsuario)
                                denunciaSnapshot.ref.child("listaUsuariosQuGostaram").setValue(novaListaDeUsuariosQueGostaram)
                            }

                        }

                    }
                }
                atualizarNumeroDeLikesDepoisDeColocar(DenunciaComQuantidadeDeLikesAtualizada(idDenuncia, qntLikes, gostou))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }

    override fun getQuantidadeDeLikes(
        idDenuncia: String,
        callbackQuantidadeLikes: (qntLikes: Long) -> Unit
    ) {
        var qntLikes = 0L
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (denunciaSnapshot in snapshot.children){
                    val denuncia = denunciaSnapshot.getValue(Denuncias::class.java)
                    if (denuncia != null) {
                        if(denuncia.idDenuncia == idDenuncia){
                            qntLikes = denuncia.qntLikes
                        }

                    }
                }
                callbackQuantidadeLikes(qntLikes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errogetlikes", error.message)
            }

        })
    }


    private fun initFirebaseDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("denuncias")
        databaseRefUsuarios = firebaseDatabase.getReference("usuarios")
    }
}