package com.dev.meuaplicativo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton instance = null;
    // Fila de execução
    private RequestQueue requestQueue;
    // Image Loader
    private ImageLoader imageLoader;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(this.requestQueue, new BitmapCache(50));
    }

    public static VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return this.requestQueue;
    }

    public ImageLoader getImageLoader(){
        return this.imageLoader;
    }

    public class BitmapCache extends LruCache implements ImageLoader.ImageCache{
        public BitmapCache(int maxSize){
            super(maxSize);
        }

        @Override
        public Bitmap getBitmap(String url) {
            return (Bitmap)get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}