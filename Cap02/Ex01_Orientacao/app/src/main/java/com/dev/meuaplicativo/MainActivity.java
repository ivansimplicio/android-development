package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ArrayList<String> nomes;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            nomes = savedInstanceState.getStringArrayList("nomes");
        }else{
            nomes = new ArrayList<>();
        }
        editText = findViewById(R.id.editText);

        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes);
        listView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("nomes", nomes);
    }

    public void meuBotaoClick(View view){
        nomes.add(editText.getText().toString());
        editText.setText("");
        adapter.notifyDataSetChanged();
    }
}