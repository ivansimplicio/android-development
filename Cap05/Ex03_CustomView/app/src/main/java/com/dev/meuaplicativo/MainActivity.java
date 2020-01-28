package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements JogoDaVelhaView.JogoDaVelhaListener,
        View.OnClickListener{

    JogoDaVelhaView jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jogo = findViewById(R.id.jogoDaVelha);
        jogo.setListener(this);

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void fimDeJogo(int vencedor) {
        String mensagem = null;
        switch (vencedor){
            case JogoDaVelhaView.XIS:
                mensagem = "X venceu!";
                break;
            case JogoDaVelhaView.BOLA:
                mensagem = "O venceu!";
                break;
            case JogoDaVelhaView.EMPATE:
                mensagem = "Empatou!";
                break;
        }
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        jogo.reiniciarJogo();
    }
}