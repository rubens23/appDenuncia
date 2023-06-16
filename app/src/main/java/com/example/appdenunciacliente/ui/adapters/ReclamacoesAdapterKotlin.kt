package com.example.appdenunciacliente.ui.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.databinding.ItemMinhaReclamacaoBinding
import com.example.appdenunciacliente.ui.adapters.utils.callbackinterfaces.CallbackInterface
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ReclamacoesAdapterKotlin(private val callbackInterface: CallbackInterface, val auth: FirebaseAuth, val botaoEscolherFoto: Boolean) : RecyclerView.Adapter<ReclamacoesAdapterKotlin.MinhaReclamacaoViewHolder>() {
    var lista : MutableList<Denuncias> = mutableListOf()



    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): MinhaReclamacaoViewHolder {

        return MinhaReclamacaoViewHolder(
            ItemMinhaReclamacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }



    override fun onBindViewHolder(holder: MinhaReclamacaoViewHolder, position: Int) {
        holder.bind(lista[position], position)


    }


    override fun getItemCount(): Int {
        return lista.size
    }




    fun setItems(listaDenuncias : MutableList<Denuncias>){
        lista.addAll(listaDenuncias)
    }






    inner class MinhaReclamacaoViewHolder(
        val binding: ItemMinhaReclamacaoBinding,
    ) : RecyclerView.ViewHolder(binding.root){



        fun bind(denuncia: Denuncias, position: Int){
            setView(denuncia)
            binding.imgViewLike.setOnClickListener { clickLike(denuncia, position) }
            binding.btnNewImage.setOnClickListener {
                if(botaoEscolherFoto){
                    callbackInterface.selectImage(DenunciaComPosicao(denuncia, position))
                }
                }
            binding.btnComentarios1.setOnClickListener { pegarComentariosDaDenuncia(denuncia) }
            binding.btnComentario2.setOnClickListener { pegarComentariosDaDenuncia(denuncia) }


        }

        private fun pegarComentariosDaDenuncia(denuncia: Denuncias) {
            callbackInterface.clickBtnComentario(denuncia)

        }

        private fun clickLike(denuncia: Denuncias, position: Int) {
            heartAnimation()
            callbackInterface.verSeUsuarioJaGostaDessaDenuncia(denuncia, position)

        }

        private fun heartAnimation() {

                val shrinkAnim = ObjectAnimator.ofPropertyValuesHolder(
                    binding.imgViewLike,
                    PropertyValuesHolder.ofFloat("scaleX", 0.8f),
                    PropertyValuesHolder.ofFloat("scaleY", 0.8f)
                ).apply { duration = 100 }
                val growAnim = ObjectAnimator.ofPropertyValuesHolder(
                    binding.imgViewLike,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f)
                ).apply { duration = 100 }

                AnimatorSet().apply { playSequentially(shrinkAnim, growAnim) }.start()



        }

        private fun setView(denuncia: Denuncias) {
            if(!botaoEscolherFoto){
                binding.btnNewImage.visibility = View.GONE
            }
            if(denuncia.linkImagemDenuncia != ""){
                Log.d("controlerepfoto", "coloquei foto na denuncia: ${denuncia.denuncia} porque ela tem um link de foto ${denuncia.linkImagemDenuncia}")
                Picasso.get().load(denuncia.linkImagemDenuncia).into(binding.imagemReclamacao)

            }else{
                binding.imagemReclamacao.setImageResource(R.drawable.ic_launcher_background)

            }
            binding.tvMinhaReclamacao.text = denuncia.denuncia
            binding.tvStatus.text = denuncia.statusDenuncia
            binding.contadorLikes.text = denuncia.qntLikes.toString()
            if(denuncia.listaUsuariosQuGostaram.isNotEmpty()){
                denuncia.listaUsuariosQuGostaram.forEach {
                    if(it == auth.currentUser!!.uid){
                        binding.imgViewLike.setImageResource(R.drawable.ic_baseline_favorite_24)

                    }
                }
            }else{
                binding.imgViewLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)

            }


        }






    }




}








