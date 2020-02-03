package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class HotelDetalheActivity extends AppCompatActivity
        implements HotelDetalheFragment.AoEditarHotel,
        HotelDialogFragment.AoSalvarHotel {

    public static final String EXTRA_HOTEL = "hotel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detalhe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Hotel hotel = (Hotel) intent.getSerializableExtra(EXTRA_HOTEL);
        exibirHotelFragment(hotel);
    }

    private void exibirHotelFragment(Hotel hotel){
        HotelDetalheFragment fragment = HotelDetalheFragment.novaInstancia(hotel);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.detalhe, fragment, HotelDetalheFragment.TAG_DETALHE);
        ft.commit();
    }

    @Override
    public void editarHotel(Hotel hotel) {
        HotelDialogFragment hotelDialogFragment = HotelDialogFragment.newInstance(hotel);
        hotelDialogFragment.abrir(getSupportFragmentManager());
    }

    @Override
    public void salvouHotel(Hotel hotel) {
        HotelRepositorio repositorio = new HotelRepositorio(this);
        repositorio.salvar(hotel);
        exibirHotelFragment(hotel);
        setResult(RESULT_OK);
    }
}