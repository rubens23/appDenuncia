package com.example.appdenunciacliente.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.databinding.FragmentAdicionarDenunciaBinding
import com.example.appdenunciacliente.databinding.FragmentEsqueceuSenhaBinding
import com.example.appdenunciacliente.framework.viewModels.EsqueceuSenhaFragmentViewModel
import kotlinx.coroutines.flow.collectLatest


class EsqueceuSenhaFragment : Fragment() {

    private lateinit var binding: FragmentEsqueceuSenhaBinding
    private lateinit var viewModel: EsqueceuSenhaFragmentViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEsqueceuSenhaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        onClickListeners()

    }

    private fun onClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        if(!binding.emailReset.text.toString().isBlank() ||
            binding.emailReset.text.toString().isEmpty() ||
            binding.emailReset.text.toString().equals(null)){
            val email = binding.emailReset.text.toString()
            viewModel.sendEmailToResetPassword(email)
        }else{
            showToast("Digite um e-mail válido!")
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[EsqueceuSenhaFragmentViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.enviouEmail.collectLatest {
                enviouEmail->
                lidarComResultadoDeEnvioDeEmailDeRedefinicaoDeSenha(enviouEmail)
            }
        }
    }

    private fun lidarComResultadoDeEnvioDeEmailDeRedefinicaoDeSenha(enviouEmail: Boolean) {
        if(enviouEmail){
            showToast("Um e-mail foi enviado para você mudar a sua senha!")
            voltarParaFragmentDeLogin()
        }else{
            showToast("O e-mail que você digitou não foi encontrado em nossa base de dados!")

        }
    }

    private fun voltarParaFragmentDeLogin() {
        if (view?.findNavController()?.currentDestination?.id == R.id.esqueceuSenhaFragment) {
            view?.findNavController()?.popBackStack()

        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }
}