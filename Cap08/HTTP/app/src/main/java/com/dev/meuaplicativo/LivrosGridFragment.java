package com.dev.meuaplicativo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LivrosGridFragment extends Fragment
        implements Response.Listener<JSONObject>,
        Response.ErrorListener {

    List<Livro> livros;
    GridView gridView;
    TextView textView;
    ProgressBar progressBar;
    ArrayAdapter<Livro> adapter;

    boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_livros_grid, null);
        progressBar = layout.findViewById(R.id.progressBar);
        textView = layout.findViewById(android.R.id.empty);
        gridView = layout.findViewById(R.id.gridView);
        gridView.setEmptyView(textView);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(livros == null){
            livros = new ArrayList<>();
        }
        adapter = new LivrosGridAdapter(getActivity(), livros);
        gridView.setAdapter(adapter);

        if(!isRunning){
            if(LivroHttp.temConexao(getActivity())){
                iniciarDownload();
            }else{
                textView.setText("Sem conexão");
            }
        }else{
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
        isRunning = true;
        exibirProgress(true);
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,         // Requisição via HTTP_GET
                LivroHttp.LIVROS_URL_JSON,  // URL da requisição
                null,           // JSONObject a ser enviado via POST
                this,               // Response.Listener
                this);          // Response.Error.Listener

        queue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        isRunning = false;
        exibirProgress(false);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Falha ao obter livros");
    }

    @Override
    public void onResponse(JSONObject response) {
        isRunning = false;
        exibirProgress(false);
        try{
            List<Livro> livros = LivroHttp.lerJsonLivros(response);
            this.livros.clear();
            this.livros.addAll(livros);
            adapter.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}