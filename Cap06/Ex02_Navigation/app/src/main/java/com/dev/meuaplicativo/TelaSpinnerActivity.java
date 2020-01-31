package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TelaSpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String OPCAO_ATUAL = "opcaoAtual";
    Toolbar toolbar;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                getResources().getStringArray(R.array.secoes));

        spinner = new Spinner(this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.addView(spinner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(OPCAO_ATUAL)){
            spinner.setSelection(savedInstanceState.getInt(OPCAO_ATUAL));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OPCAO_ATUAL, spinner.getSelectedItemPosition());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        exibirItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void exibirItem(int position){
        String[] titulosAbas = getResources().getStringArray(R.array.secoes);
        TypedArray backgroundColors = getResources().obtainTypedArray(R.array.cores_bg);
        TypedArray textColors = getResources().obtainTypedArray(R.array.cores_texto);

        SegundoNivelFragment fragment = SegundoNivelFragment.novaInstancia(titulosAbas[position],
                backgroundColors.getColor(position, 0), textColors.getColor(position, 0));


        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag("tag");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, "tag");
        if(f != null){
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        ft.commit();

        //getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}