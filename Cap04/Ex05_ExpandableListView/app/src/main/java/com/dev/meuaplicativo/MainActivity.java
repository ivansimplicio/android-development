package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        List<String> listPe = new ArrayList<>();
        listPe.add("Caruaru");
        listPe.add("Recife");
        List<String> listSp = new ArrayList<>();
        listSp.add("São Paulo");
        listSp.add("Campinas");

        Map<String, List<String>> dados = new HashMap<>();
        dados.put("PE", listPe);
        dados.put("SP", listSp);

        expandableListView.setAdapter(new MeuExpandableAdapter(dados));
    }
}