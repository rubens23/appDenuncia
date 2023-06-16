package com.example.appdenunciacliente.ui

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureFonts()
        onClickListeners()
    }

    private fun configureFonts() {
        val tf: Typeface = Typeface.createFromAsset(requireActivity().assets, "fonts/Lato-Hairline.ttf")
        binding.lbTitleAdicionarReclamacoes.setTypeface(tf, Typeface.BOLD)
        binding.lbTitleMinhasReclamacoes.setTypeface(tf, Typeface.BOLD)
        binding.lbTitleReclamacoesComunidades.setTypeface(tf, Typeface.BOLD)
    }

    private fun onClickListeners(){
        binding.btnNovasReclamacoes.setOnClickListener{
            if(view?.findNavController()?.currentDestination?.id == R.id.menuFragment){
                view?.findNavController()?.navigate(MenuFragmentDirections.actionMenuFragmentToAdicionarDenunciaFragment())
            }
        }
        binding.btnMinhasReclamacoes.setOnClickListener {
            if(view?.findNavController()?.currentDestination?.id == R.id.menuFragment){
                view?.findNavController()?.navigate(MenuFragmentDirections.actionMenuFragmentToMinhasDenunciasFragment())
            }
        }
        binding.btnReclamacoesComunidade.setOnClickListener {
            if(view?.findNavController()?.currentDestination?.id == R.id.menuFragment){
                view?.findNavController()?.navigate(MenuFragmentDirections.actionMenuFragmentToDenunciasComunidadeFragment())
            }
        }
    }



}