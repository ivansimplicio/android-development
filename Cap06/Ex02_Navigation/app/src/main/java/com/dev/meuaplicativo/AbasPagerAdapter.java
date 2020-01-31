package com.dev.meuaplicativo;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Locale;

public class AbasPagerAdapter extends FragmentPagerAdapter {

    String[] titulosAbas;
    TypedArray backgroundColors;
    TypedArray textColors;

    public AbasPagerAdapter(Context context, FragmentManager fm){
        super(fm);
        titulosAbas = context.getResources().getStringArray(R.array.secoes);
        backgroundColors = context.getResources().obtainTypedArray(R.array.cores_bg);
        textColors = context.getResources().obtainTypedArray(R.array.cores_texto);
    }

    @Override
    public Fragment getItem(int position) {
        SegundoNivelFragment fragment = SegundoNivelFragment.novaInstancia(
                titulosAbas[position], backgroundColors.getColor(position, 0),
                textColors.getColor(position, 0));
        return fragment;
    }

    @Override
    public int getCount() {
        return titulosAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        return titulosAbas[position].toUpperCase(l);
    }
}