package com.dev.meuaplicativo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class LivrosGridAdapter extends ArrayAdapter<Livro> {

    private ImageLoader imageLoader;

    public LivrosGridAdapter(Context context, List<Livro> livros){
        super(context, 0, livros);
        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_livro_grid, null);
        }
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.imagemCapa);
        TextView textView = convertView.findViewById(R.id.textViewTitulo);

        Livro livro = getItem(position);
        textView.setText(livro.titulo);
        image.setImageUrl(livro.capa, imageLoader);

        return convertView;
    }
}