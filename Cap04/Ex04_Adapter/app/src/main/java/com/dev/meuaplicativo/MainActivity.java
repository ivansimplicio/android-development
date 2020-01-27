package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Carro> carros;
    CarrosAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        //0=VW, 1=GM, 2=Fiat, 3=Ford
        carros = new ArrayList<Carro>();
        carros.add(new Carro("Celta", 2010, 1, true, false));
        carros.add(new Carro("Uno", 2012, 2, true, true));
        carros.add(new Carro("Fiesta", 2009, 3, false, true));
        carros.add(new Carro("Gol", 2014, 0, true, true));

        adapter = new CarrosAdapter(this, carros);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carro carro = (Carro)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, carro.modelo+"-"+carro.ano,
                        Toast.LENGTH_SHORT).show();
            }
        });

        final int PADDING = 8;
        TextView textViewHeader = new TextView(this);
        textViewHeader.setBackgroundColor(Color.GRAY);
        textViewHeader.setTextColor(Color.WHITE);
        textViewHeader.setText(R.string.texto_cabecalho);
        textViewHeader.setPadding(PADDING, PADDING,0, PADDING);
        listView.addHeaderView(textViewHeader);

        TextView textViewFooter = new TextView(this);
        textViewFooter.setText(getResources().getQuantityString(R.plurals.texto_rodape,
                adapter.getCount(), adapter.getCount()));
        textViewFooter.setBackgroundColor(Color.LTGRAY);
        textViewFooter.setGravity(Gravity.RIGHT);
        textViewFooter.setPadding(0, PADDING, PADDING, PADDING);
        listView.addFooterView(textViewFooter);

        //EmptyView só funciona com ListActivity...
        listView.setEmptyView(findViewById(android.R.id.empty));
    }
}