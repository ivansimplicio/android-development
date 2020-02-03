package com.dev.meuaplicativo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

public class HotelDetalheFragment extends Fragment {

    public static final String TAG_DETALHE = "tagDetalhe";
    public static final String EXTRA_HOTEL = "hotel";

    TextView textViewNome;
    TextView textViewEndereco;
    RatingBar ratingBarEstrelas;

    ShareActionProvider shareActionProvider;

    Hotel hotel;

    public static HotelDetalheFragment novaInstancia(Hotel hotel){
        Bundle paramentos = new Bundle();
        paramentos.putSerializable(EXTRA_HOTEL, hotel);

        HotelDetalheFragment fragment = new HotelDetalheFragment();
        fragment.setArguments(paramentos);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotel = (Hotel) getArguments().getSerializable(EXTRA_HOTEL);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_detalhe_hotel, container, false);

        textViewNome = layout.findViewById(R.id.textViewNome);
        textViewEndereco = layout.findViewById(R.id.textViewEndereco);
        ratingBarEstrelas = layout.findViewById(R.id.ratingBarEstrelas);

        if(hotel != null){
            textViewNome.setText(hotel.nome);
            textViewEndereco.setText(hotel.endereco);
            ratingBarEstrelas.setRating(hotel.estrelas);
        }
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_hotel_detalhe, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        String texto = getString(R.string.texto_compartilhar, hotel.nome, hotel.estrelas);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.acao_editar){
            Activity activity = getActivity();
            if(activity instanceof AoEditarHotel){
                AoEditarHotel aoEditarHotel = (AoEditarHotel)activity;
                aoEditarHotel.editarHotel(hotel);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Hotel getHotel(){
        return hotel;
    }

    public interface AoEditarHotel{
        void editarHotel(Hotel hotel);
    }
}