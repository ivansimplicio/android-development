package com.dev.meuaplicativo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SegundoNivelFragment extends Fragment {

    private static final String EXTRA_TEXTO = "texto";
    private static final String EXTRA_COR_BG = "corBg";
    private static final String EXTRA_COR_TEXTO = "corTexto";

    public static SegundoNivelFragment novaInstancia(String texto, int backgroundColor, int textColor){
        Bundle params = new Bundle();
        params.putString(EXTRA_TEXTO, texto);
        params.putInt(EXTRA_COR_BG, backgroundColor);
        params.putInt(EXTRA_COR_TEXTO, textColor);

        SegundoNivelFragment fragment = new SegundoNivelFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = getArguments();

        String texto = params.getString(EXTRA_TEXTO);
        int backgroundColor = params.getInt(EXTRA_COR_BG);
        int textColor = params.getInt(EXTRA_COR_TEXTO);

        View layout = inflater.inflate(R.layout.fragment_segundo_nivel, container, false);
        layout.setBackgroundColor(backgroundColor);

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(texto);
        textView.setTextColor(textColor);

        return layout;
    }
}