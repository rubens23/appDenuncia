package com.example.appdenunciacliente.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.appdenunciacliente.R
import com.example.appdenunciacliente.databinding.FragmentAdicionarDenunciaBinding
import com.example.appdenunciacliente.framework.viewModels.AdicionarDenunciaFragmentViewModel
import kotlinx.coroutines.flow.collectLatest

class AdicionarDenunciaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAdicionarDenunciaBinding
    private var spinnerResult = ""
    private lateinit var viewModel: AdicionarDenunciaFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAdicionarDenunciaBinding.inflate(inflater)
        configFonts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        setSpinnersSettings()
        onClickListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[AdicionarDenunciaFragmentViewModel::class.java]
        initFlowCollectors()

    }

    private fun initFlowCollectors() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.envioResult.collectLatest {
                result->
                if(result == "Denúncia salva com sucesso"){
                    Toast.makeText(requireActivity(), "Denúncia salva com sucesso!", Toast.LENGTH_SHORT).show()
                    fecharFragmentoAdicionarDenuncia()
                }else{
                    Toast.makeText(requireActivity(), "Erro ao salvar denúncia", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onClickListeners() {
        binding.btnEnviarReclamacao.setOnClickListener {
            enviarReclamacao()
        }
        binding.backButton2.setOnClickListener {
            fecharFragmentoAdicionarDenuncia()

        }
    }

    private fun fecharFragmentoAdicionarDenuncia() {
        if(view?.findNavController()?.currentDestination?.id == R.id.adicionarDenunciaFragment){
            view?.findNavController()?.popBackStack()

        }
    }

    private fun enviarReclamacao() {
        val reclamacao: String = binding.reclamacaoCliente.text.toString()
        val tipoCustom: String =
            binding.editTextOutro.text.toString() //o problema é com a captaçao da edit text

        if(tipoCustom.isNotEmpty()){
            spinnerResult = tipoCustom
        }

        if (spinnerResult.isEmpty()) {
            spinnerResult = tipoCustom
        }
        if (reclamacao.isNotEmpty()) {
            viewModel.enviarReclamacao(reclamacao, spinnerResult)

        }else{
            Toast.makeText(requireActivity(), "A denúncia não pode ser vazia!", Toast.LENGTH_SHORT).show()

        }


    }

    private fun setSpinnersSettings() {
        val stringArray = arrayOf("Educação", "Saneamento", "Iluminação", "Mobilidade", "Saúde", "Transporte", "outro")

        val adapterSpinner = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text_color, stringArray
        )
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_layout) //simple_spinner_dropdown_item


        binding.spinnerTiposReclamacao.adapter = adapterSpinner


        binding.spinnerTiposReclamacao.onItemSelectedListener = this@AdicionarDenunciaFragment


    }

    private fun configFonts() {
        val tfLight = Typeface.createFromAsset(requireActivity().assets, "fonts/Lato-Light.ttf")
        binding.labelTipoReclamacao.setTypeface(tfLight, Typeface.BOLD)
        binding.labelTipoReclamacao.typeface = tfLight
        binding.titleActivityNovaReclamacao.typeface = tfLight
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        parent!!.getItemAtPosition(pos)
        val item = parent.getItemAtPosition(pos) as String
        if (item != "outro") {
            binding.editTextOutro.visibility = View.GONE
            spinnerResult = item
        }
        if (item == "outro") {
            Log.d("outro", "cliquei no outro")
            binding.editTextOutro.setText("")
            binding.spinnerTiposReclamacao.visibility = View.GONE
            binding.editTextOutro.visibility = View.VISIBLE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}