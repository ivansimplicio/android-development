package com.dev.meuaplicativo;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarrosAdapter extends BaseAdapter {

    Context context;
    List<Carro> carros;

    public CarrosAdapter(Context context, List<Carro> carros) {
        this.context = context;
        this.carros = carros;
    }

    @Override
    public int getCount() {
        return carros.size();
    }

    @Override
    public Object getItem(int position) {
        return carros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1 Passo
        Carro carro = carros.get(position);

        // 2 Passo
        ViewHolder holder = null;
        if(convertView == null){
            Log.d("Log::Teste", "View nova -> position: "+position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_carro, null);
            holder = new ViewHolder();
            holder.imageLogo = convertView.findViewById(R.id.imageLogo);
            holder.textViewModelo = convertView.findViewById(R.id.textViewModelo);
            holder.textViewAno = convertView.findViewById(R.id.textViewAno);
            holder.textViewCombustivel = convertView.findViewById(R.id.textViewCombustivel);
            convertView.setTag(holder);
        }else{
            Log.d("Log::Teste", "View existente -> position: "+position);
            holder = (ViewHolder)convertView.getTag();
        }

        // 3 Passo
        //0=VW, 1=GM, 2=Fiat, 3=Ford
        Resources res = context.getResources();
        TypedArray logos = res.obtainTypedArray(R.array.logos);
        holder.imageLogo.setImageDrawable(logos.getDrawable(carro.fabricante));
        holder.textViewModelo.setText(carro.modelo);
        holder.textViewAno.setText(String.valueOf(carro.ano));
        holder.textViewCombustivel.setText((carro.gasolina ? "G": "")+(carro.etanol ? "E" : ""));

        // 4 Passo
        return convertView;
    }

    static class ViewHolder{
        ImageView imageLogo;
        TextView textViewModelo;
        TextView textViewAno;
        TextView textViewCombustivel;
    }
}