package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class TelaAbasActivity extends AppCompatActivity {

    ViewPager viewPager;
    SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_abas);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        AbasPagerAdapter pagerAdapter = new AbasPagerAdapter(this, getSupportFragmentManager());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        slidingTabLayout = findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }
}