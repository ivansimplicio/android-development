package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetalheActivity extends AppCompatActivity {

    public static final String EXTRA_LIVRO = "livro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        ImageView imageCapa = findViewById(R.id.imagemCapa);
        TextView textViewTitulo = findViewById(R.id.textViewTitulo);
        TextView textViewAutores = findViewById(R.id.textViewAutores);
        TextView textViewAno = findViewById(R.id.textViewAno);
        TextView textViewPaginas = findViewById(R.id.textViewPaginas);
        ImageButton buttonAnim = findViewById(R.id.buttonAnim);

        ViewCompat.setTransitionName(imageCapa, "capa");
        ViewCompat.setTransitionName(textViewTitulo, "titulo");
        ViewCompat.setTransitionName(textViewAutores, "autores");
        ViewCompat.setTransitionName(textViewPaginas, "paginas");
        ViewCompat.setTransitionName(textViewAno, "ano");
        ViewCompat.setTransitionName(buttonAnim, "button");

        Livro livro = (Livro)getIntent().getSerializableExtra(EXTRA_LIVRO);

        Picasso.get().load(livro.capa).into(imageCapa);
        textViewTitulo.setText(livro.titulo);
        textViewAutores.setText(livro.autor);
        textViewPaginas.setText(String.valueOf(livro.paginas));
        textViewAno.setText(String.valueOf(livro.ano));

        buttonAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(DetalheActivity.this);
            }
        });
    }
}
