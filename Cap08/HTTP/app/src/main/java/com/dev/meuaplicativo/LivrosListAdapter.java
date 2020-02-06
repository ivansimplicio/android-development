package com.dev.meuaplicativo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LivrosListAdapter extends ArrayAdapter<Livro> {

    public LivrosListAdapter(Context context, List<Livro> livros){
        super(context, 0, livros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Livro livro = getItem(position);

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_livro_list, null);
            holder = new ViewHolder();
            holder.imagemCapa = convertView.findViewById(R.id.imagemCapa);
            holder.textViewTitulo = convertView.findViewById(R.id.textViewTitulo);
            holder.textViewAutores = convertView.findViewById(R.id.textViewAutores);
            holder.textViewPaginas = convertView.findViewById(R.id.textViewPaginas);
            holder.textViewAno = convertView.findViewById(R.id.textViewAno);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Picasso.get().load(livro.capa).into(holder.imagemCapa);
        holder.textViewTitulo.setText(livro.titulo);
        holder.textViewAutores.setText(livro.autor);
        holder.textViewAno.setText(String.valueOf(livro.ano));
        holder.textViewPaginas.setText(getContext().getString(R.string.numero_paginas, livro.paginas));
        return convertView;
    }

    static class ViewHolder{
        ImageView imagemCapa;
        TextView textViewTitulo;
        TextView textViewAutores;
        TextView textViewPaginas;
        TextView textViewAno;
    }
}