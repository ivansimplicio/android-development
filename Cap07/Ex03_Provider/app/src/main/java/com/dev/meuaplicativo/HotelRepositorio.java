package com.dev.meuaplicativo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.loader.content.CursorLoader;

public class HotelRepositorio {

    private Context context;

    public HotelRepositorio(Context context){
        this.context = context;
    }

    private long inserir(Hotel hotel){
        Uri uri = context.getContentResolver().insert(HotelProvider.CONTENT_URI, getValues(hotel));
        long id = Long.parseLong(uri.getLastPathSegment());
        if(id != -1){
            hotel.id = id;
        }
        return id;
    }

    private int atualizar(Hotel hotel){
        Uri uri = Uri.withAppendedPath(HotelProvider.CONTENT_URI, String.valueOf(hotel.id));
        int linhasAfetadas = context.getContentResolver().update(uri, getValues(hotel), null, null);
        return linhasAfetadas;
    }

    public void salvar(Hotel hotel){
        if(hotel.id == 0){
            inserir(hotel);
        }else{
            atualizar(hotel);
        }
    }

    public int excluir(Hotel hotel){
        Uri uri = Uri.withAppendedPath(HotelProvider.CONTENT_URI, String.valueOf(hotel.id));
        int linhasAfetadas = context.getContentResolver().delete(uri, null, null);
        return linhasAfetadas;
    }

    public CursorLoader buscar(Context context, String s){
        String where = null;
        String[] whereArgs = null;
        if(s != null){
            where = HotelSQLHelper.COLUNA_NOME+" LIKE ? ";
            whereArgs = new String[]{"%"+s+"%"};
        }
        return new CursorLoader(context, HotelProvider.CONTENT_URI, null, where,
                whereArgs, HotelSQLHelper.COLUNA_NOME);
    }

    private ContentValues getValues(Hotel hotel){
        ContentValues cv = new ContentValues();
        cv.put(HotelSQLHelper.COLUNA_NOME, hotel.nome);
        cv.put(HotelSQLHelper.COLUNA_ENDERECO, hotel.endereco);
        cv.put(HotelSQLHelper.COLUNA_ESTRELAS, hotel.estrelas);
        return cv;
    }

    public static Hotel hotelFromCursor(Cursor cursor){
        long id = cursor.getLong(cursor.getColumnIndex(HotelSQLHelper.COLUNA_ID));
        String nome = cursor.getString(cursor.getColumnIndex(HotelSQLHelper.COLUNA_NOME));
        String endereco = cursor.getString(cursor.getColumnIndex(HotelSQLHelper.COLUNA_ENDERECO));
        float estrelas = cursor.getFloat(cursor.getColumnIndex(HotelSQLHelper.COLUNA_ESTRELAS));

        Hotel hotel = new Hotel(id, nome, endereco, estrelas);
        return hotel;
    }
}