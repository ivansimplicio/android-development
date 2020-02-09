package com.dev.meuaplicativo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class HotelListFragment extends ListFragment
        implements ActionMode.Callback,
        AdapterView.OnItemLongClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    //List<Hotel> hoteis;
    //ArrayAdapter<Hotel> adapter;

    SwipeRefreshLayout swipeLayout;

    BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            if(!intent.getBooleanExtra(HotelIntentService.EXTRA_SUCESSO, false)){
                Toast.makeText(getActivity(), R.string.erro_sincronizacao, Toast.LENGTH_SHORT).show();
            }
        }
    };

    CursorAdapter adapter;
    String textoBusca;

    ListView listView;
    ActionMode actionMode;

    HotelRepositorio repositorio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        IntentFilter filter = new IntentFilter(HotelIntentService.ACAO_SINCRONIZAR);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(serviceReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(serviceReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list_hotel, null);
        swipeLayout = layout.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.azul, R.color.verde, R.color.laranja, R.color.vermelho);
        return layout;
    }

    @Override
    public void onRefresh() {
        Intent intent = new Intent(getActivity(), HotelIntentService.class);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repositorio = new HotelRepositorio(getActivity());
        adapter = new HotelCursorAdapter(getActivity(), null);
        listView = getListView();
        setListAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(0, null, this);
        //limparBusca();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(actionMode == null){
            Activity activity = getActivity();
            if(activity instanceof AoClicarNoHotel){
                //Hotel hotel = (Hotel)l.getItemAtPosition(position);
                Cursor cursor = (Cursor)l.getItemAtPosition(position);
                Hotel hotel = repositorio.hotelFromCursor(cursor);

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
        textoBusca = TextUtils.isEmpty(s) ? null : s;
        getLoaderManager().restartLoader(0, null, this);
        /*if(s == null || s.trim().equals("")) {
            limparBusca();
            return;
        }
        List<Hotel> hoteisEncontrados = repositorio.buscarHotel("%"+s+"%");
        listView.setOnItemLongClickListener(null);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, hoteisEncontrados);
        setListAdapter(adapter);*/
    }

    public void limparBusca(){
        buscar(null);
        /*hoteis = repositorio.buscarHotel(null);
        listView.setOnItemLongClickListener(this);
        adapter = new MultiSelectAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, hoteis);
        setListAdapter(adapter);*/
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

            List<Hotel> excluidos = new ArrayList<>();
            for(int i = checked.size()-1; i >= 0; i--){
                if(checked.valueAt(i)){
                    //Hotel hotel = hoteis.get(checked.keyAt(i));
                    Cursor cursor = (Cursor)listView.getItemAtPosition(checked.keyAt(i));
                    Hotel hotel = repositorio.hotelFromCursor(cursor);
                    repositorio.excluir(hotel);
                    excluidos.add(hotel);
                }
            }
            limparBusca();
            actionMode.finish();

            Activity activity = getActivity();
            if(activity instanceof AoExcluirHoteis){
                AoExcluirHoteis aoExcluirHoteis = (AoExcluirHoteis)activity;
                aoExcluirHoteis.exclusaoCompleta(excluidos);
            }
            return true;
        }
        return false;
    }

    public interface AoExcluirHoteis{
        void exclusaoCompleta(List<Hotel> excluidos);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        for(int i = 0; i < listView.getCount(); i++){
            listView.setItemChecked(i, false);
        }
        listView.clearChoices();
        //adapter.notifyDataSetChanged();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args) {
        return repositorio.buscar(getActivity(), textoBusca);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}