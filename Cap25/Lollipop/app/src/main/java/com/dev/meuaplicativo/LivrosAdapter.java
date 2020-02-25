package com.dev.meuaplicativo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LivrosAdapter extends RecyclerView.Adapter<LivrosAdapter.LivrosViewHolder> {

    private Context context;
    private List<Livro> livros;
    private AoClicarNoLivroListener listener;

    public LivrosAdapter(Context context, List<Livro> livros){
        this.context = context;
        this.livros = livros;
    }

    public void setAoClicarNoLivroListener(AoClicarNoLivroListener listener){
        this.listener = listener;
    }

    @Override
    public LivrosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_livro, parent, false);
        LivrosViewHolder vh = new LivrosViewHolder(view);
        view.setTag(vh);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(listener != null){
                    LivrosViewHolder vh = (LivrosViewHolder)view.getTag();
                    int position = vh.getPosition();
                    listener.aoClicarNoLivro(view, position, livros.get(position));
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(LivrosViewHolder holder, int position) {
        Livro livro = livros.get(position);

        Picasso.get().load(livro.capa).into(holder.imageCapa);
        holder.textViewTitulo.setText(livro.titulo);
        holder.textViewAutores.setText(livro.autor);
        holder.textViewAno.setText(String.valueOf(livro.ano));
        holder.textViewPaginas.setText(String.valueOf(livro.paginas));
    }

    @Override
    public int getItemCount() {
        return livros != null ? livros.size() : 0;
    }

    public interface AoClicarNoLivroListener{
        void aoClicarNoLivro(View view, int position, Livro livro);
    }

    public static class LivrosViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageCapa;
        public TextView textViewTitulo;
        public TextView textViewAutores;
        public TextView textViewPaginas;
        public TextView textViewAno;

        public LivrosViewHolder(View parent){
            super(parent);
            imageCapa = parent.findViewById(R.id.imagemCapa);
            textViewTitulo = parent.findViewById(R.id.textViewTitulo);
            textViewAutores = parent.findViewById(R.id.textViewAutores);
            textViewPaginas = parent.findViewById(R.id.textViewPaginas);
            textViewAno = parent.findViewById(R.id.textViewAno);

            ViewCompat.setTransitionName(imageCapa, "capa");
            ViewCompat.setTransitionName(textViewTitulo, "titulo");
            ViewCompat.setTransitionName(textViewAutores, "autores");
            ViewCompat.setTransitionName(textViewPaginas, "paginas");
            ViewCompat.setTransitionName(textViewAno, "ano");
        }
    }
}