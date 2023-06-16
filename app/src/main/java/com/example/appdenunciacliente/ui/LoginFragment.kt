package com.example.appdenunciacliente.ui

import android.content.Intent
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
import com.example.appdenunciacliente.databinding.FragmentLoginBinding
import com.example.appdenunciacliente.framework.viewModels.LoginFragmentViewModel
import kotlinx.coroutines.flow.collectLatest



class LoginFragment : Fragment() {

    //todo use um flow quando voce nao quer manter o estado

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginFragmentViewModel
    private var loginErrorToastShown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        onClickListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[LoginFragmentViewModel::class.java]
        //initViewModelLiveDatas()
        //initObservers()
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginResult.collectLatest{
                    result->

                if (result == "Login bem-sucedido!"){
                    Log.d("bugnavigation", "eu to na: ${view?.findNavController()?.currentDestination}")
                    if(view?.findNavController()?.currentDestination?.id == R.id.loginFragment){
                        view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToMenuFragment2())
                    }
                }else{

                    Toast.makeText(requireActivity(), "Falha no login", Toast.LENGTH_SHORT).show()






                }

            }
        }


    }





    private fun onClickListeners() {
        binding.btnFazerConta.setOnClickListener{
            if(view?.findNavController()?.currentDestination?.id == R.id.loginFragment){
                view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToCadastroFragment())
            }
        }
        binding.btnLogin.setOnClickListener {
            viewModel.fazerLogin2(binding.email.text.toString(), binding.senha.text.toString())
        }
        binding.btnEsqueciSenha.setOnClickListener {
            if(view?.findNavController()?.currentDestination?.id == R.id.loginFragment){
                view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToEsqueceuSenhaFragment())
            }

        }
    }


}