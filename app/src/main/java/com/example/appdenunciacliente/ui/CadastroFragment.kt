package com.example.appdenunciacliente.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.databinding.FragmentCadastroBinding
import com.example.appdenunciacliente.framework.viewModels.CadastroFragmentViewModel
import kotlinx.coroutines.flow.collectLatest


class CadastroFragment : Fragment() {

    private lateinit var binding: FragmentCadastroBinding
    private lateinit var viewModel: CadastroFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCadastroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        onClickListeners()


    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[CadastroFragmentViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.resultadoCadastro.collectLatest {
                resultadoCadastro->
                validarCadastro(resultadoCadastro)
            }

        }
    }

    private fun validarCadastro(resultadoCadastro: String) {
        when(resultadoCadastro){
            "preencha todos os campos"->{showToastDeValidacaoDeCadastro(resultadoCadastro)}
            "a senha tem que ter pelo menos 6 caracteres!"->{showToastDeValidacaoDeCadastro(resultadoCadastro)}
            "cadastrado com sucesso"->{showToastDeValidacaoDeCadastro(resultadoCadastro)
            closeCadastroFragment()}
            "falha no cadastro"->{showToastDeValidacaoDeCadastro(resultadoCadastro)}
        }

    }

    private fun closeCadastroFragment() {
        if (view?.findNavController()?.currentDestination?.id == R.id.cadastroFragment) {
            view?.findNavController()?.popBackStack()

        }
    }

    private fun showToastDeValidacaoDeCadastro(resultadoCadastro: String) {
        Toast.makeText(requireActivity(), resultadoCadastro, Toast.LENGTH_LONG).show()

    }

    private fun onClickListeners() {
        binding.btnCadastro.setOnClickListener {
            cadastrar()

        }
    }

    private fun cadastrar() {
        val email = binding.emailCadastro.text.toString()
        val senha = binding.senhaCadastro.text.toString()
        val primeiroNome = binding.priemiroNomeCadastro.text.toString()
        val sobrenome = binding.sobrenomeCadastro.text.toString()
        val cidade = binding.cidadeCadastro.text.toString()
        viewModel.cadastrarNovoUsuario(email, senha, primeiroNome, sobrenome, cidade)
    }
}