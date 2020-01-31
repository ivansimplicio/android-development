package com.dev.meuaplicativo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.LinkedHashMap;

public class PrimeiroNivelFragment extends Fragment implements View.OnClickListener {

    private static final String EXTRA_TIPO = "tipo";
    private String tipo;
    private LinkedHashMap<String, Class> acoes;

    public static PrimeiroNivelFragment novaInstancia(String tipo){
        Bundle params = new Bundle();
        params.putString(EXTRA_TIPO, tipo);

        PrimeiroNivelFragment f = new PrimeiroNivelFragment();
        f.setArguments(params);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acoes = new LinkedHashMap<>();

        String[] opcoes = getResources().getStringArray(R.array.opcoes);
        acoes.put(opcoes[0], TelaAbasActivity.class);
        acoes.put(opcoes[1], TelaSpinnerActivity.class);
        acoes.put(opcoes[2], TelaPagerActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tipo = getArguments().getString(EXTRA_TIPO);
        View layout = inflater.inflate(R.layout.fragment_primeiro_nivel, container, false);

        Button button = layout.findViewById(R.id.button);
        button.setOnClickListener(this);

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(tipo);
        return layout;
    }

    @Override
    public void onClick(View v) {
        Class classe = acoes.get(tipo);
        startActivity(new Intent(getActivity(), classe));
    }
}