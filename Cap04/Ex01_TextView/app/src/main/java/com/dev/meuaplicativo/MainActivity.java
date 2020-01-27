package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typefaceLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        Typeface typefaceCondensed = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");

        TextView textView1 = findViewById(R.id.textViewFonte1);
        textView1.setTypeface(typefaceLight);

        TextView textView2 = findViewById(R.id.textViewFonte2);
        textView2.setTypeface(typefaceCondensed);

        TextView textView3 = findViewById(R.id.textViewFonteStrike);
        textView3.setPaintFlags(textView3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView textViewHtml = findViewById(R.id.textViewHtml);
        final String textoEmHtml =
                "<html><body>Html em " +
                "<b>Negrito</b>, <i>Itálico</i>" +
                "e <u>Sublinhado</u>.<br>" +
                "Mario: <img src='mario.png'/><br>" +
                "Yoshi: <img src='yoshi.png'/><br>" +
                "Um texto simples depois da imagem.</body></html>";

        Html.ImageGetter imageGetter = new Html.ImageGetter(){
            public Drawable getDrawable(String source) {
                BitmapDrawable drawable = null;
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(source));
                    drawable = new BitmapDrawable(getResources(), bitmap);
                    drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return drawable;
            }
        };
        textViewHtml.setText(Html.fromHtml(textoEmHtml, imageGetter, null));
    }
}