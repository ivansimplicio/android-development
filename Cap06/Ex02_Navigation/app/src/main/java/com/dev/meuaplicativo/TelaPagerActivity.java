package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class TelaPagerActivity extends AppCompatActivity {

    AbasPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pager);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        adapter = new AbasPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomPageTransformer());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}