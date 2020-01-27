package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CompoundButton checkBox;
    SeekBar seekBar;
    Spinner spinner;
    RadioGroup radioGroup;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.checkBoxHabilitar);
        seekBar = findViewById(R.id.seekBarValor);
        spinner = findViewById(R.id.spinnerNomes);
        radioGroup = findViewById(R.id.radioGroupOpcoes);
        textView = findViewById(R.id.textViewValor);

        configuraSpinner();
        configuraSeekBar();

        //Atribuindo programaticamente os valores-padrão
        checkBox.setChecked(true);
        seekBar.setProgress(20);
        spinner.setSelection(2);
        radioGroup.check(R.id.radioButton2);
    }

    private void configuraSeekBar(){
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        textView.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
    }

    private void configuraSpinner(){
        String[] nomes = new String[]{"Eric", "Diana", "Presto", "Hank", "Sheila", "Bob"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, nomes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void verValores(View v){
        int idRadioSelecionado = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(idRadioSelecionado);

        String habilitado = checkBox.isChecked() ? "Habilitado" : "Desabilitado";
        String valor = "valor: "+seekBar.getProgress();
        String nome = "nome: "+spinner.getSelectedItem().toString();
        String opcao = "opção: "+radio.getText();

        StringBuilder mensagem = new StringBuilder();
        mensagem.append(habilitado).append("\n")
                .append(valor).append("\n")
                .append(nome).append("\n")
                .append(opcao);

        Toast.makeText(this, mensagem.toString(), Toast.LENGTH_SHORT).show();
    }
}