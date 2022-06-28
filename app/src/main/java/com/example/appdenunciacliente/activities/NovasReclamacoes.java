package com.example.appdenunciacliente.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdenunciacliente.R;
import com.example.appdenunciacliente.database.BancoController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NovasReclamacoes extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText outroTipo, caixa_reclamacao;
    private String spinnerResult;
    private Spinner spinner;
    private Button btn_enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novas_reclamacoes);

        initClassMembers();

        setSpinnerSettings();

        apertarBotaoEnviarReclamacao();
    }

    private void initClassMembers() {
        spinner = findViewById(R.id.spinner_tipos_reclamacao);
        caixa_reclamacao = findViewById(R.id.reclamacao_cliente);
        btn_enviar = findViewById(R.id.btn_enviar_reclamacao);
        outroTipo = findViewById(R.id.edit_text_outro);
    }

    private void setSpinnerSettings() {
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.tiposReclamacao, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(this);
    }


    private void apertarBotaoEnviarReclamacao() {
        btn_enviar.setOnClickListener(v->{
            enviarReclamacao();

        });
    }


    private void enviarReclamacao() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = null;
        if(user != null){
            uid = user.getUid();
        }else{
            Toast.makeText(this, "houve um problema com a autenticação da sua conta, entre em contato com o suporte", Toast.LENGTH_SHORT).show();
        }
        String reclamacao = caixa_reclamacao.getText().toString();
        String tipoCustom = outroTipo.getText().toString();//o problema é com a captaçao da edit text
        if(spinnerResult.isEmpty()){
            spinnerResult = tipoCustom;
        }
        String resultado;
        if(!reclamacao.isEmpty()){
            BancoController bd = new BancoController(getBaseContext());
            resultado = bd.inserirDadosReclamacoes(uid, reclamacao, "pendente", spinnerResult);
            spinnerResult = "";
            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView)parent.getChildAt(0)).setTextSize(20);
        parent.getItemAtPosition(pos);
        String item = (String)parent.getItemAtPosition(pos);
        if(!item.equals("outro")){
            outroTipo.setVisibility(View.GONE);
            spinnerResult = item;
        }
        if(item.equals("outro")){
            outroTipo.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}