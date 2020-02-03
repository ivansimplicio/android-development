package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    TextView textView;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        radioGroup = findViewById(R.id.radioGroup);

        findViewById(R.id.buttonSalvar).setOnClickListener(this);
        findViewById(R.id.buttonLer).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int idRadioButton = radioGroup.getCheckedRadioButtonId();

        if(view.getId() == R.id.buttonLer){
            switch (idRadioButton){
                case R.id.memInterna:
                    carregarInterno();
                    break;
                case R.id.memExternaPrivada:
                    carregarDoSdCard(true);
                    break;
                case R.id.memExternaPublica:
                    carregarDoSdCard(false);
                    break;
            }
        }else{
            switch (idRadioButton){
                case R.id.memInterna:
                    salvarInterno();
                    break;
                case R.id.memExternaPrivada:
                    salvarNoSdCard(true);
                    break;
                case R.id.memExternaPublica:
                    salvarNoSdCard(false);
                    break;
            }
        }
    }

    private void salvarInterno(){
        try{
            FileOutputStream fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE);
            salvar(fos);
        }catch (IOException e){
            Log.e("Log::Teste", "Erro ao salvar o arquivo", e);
        }
    }

    private void carregarInterno(){
        try{
            FileInputStream fis = openFileInput("arquivo.txt");
            carregar(fis);
        }catch (IOException e) {
            Log.e("Log::Teste", "Erro ao carregar arquivo", e);
        }
    }

    private void salvarNoSdCard(boolean privado){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File meuDir = getExternalDir(privado);
            try{
                if(!meuDir.exists()){
                    meuDir.mkdir();
                }
                File arquivoTxt = new File(meuDir, "arquivo.txt");
                if(!arquivoTxt.exists()){
                    arquivoTxt.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(arquivoTxt);
                salvar(fos);
            }catch (IOException e){
                Log.e("Log::Teste", "Erro ao salvar arquivo!", e);
        }
        }else{
            Log.e("Log::Teste", "Não é possível escrever no SD Card");
        }
    }

    private void carregarDoSdCard(boolean privado){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            File meuDir = getExternalDir(privado);
            if(meuDir.exists()){
                File arquivoTxt = new File(meuDir, "arquivo.txt");
                if(arquivoTxt.exists()){
                    try{
                        arquivoTxt.createNewFile();
                        FileInputStream fis = new FileInputStream(arquivoTxt);
                        carregar(fis);
                    }catch (IOException e){
                        Log.e("Log::Teste", "Erro ao carregar arquivo!", e);
                    }
                }
            }
        }else{
            Log.e("Log::Teste", "SD Card Indisponível");
        }
    }

    private File getExternalDir(boolean privado){
        if(privado){
            // SDCard/Android/data/pacote.da.app/files
            return getExternalFilesDir(null);
        }else{
            // SDCard/DCIM
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        }
    }

    private void salvar(FileOutputStream fos) throws IOException{
        String[] linhas = TextUtils.split(editText.getText().toString(), "\n");
        PrintWriter writer = new PrintWriter(fos);
        for(String linha : linhas){
            writer.println(linha);
        }
        writer.flush();
        writer.close();
        fos.close();
    }

    private void carregar(FileInputStream fis) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder string = new StringBuilder();
        String linha;
        while((linha = reader.readLine()) != null){
            if(string.length() != 0){
                string.append('\n');
            }
            string.append(linha);
        }
        reader.close();
        fis.close();

        textView.setText(string.toString());
    }

    /*private void salvarDadosUsandoSharedPreferences(){
        //Obtendo a instância do SharedPreferences
        SharedPreferences prefs = getSharedPreferences("configuracoes", MODE_PRIVATE);
        //Salvando dois valores nas preferências
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("musica", "Californication");
        editor.putBoolean("som", false);
        editor.commit();
        //Recuperando os valores
        String musica = prefs.getString("musica", null);
        boolean som = prefs.getBoolean("som", false);
    }*/
}