package com.dev.meuaplicativo;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class HotelListFragment extends ListFragment
        implements ActionMode.Callback,
        AdapterView.OnItemLongClickListener {

    List<Hotel> hoteis;
    ArrayAdapter<Hotel> adapter;

    ListView listView;
    ActionMode actionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        hoteis = carregarHoteis();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getListView();
        limparBusca();

        /*adapter = new ArrayAdapter<Hotel>(getActivity(),
                android.R.layout.simple_list_item_1, hoteis);
        setListAdapter(adapter);*/
    }

    private List<Hotel> carregarHoteis(){
        List<Hotel> hoteis = new ArrayList<>();
        hoteis.add(new Hotel("New Beach Hotel", "Av. Boa Viagem", 4.5f));
        hoteis.add(new Hotel("Recife Hotel","Av. Boa Viagem", 4.0f));
        hoteis.add(new Hotel("Canario Hotel","Rua dos Navegantes", 3.0f));
        hoteis.add(new Hotel("Byanca Beach Hotel","Rua Mamanguape", 4.0f));
        hoteis.add(new Hotel("Grand Hotel Dor","Av. Bernardo", 3.5f));
        hoteis.add(new Hotel("Hotel Cool","Av. Conselheiro Aguiar", 4.0f));
        hoteis.add(new Hotel("Hotel Infinito","Rua Ribeiro de Brito", 5.0f));
        hoteis.add(new Hotel("Hotel Tulipa","Av. Boa Viagem", 5.0f));
        return hoteis;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(actionMode == null){
            Activity activity = getActivity();
            if(activity instanceof AoClicarNoHotel){
                Hotel hotel = (Hotel)l.getItemAtPosition(position);

                AoClicarNoHotel listener = (AoClicarNoHotel)activity;
                listener.clicouNoHotel(hotel);
            }
        }else{
            int checkedCount = atualizarItensMarcados(listView, position);
            if(checkedCount == 0){
                actionMode.finish();
            }
        }
    }

    public void buscar(String s){
        if(s == null || s.trim().equals("")){
            limparBusca();
            return;
        }

        List<Hotel> hoteisEncontrados = new ArrayList<>(hoteis);

        for(int i = hoteisEncontrados.size()-1; i>= 0; i--){
            Hotel hotel = hoteisEncontrados.get(i);
            if(!hotel.nome.toUpperCase().contains(s.toUpperCase())){
                hoteisEncontrados.remove(hotel);
            }
        }

        listView.setOnItemLongClickListener(null);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, hoteisEncontrados);
        setListAdapter(adapter);
    }

    public void limparBusca(){
        listView.setOnItemLongClickListener(this);

        adapter = new MultiSelectAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, hoteis);
        setListAdapter(adapter);
    }

    public void adicionar(Hotel hotel){
        hoteis.add(hotel);
        adapter.notifyDataSetChanged();
    }

    public interface AoClicarNoHotel{
        void clicouNoHotel(Hotel hotel);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_delete_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.acao_delete){
            SparseBooleanArray checked = listView.getCheckedItemPositions();

            for(int i = checked.size()-1; i >= 0; i--){
                if(checked.valueAt(i)){
                    hoteis.remove(checked.keyAt(i));
                }
            }
            actionMode.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        listView.clearChoices();
        adapter.notifyDataSetChanged();
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        boolean consumed = (actionMode == null);

        if(consumed){
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            actionMode = activity.startSupportActionMode(this);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            listView.setItemChecked(position, true);
            atualizarItensMarcados(listView, position);
        }
        return consumed;
    }

    private int atualizarItensMarcados(ListView listView, int position){
        SparseBooleanArray checked = listView.getCheckedItemPositions();

        //listView.setItemChecked(position, listView.isItemChecked(position));
        adapter.notifyDataSetChanged();
        int checkedCount = 0;
        for(int i = 0; i < checked.size(); i++){
            if(checked.valueAt(i)){
                checkedCount++;
            }
        }

        String selecionados = getResources().getQuantityString(
                R.plurals.numero_selecionados, checkedCount, checkedCount);
        actionMode.setTitle(selecionados);

        return checkedCount;
    }
}