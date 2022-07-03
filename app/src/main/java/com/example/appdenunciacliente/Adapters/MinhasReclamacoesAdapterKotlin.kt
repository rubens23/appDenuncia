package com.example.appdenunciacliente.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.support.annotation.NonNull
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.database.BancoController
import com.example.appdenunciacliente.databinding.ItemMinhaReclamacaoBinding
import com.example.appdenunciacliente.models.Minha_Reclamacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_minha_reclamacao.view.*

class MinhasReclamacoesAdapterKotlin
constructor(private val callbackInterface: MinhasReclamacoesAdapterKotlin.CallbackInterface,
                                                 ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lista : MutableList<Minha_Reclamacao> = mutableListOf()
    private var context : Context = ctx
    private lateinit var filepath : Uri









    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): MinhaReclamacaoViewHolder {
        return MinhaReclamacaoViewHolder(
            ItemMinhaReclamacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MinhaReclamacaoViewHolder -> {
                val mr : Minha_Reclamacao = lista.get(position)
                Log.d("listposition", ""+mr.getCodigo_reclamacao()+" "+mr.getReclamacao())
                holder.bind(mr)
                val img : ImageView = holder.itemView.findViewById(R.id.imagem_reclamacao)

            }
        }


    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OnPictureTakenPassViewHolderData {
        fun onPictureTakenPassViewHolderData(intent: Intent)

    }

    interface CallbackInterface{
        fun passResultCallback(intent: Intent)
    }

    fun setItems(arrayMR : List<Minha_Reclamacao>){
        lista.addAll(arrayMR)
    }

    fun setUris(ref: Uri){
        val filepath = ref
    }




    inner class MinhaReclamacaoViewHolder(
        itemView: ItemMinhaReclamacaoBinding,
    ) : RecyclerView.ViewHolder(itemView.root){

        private lateinit var ctx: Context
        private lateinit var user: FirebaseUser
        private lateinit var storage : FirebaseStorage
        private lateinit var storageReference : StorageReference
        private lateinit var bd : BancoController
        private lateinit var filePath: Uri
        private var contador: Int = 0
        //private lateinit var onCardInfoListener : OnPictureTakenPassViewHolderData


        private val PICK_IMAGE_REQUEST: Int = 2



        fun bind(mr: Minha_Reclamacao){

            initViewHolderItems()
            onClickEvents(mr.codigo_reclamacao)
            //eu tenho que de algum jeito ver se esse id de reclamacao tem um
            //link de imagem
            val temLinkImagem: Cursor = bd.getComplaintImage(mr.codigo_reclamacao)
            if(temLinkImagem == null){
                itemView.imagem_reclamacao.setImageResource(R.drawable.ic_launcher_background)

            }




            Log.d("contador", ""+contador);
            val cursorTemImagens : Cursor = bd.getComplaintImage(mr.codigo_reclamacao)
            if(cursorTemImagens.count >0){
                putPhotoInComplaint(mr, cursorTemImagens)
            }




            putComplaintsInRecyclerView(mr);
            verifyIfUserAlreadyLikedSpecificComplaint(mr);
            setLikesCounter(mr);
            if(user != null){

                itemView.img_view_like.setOnClickListener{
                    val isThereALike: Cursor  = bd.seeIfUserLikedParticularComplaint(user.uid, mr.codigo_reclamacao)
                    if (isThereALike.count >0) {
                        itemView.img_view_like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        val resultMinusOneLike: String = bd.subtractOneLike(mr.codigo_reclamacao)
                        val codReclamacaoTirarLike : String = mr.codigo_reclamacao
                        val cursor: Cursor  = bd.getQuantidadeLikes(codReclamacaoTirarLike)
                        if(cursor != null){
                            do{
                                val qtdLikes : String = cursor.getString(0)
                                itemView.contador_likes.text = qtdLikes
                            }while(cursor.moveToNext())
                            val excluirCurtida: String = bd.deleteLikedComplaint(user.uid, codReclamacaoTirarLike);
                        }
                    }else{
                        itemView.img_view_like.setImageResource(R.drawable.ic_baseline_favorite_24)
                        val resultadoLike : String = bd.adicionarLike(mr.codigo_reclamacao)
                        val codReclamacao : String = mr.codigo_reclamacao
                        val cursor: Cursor = bd.getQuantidadeLikes(codReclamacao)
                        if(cursor != null){
                            do{
                                val qtdLikes: String = cursor.getString(0)
                                itemView.contador_likes.text = qtdLikes;
                            }while(cursor.moveToNext());
                            val armazenarNasCurtidas: String = bd.setLikedComplaintId(user.uid, codReclamacao)
                        }
                    }
                }
            }
            contador++

        }

        fun initViewHolderItems() {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            ctx = itemView.context
            user = FirebaseAuth.getInstance().currentUser!!
            bd = BancoController(ctx)
            //onCardInfoListener = (ctx as Activity) as OnPictureTakenPassViewHolderData

        }


        fun putPhotoInComplaint(mr: Minha_Reclamacao, c: Cursor) {
            Log.d("iditembinddata", ""+mr.codigo_reclamacao +c.getString(0))
            Picasso.get().load(c.getString(0)).into(itemView.imagem_reclamacao)

        }


        fun putComplaintsInRecyclerView( mr: Minha_Reclamacao) {
            itemView.tv_minha_reclamacao.text = mr.reclamacao
            itemView.tv_status.text = mr.status
        }

        fun verifyIfUserAlreadyLikedSpecificComplaint(mr: Minha_Reclamacao){
            val complaintsLikedByTheUser: Cursor = bd.getUsersLikedComplaints(user.getUid())
            if(complaintsLikedByTheUser != null && complaintsLikedByTheUser.count >0){
                do{
                    val likedComplaint: String = complaintsLikedByTheUser.getString(0);
                    if(likedComplaint == mr.codigo_reclamacao){
                        itemView.img_view_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }

                }while(complaintsLikedByTheUser.moveToNext());
            }

        }

        fun setLikesCounter(mr: Minha_Reclamacao){
            val codReclamacao: String = mr.codigo_reclamacao
            val cursor: Cursor = bd.getQuantidadeLikes(codReclamacao)
            if(cursor != null){
                do{
                    val qtdLikes: String = cursor.getString(0)
                    itemView.contador_likes.text = qtdLikes
                }while(cursor.moveToNext())
            }
        }

        private fun onClickEvents(mr_id_reclamacao: String) {
            itemView.btn_newImage.setOnClickListener {
                selectImage(mr_id_reclamacao)
            }
        }



        fun selectImage(id_reclamacao: String){
            val intent: Intent= Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT;
            intent.putExtra("id_reclamacao", id_reclamacao)

            //onCardInfoListener.onPictureTakenPassViewHolderData(intent)
            callbackInterface.passResultCallback(intent)
            (ctx as Activity).startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }




    }





}







