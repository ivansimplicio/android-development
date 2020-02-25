package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageButton;

import java.util.List;

public class ListaActivity extends AppCompatActivity
        implements LivrosAdapter.AoClicarNoLivroListener {

    private RecyclerView recyclerView;
    private LivrosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Definindo as animações de entrada e saída da activity programaticamente
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this, 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new LivrosTask().execute();

        ImageButton imageButton = findViewById(R.id.buttonAnim);
        float elevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        ViewCompat.setElevation(imageButton, elevation);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirOcultar(view);
            }
        });
    }

    public void exibirOcultar(View view){
        boolean exibindo = recyclerView.getVisibility() == View.VISIBLE;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int cx = view.getLeft() + (view.getWidth() / 2);
            int cy = view.getTop() + (view.getHeight() / 2);
            int raio = Math.max(recyclerView.getWidth(), recyclerView.getHeight());

            if(exibindo){
                Animator anim = ViewAnimationUtils.createCircularReveal(
                        recyclerView, cx, cy, raio, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }else{
                Animator anim = ViewAnimationUtils.createCircularReveal(
                        recyclerView, cx, cy, 0, raio);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
                anim.start();
            }
        }else{
            recyclerView.setVisibility(exibindo ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public void aoClicarNoLivro(View view, int position, Livro livro) {
        Intent intent = new Intent(this, DetalheActivity.class);
        intent.putExtra(DetalheActivity.EXTRA_LIVRO, livro);

        View buttonAnim = findViewById(R.id.buttonAnim);
        ViewCompat.setTransitionName(buttonAnim, "button");

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create(view.findViewById(R.id.imagemCapa), "capa"),
                Pair.create(view.findViewById(R.id.textViewTitulo), "titulo"),
                Pair.create(view.findViewById(R.id.textViewAutores), "autores"),
                Pair.create(view.findViewById(R.id.textViewAno), "ano"),
                Pair.create(view.findViewById(R.id.textViewPaginas), "paginas"),
                Pair.create(buttonAnim, "button"));
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    class LivrosTask extends AsyncTask<Void, Void, List<Livro>>{

        @Override
        protected List<Livro> doInBackground(Void... voids) {
            return LivroHttp.carregarLivrosJson();
        }

        @Override
        protected void onPostExecute(List<Livro> livros) {
            super.onPostExecute(livros);
            if(livros != null){
                adapter = new LivrosAdapter(ListaActivity.this, livros);
                adapter.setAoClicarNoLivroListener(ListaActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}