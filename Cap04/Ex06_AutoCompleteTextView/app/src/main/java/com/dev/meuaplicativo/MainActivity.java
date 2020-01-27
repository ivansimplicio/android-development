package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> cidades = new ArrayList<>();
        cidades.add("Caruaru");
        cidades.add("Cabo de Santo Agostinho");
        cidades.add("Recife");
        cidades.add("São Paulo");
        cidades.add("Santos");
        cidades.add("Santa Cruz");

        MeuAutoCompleteAdapter adapter = new MeuAutoCompleteAdapter(this,
                android.R.layout.simple_dropdown_item_1line, cidades);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
    }
}