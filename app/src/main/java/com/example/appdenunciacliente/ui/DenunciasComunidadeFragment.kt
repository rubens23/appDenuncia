package com.example.appdenunciacliente.ui

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.framework.data.models.Denuncias
import com.example.appdenunciacliente.databinding.FragmentDenunciasComunidadeBinding
import com.example.appdenunciacliente.framework.viewModels.DenunciasComunidadeFragmentViewModel
import com.example.appdenunciacliente.ui.adapters.utils.callbackinterfaces.CallbackInterface
import com.example.appdenunciacliente.ui.adapters.ReclamacoesAdapterKotlin
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicao
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComPosicaoEImagem
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.DenunciaComQuantidadeDeLikesAtualizada
import com.example.appdenunciacliente.ui.adapters.utils.dataclasses.GostouDaDenuncia
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest


class DenunciasComunidadeFragment : Fragment(), CallbackInterface {


    private lateinit var binding: FragmentDenunciasComunidadeBinding
    private lateinit var viewModel: DenunciasComunidadeFragmentViewModel
    private lateinit var minhasDenunciasAdapter: ReclamacoesAdapterKotlin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDenunciasComunidadeBinding.inflate(inflater)
        configFonts()
        showLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        getTodasDenuncias()
        onClickListeners()
    }

    private fun initViewModel() {



        viewModel = ViewModelProvider(requireActivity())[DenunciasComunidadeFragmentViewModel::class.java]
        initCollectors()



    }


    private fun initCollectors() {


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passouImagemParaFireabse.collectLatest { denunciaComPosicaoEImagem ->
                salvarLinkDaImagemNoFirebaseDatabase(denunciaComPosicaoEImagem)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getDenunciasResult.collectLatest { result ->
                mandarReclamacoesParaAdapter(result)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.atualizacaoDenuncia.collectLatest { denunciaComPosicao ->
                atualizarListaEAtualizarItemNoRecyclerView(denunciaComPosicao)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.usuarioGostouDaReclamacao.collectLatest { gostou ->
                lidarComCliqueNoLike(gostou)

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.qntDeLikesDepoisDeColocar.collectLatest { qntLikes ->
                colocarLike(qntLikes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.qntLikesDepoisDeTirar.collectLatest { qntLikes ->
                tirarLike(qntLikes)
            }
        }




        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.atualizarLikesDepoisDeTirarLike.collectLatest { qntLikes ->
                atualizarLikesNaRecyclerView(qntLikes)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.atualizarLikesDepoisDeColocarLike.collectLatest { qntLikes ->
                atualizarLikesNaRecyclerView(qntLikes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passouImagemParaDenuncia.collectLatest {
                    confirmacaoPassouImagemParaDenuncia->
                pegarDenunciaComImagemAtualizada(confirmacaoPassouImagemParaDenuncia)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.atualizacaoDenunciaComImagemEPosicao.collectLatest {
                    denunciaComImagemEPosicao->
                atualizarRecyclerViewComDenunciaComImagemEPosicao(denunciaComImagemEPosicao)

            }
        }




    }


    private fun atualizarRecyclerViewComDenunciaComImagemEPosicao(denunciaComImagemEPosicao: DenunciaComPosicao) {
        minhasDenunciasAdapter.lista[denunciaComImagemEPosicao.recyclerPosition] =
            denunciaComImagemEPosicao.denuncia
        hideLoading()
        minhasDenunciasAdapter.notifyItemChanged(denunciaComImagemEPosicao.recyclerPosition)
    }

    private fun pegarDenunciaComImagemAtualizada(confirmacaoPassouImagemParaDenuncia: DenunciaComPosicaoEImagem) {
        viewModel.pegarDenunciaComImagemAtualizada(confirmacaoPassouImagemParaDenuncia)
    }

    private fun salvarLinkDaImagemNoFirebaseDatabase(denunciaComPosicaoEImagem: DenunciaComPosicaoEImagem) {
        if(denunciaComPosicaoEImagem.linkImagemRecemSalva != "erro ao salvar"){
            viewModel.salvarImagemNaDenuncia(denunciaComPosicaoEImagem)
        }

    }

    private fun atualizarListaEAtualizarItemNoRecyclerView(denunciaComPosicao: DenunciaComPosicao) {
        minhasDenunciasAdapter.lista[denunciaComPosicao.recyclerPosition] =
            denunciaComPosicao.denuncia
        minhasDenunciasAdapter.notifyItemChanged(denunciaComPosicao.recyclerPosition)

    }


    private fun atualizarLikesNaRecyclerView(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada) {
        pegarDenunciaAtualizada(denunciaComLikesAtualizados)

    }

    private fun pegarDenunciaAtualizada(denunciaComLikesAtualizados: DenunciaComQuantidadeDeLikesAtualizada) {
        viewModel.pegarDenunciaAtualizada(denunciaComLikesAtualizados)
    }

    private fun tirarLike(denunciaComQuantidadeAtualizada: DenunciaComQuantidadeDeLikesAtualizada) {

        val novaQntDeLikes = denunciaComQuantidadeAtualizada.qntLikes - 1
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModel.tirarLike(
                novaQntDeLikes,
                denunciaComQuantidadeAtualizada.idDenuncia,
                FirebaseAuth.getInstance().currentUser!!.uid,
                denunciaComQuantidadeAtualizada.gostouDaDenuncia
            )


        }


    }

    private fun colocarLike(denunciaComQuantidadeAtualizada: DenunciaComQuantidadeDeLikesAtualizada) {
        val novaQntDeLikes = denunciaComQuantidadeAtualizada.qntLikes + 1
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModel.darLike(
                novaQntDeLikes,
                denunciaComQuantidadeAtualizada.idDenuncia,
                FirebaseAuth.getInstance().currentUser!!.uid,
                denunciaComQuantidadeAtualizada.gostouDaDenuncia
            )


        }
    }

    private fun lidarComCliqueNoLike(gostou: GostouDaDenuncia) {
        if (gostou.gostou) {

            if (FirebaseAuth.getInstance().currentUser != null) {
                viewModel.getQuantidadeDeLikesParaTirarLike(
                    gostou.idDenuncia,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    gostou
                )

            }

        } else {
            if (FirebaseAuth.getInstance().currentUser != null) {
                viewModel.getQuantidadeDeLikesParaColocarLike(
                    gostou.idDenuncia,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    gostou
                )

            }

        }


    }

    private fun getTodasDenuncias() {
        viewModel.getTodasDenuncias()
    }

    private fun onClickListeners() {
        binding.backButton.setOnClickListener {
            if (view?.findNavController()?.currentDestination?.id == R.id.denunciasComunidadeFragment) {
                view?.findNavController()?.popBackStack()

            }
        }
    }

    private fun configFonts() {
        val tfLight = Typeface.createFromAsset(requireActivity().assets, "fonts/Lato-Light.ttf")
        binding.labelMinhasReclamacoes.typeface = tfLight
    }

    private fun mandarReclamacoesParaAdapter(lista: MutableList<Denuncias>) {
        initAdapter()


        hideLoading()
        minhasDenunciasAdapter.setItems(lista)

    }

    private fun initAdapter() {

        val lm = LinearLayoutManager(requireContext())
        binding.recyclerMinhasReclamacoes.layoutManager = lm
        if (FirebaseAuth.getInstance().currentUser != null) {
            minhasDenunciasAdapter =
                ReclamacoesAdapterKotlin(this, FirebaseAuth.getInstance(), false)
            binding.recyclerMinhasReclamacoes.adapter = minhasDenunciasAdapter
        }

    }


    override fun clickBtnComentario(denuncia: Denuncias) {
        val action = DenunciasComunidadeFragmentDirections.actionDenunciasComunidadeFragmentToComentariosFragment(denuncia)
        findNavController().navigate(action)

    }





    private fun showLoading(){
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.loading.visibility = View.GONE
    }




    override fun verSeUsuarioJaGostaDessaDenuncia(denuncia: Denuncias, position: Int) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModel.verSeUsuarioGostouDaReclamacao(
                denuncia.idDenuncia,
                position,
                FirebaseAuth.getInstance().currentUser!!.uid
            )

        }
    }






}
