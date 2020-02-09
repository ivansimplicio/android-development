package com.dev.meuaplicativo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

public class HotelCursorAdapter extends CursorAdapter {

    public HotelCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewMensagem = view.findViewById(R.id.textViewNome);
        RatingBar ratingBarEstrelas = view.findViewById(R.id.ratingBarEstrelas);

        textViewMensagem.setText(cursor.getString(cursor.getColumnIndex(HotelSQLHelper.COLUNA_NOME)));
        ratingBarEstrelas.setRating(cursor.getFloat(cursor.getColumnIndex(HotelSQLHelper.COLUNA_ESTRELAS)));

        int status = cursor.getInt(cursor.getColumnIndex(HotelSQLHelper.COLUNA_STATUS));
        if(status == Hotel.Status.EXCLUIR.ordinal()){
            textViewMensagem.setTextColor(Color.RED);
        }else{
            textViewMensagem.setTextColor(Color.BLACK);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_hotel, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        ListView listView = (ListView)parent;

        int color = listView.isItemChecked(position) ?
                Color.argb(0xFF, 0x31, 0xB6, 0xE7) :
                Color.TRANSPARENT;
        view.setBackgroundColor(color);
        return view;
    }
}