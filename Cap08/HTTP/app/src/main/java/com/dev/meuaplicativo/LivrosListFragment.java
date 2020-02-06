package com.dev.meuaplicativo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class LivrosListFragment extends Fragment {

    LivrosTask task;
    List<Livro> listaDeLivros;
    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    ArrayAdapter<Livro> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_livros_list, null);
        textView = layout.findViewById(android.R.id.empty);
        progressBar = layout.findViewById(R.id.progressBar);
        listView = layout.findViewById(R.id.listView);
        listView.setEmptyView(textView);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(listaDeLivros == null){
            listaDeLivros = new ArrayList<>();
        }

        adapter = new LivrosListAdapter(getActivity(), listaDeLivros);
        listView.setAdapter(adapter);

        if(task == null){
            if(LivroHttp.temConexao(getActivity())){
                iniciarDownload();
            }else{
                textView.setText("Sem conexão");
            }
        }else if(task.getStatus() == AsyncTask.Status.RUNNING){
            exibirProgress(true);
        }
    }

    private void exibirProgress(boolean exibir){
        if(exibir){
            textView.setText("Baixando informações dos livros...");
        }
        textView.setVisibility(exibir ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void iniciarDownload(){
        if(task == null || task.getStatus() != AsyncTask.Status.RUNNING){
            task = new LivrosTask();
            task.execute();
        }
    }

    class LivrosTask extends AsyncTask<Void, Void, List<Livro>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Livro> doInBackground(Void... voids) {
            //return LivroHttp.carregarLivrosJson();
            return LivroHttp.carregarLivrosXml();
        }

        @Override
        protected void onPostExecute(List<Livro> livros) {
            super.onPostExecute(livros);
            exibirProgress(false);
            if(livros != null){
                listaDeLivros.clear();
                listaDeLivros.addAll(livros);
                adapter.notifyDataSetChanged();
            }else{
                textView.setVisibility(View.VISIBLE);
                textView.setText("Falha ao obter livros");
            }
        }
    }
}