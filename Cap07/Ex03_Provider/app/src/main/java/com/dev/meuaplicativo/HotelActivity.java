package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class HotelActivity extends AppCompatActivity
        implements HotelListFragment.AoClicarNoHotel,
        SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener,
        GenericDialogFragment.AoClicarNoDialog,
        HotelDialogFragment.AoSalvarHotel,
        HotelDetalheFragment.AoEditarHotel,
        HotelListFragment.AoExcluirHoteis {

    private FragmentManager fragmentManager;
    private HotelListFragment listFragment;

    private long idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        fragmentManager = getSupportFragmentManager();
        listFragment = (HotelListFragment) fragmentManager.findFragmentById(R.id.fragmentLista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.hint_busca));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                GenericDialogFragment dialog = GenericDialogFragment.novoDialog(
                        0, R.string.sobre_titulo, R.string.sobre_mensagem,
                        new int[]{android.R.string.ok, R.string.sobre_botao_site}
                );
                dialog.abrir(fragmentManager);
                break;
            case R.id.action_new:
                HotelDialogFragment hotelDialogFragment = HotelDialogFragment.newInstance(null);
                hotelDialogFragment.abrir(getSupportFragmentManager());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            listFragment.limparBusca();
        }
    }

    @Override
    public void aoClicar(int id, int botao) {
        if(botao == DialogInterface.BUTTON_NEGATIVE){
            Uri uri = Uri.parse("https://github.com/ivansimplicio");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void salvouHotel(Hotel hotel) {
        HotelRepositorio repositorio = new HotelRepositorio(this);
        repositorio.salvar(hotel);
        listFragment.limparBusca();
        if(isTablet()){
            clicouNoHotel(hotel);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listFragment.buscar(s);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true; // Para expandir a View
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        listFragment.limparBusca();
        return true; // Para voltar ao normal
    }

    @Override
    public void clicouNoHotel(Hotel hotel) {
        idSelecionado = hotel.id;
        if(isTablet()){
            HotelDetalheFragment fragment = HotelDetalheFragment.novaInstancia(hotel);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.detalhe, fragment, HotelDetalheFragment.TAG_DETALHE);
            ft.commit();
        }else{
            Intent intent = new Intent(this, HotelDetalheActivity.class);
            intent.putExtra(HotelDetalheActivity.EXTRA_HOTEL, hotel);
            startActivityForResult(intent, 0);
        }
    }

    /*private boolean isTablet(){
        return findViewById(R.id.detalhe) != null;
    }*/

    private boolean isTablet(){
        return getResources().getBoolean(R.bool.tablet);
    }

    private boolean isSmartphone(){
        return getResources().getBoolean(R.bool.smartphone);
    }

    @Override
    public void editarHotel(Hotel hotel) {
        HotelDialogFragment hotelDialogFragment = HotelDialogFragment.newInstance(hotel);
        hotelDialogFragment.abrir(getSupportFragmentManager());
    }

    @Override
    public void exclusaoCompleta(List<Hotel> excluidos) {
        HotelDetalheFragment f = (HotelDetalheFragment)fragmentManager.findFragmentByTag(HotelDetalheFragment.TAG_DETALHE);
        if(f != null){
            boolean encontrou = false;
            for(Hotel h : excluidos){
                if(h.id == idSelecionado){
                    encontrou = true;
                    break;
                }
            }
            if(encontrou){
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.remove(f);
                ft.commit();
            }
        }
    }
}