package com.dev.meuaplicativo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HotelProvider extends ContentProvider {

    private static final String AUTHORITY = "com.dev.meuaplicativo";
    private static final String PATH = "hoteis";
    private static final int TIPO_GERAL = 1;
    private static final int TIPO_HOTEL_ESPECIFICO = 2;
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+PATH);

    private HotelSQLHelper helper;

    private static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, TIPO_GERAL);
        uriMatcher.addURI(AUTHORITY, PATH+"/#", TIPO_HOTEL_ESPECIFICO);
    }

    @Override
    public boolean onCreate() {
        helper = new HotelSQLHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null; //Não implementamos busca por MimeType
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        long id = 0;
        switch (uriType){
            case TIPO_GERAL:
                id = sqlDB.insertWithOnConflict(HotelSQLHelper.TABELA_HOTEL, null,
                        values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                throw new IllegalArgumentException("URI não suportada: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        int linhasAfetadas = 0;
        switch (uriType){
            case TIPO_GERAL:
                linhasAfetadas = sqlDB.update(HotelSQLHelper.TABELA_HOTEL, values,
                        selection, selectionArgs);
                break;
            case TIPO_HOTEL_ESPECIFICO:
                String id = uri.getLastPathSegment();
                linhasAfetadas = sqlDB.update(HotelSQLHelper.TABELA_HOTEL, values,
                        HotelSQLHelper.COLUNA_ID+" = ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("URI não suportada: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return linhasAfetadas;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType){
            case TIPO_GERAL:
                rowsDeleted = sqlDB.delete(HotelSQLHelper.TABELA_HOTEL, selection, selectionArgs);
                break;
            case TIPO_HOTEL_ESPECIFICO:
                String id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(HotelSQLHelper.TABELA_HOTEL,
                        HotelSQLHelper.COLUNA_ID+" = ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("URI não suportada: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(HotelSQLHelper.TABELA_HOTEL);
        Cursor cursor = null;

        switch (uriType){
            case TIPO_GERAL:
                cursor = queryBuilder.query(sqlDB, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TIPO_HOTEL_ESPECIFICO:
                queryBuilder.appendWhere(HotelSQLHelper.COLUNA_ID+" = ?");
                cursor = queryBuilder.query(sqlDB, projection, selection,
                        new String[]{uri.getLastPathSegment()}, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("URI não suportada: "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
}