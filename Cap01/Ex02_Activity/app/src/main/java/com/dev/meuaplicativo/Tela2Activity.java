package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Tela2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Log::Teste", "Tela2::onCreate");
        setContentView(R.layout.activity_tela2);

        TextView textView = findViewById(R.id.textTexto);

        Intent it = getIntent();

        Cliente cliente = it.getParcelableExtra("cliente");
        Pessoa pessoa = (Pessoa) it.getSerializableExtra("pessoa");

        if(cliente != null){
            String text = String.format("Nome: %s\nCódigo: %d", cliente.nome, cliente.codigo);
            textView.setText(text);
        }else if(pessoa != null){
            String text = String.format("Nome: %s\nIdade: %d", pessoa.nome, pessoa.idade);
            textView.setText(text);
        }else{
            String nome = it.getStringExtra("nome");
            int idade = it.getIntExtra("idade", -1);
            textView.setText(String.format("Nome: %s\nIdade: %d", nome, idade));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Log::Teste", "Tela2::onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Log::Teste", "Tela2::onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Log::Teste", "Tela2::onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Log::Teste", "Tela2::onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Log::Teste", "Tela2::onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Log::Teste", "Tela2::onDestroy");
    }
}
