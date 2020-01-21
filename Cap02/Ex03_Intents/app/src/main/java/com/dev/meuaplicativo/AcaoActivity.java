package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class AcaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acao);

        TextView textView = findViewById(R.id.textView);

        Intent it = getIntent();

        if(it.getAction().equals(Intent.ACTION_VIEW)){
            Uri uri = it.getData();
            textView.setText(   "Ação customizada 2."+
                                "\nUri: "+uri.toString()+
                                "\nHost: "+uri.getHost()+
                                "\nPath: "+uri.getPath());
        }else if(it.getAction().equals("com.dev.meuaplicativo.ACAO_CUSTOMIZADA")){
            textView.setText("Ação customizada 1.");
        }
    }
}