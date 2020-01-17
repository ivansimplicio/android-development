package com.dev.meuaplicativo;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration configuration = getResources().getConfiguration();
        int density = configuration.densityDpi;
        int orientation = configuration.orientation;
        int height = configuration.screenHeightDp;
        int width = configuration.screenWidthDp;
        int mcc = configuration.mcc;
        int mnc = configuration.mnc;

        Locale locale = configuration.locale;
        Log.d("Log::Teste", "density: "+density);
        Log.d("Log::Teste", "orientation: "+orientation);
        Log.d("Log::Teste", "height: "+height);
        Log.d("Log::Teste", "width: "+width);
        Log.d("Log::Teste", "language: "+locale.getLanguage()+"-"+locale.getCountry());
        Log.d("Log::Teste", "mcc: "+mcc);
        Log.d("Log::Teste", "mnc"+mnc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}