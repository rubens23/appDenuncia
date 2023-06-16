package com.example.appdenunciacliente.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.databinding.FragmentComentariosBinding
import com.example.appdenunciacliente.framework.viewModels.ComentariosFragmentViewModel
import com.example.appdenunciacliente.ui.adapters.AdapterComentarios
import com.example.appdenunciacliente.ui.adapters.utils.callbackinterfaces.InterfaceAdapterComentarios
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlinx.coroutines.flow.collectLatest


class ComentariosFragment : Fragment(), InterfaceAdapterComentarios {

    private lateinit var binding: FragmentComentariosBinding
    private lateinit var adapter: AdapterComentarios
    private lateinit var viewModel: ComentariosFragmentViewModel
    private var idDenuncia = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentComentariosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        getArgs()

        onClickListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[ComentariosFragmentViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.comentarioFoiPostadoNaDenuncia.collectLatest {
                foiPostado->
                validarSeFoiPostado(foiPostado)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.denunciaAtualizada.collectLatest {
                denuncia->
                atualizarRecyclerViewComComentarioNovo(denuncia)
            }
        }
    }

    private fun atualizarRecyclerViewComComentarioNovo(denuncia: Denuncias) {
        val tamanhoAtualDaLista = adapter.listaComentarios.size
        val comentariosAtuais = adapter.listaComentarios
        for(comentarioNovo in denuncia.listaComentarios){
            val comentarioExistente = comentariosAtuais.find {
                it.id_comentario == comentarioNovo.id_comentario
            }
            if(comentarioExistente == null){
                comentariosAtuais.add(comentarioNovo)
            }
        }

        val tamanhoAtualizadoDaLista = comentariosAtuais.size
        if(tamanhoAtualDaLista != tamanhoAtualizadoDaLista){
            //quer dizer que houve mudança na lista
            if(tamanhoAtualizadoDaLista > tamanhoAtualDaLista){
                adapter.notifyItemRangeInserted(tamanhoAtualDaLista, tamanhoAtualizadoDaLista)


            }

        }

        adapter.notifyDataSetChanged()
        binding.recyclerComentarios.scrollToPosition(0)




    }

    private fun validarSeFoiPostado(foiPostado: Boolean) {
        if(foiPostado){
            showComentarioEnviadoToast()
            limparCaixaDeTexto()
            carregarDenunciaAtualizada(idDenuncia)
        }else{
            showPostCommentErrorToast()
        }

    }

    private fun carregarDenunciaAtualizada(idDenuncia: String) {
        viewModel.carregarDenunciaAtualizada(idDenuncia)

    }

    private fun showPostCommentErrorToast() {
        Toast.makeText(requireContext(), "houve um erro ao postar o comentário", Toast.LENGTH_SHORT).show()
    }

    private fun getArgs() {
        val args = arguments
        Log.d("testingargs", "$args")
        if(args != null){
            idDenuncia = (args.getSerializable("denuncia") as Denuncias).idDenuncia
            putDenunciaComentadaInViews(args.getSerializable("denuncia") as Denuncias)
            initComentariosAdapter(args.getSerializable("denuncia") as Denuncias)
        }
    }

    private fun initComentariosAdapter(denuncia: Denuncias) {
        binding.recyclerComentarios.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterComentarios(denuncia.listaComentarios, this)
        binding.recyclerComentarios.adapter = adapter

    }

    private fun putDenunciaComentadaInViews(denuncia: Denuncias) {
        binding.denunciaQueEstaSendoComentada.text = denuncia.denuncia
        binding.nomeOp.text = denuncia.nomeOp

    }

    private fun onClickListeners() {
        binding.backButton4.setOnClickListener {
            if (view?.findNavController()?.currentDestination?.id == R.id.comentariosFragment) {
                view?.findNavController()?.popBackStack()

            }
        }

        binding.btnEnviarComentario.setOnClickListener {
            if(binding.caixaComentarios.text.toString().isNotEmpty() && idDenuncia != ""){
                viewModel.enviarComentarioParaDenuncia(binding.caixaComentarios.text.toString(), idDenuncia)
                //passar isso para o observer da confirmacao do save do comentario no firebase
                showComentarioEnviadoToast()
                limparCaixaDeTexto()
            }else{
                showEmptyCommentToast()
            }
            hideKeyboardOnClick()

        }
    }

    private fun hideKeyboardOnClick() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if(view == null){
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun limparCaixaDeTexto() {
        binding.caixaComentarios.setText("")
        binding.caixaComentarios.hint = "Escreva um comentário"
    }

    private fun showComentarioEnviadoToast() {
        Toast.makeText(requireActivity(), "comentário enviado com sucesso!", Toast.LENGTH_SHORT).show()

    }

    private fun showEmptyCommentToast() {
        Toast.makeText(requireActivity(), "comentário vazio!", Toast.LENGTH_SHORT).show()
    }

}