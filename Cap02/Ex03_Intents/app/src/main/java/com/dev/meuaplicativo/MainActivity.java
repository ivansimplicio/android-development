package com.dev.meuaplicativo;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private static final String[] OPCOES = {
            "Browser",
            "Realizando uma chamada",
            "Mapa",
            "Tocar música",
            "SMS",
            "Compartilhar",
            "Minha ação 1",
            "Minha ação 2",
            "Sair"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, OPCOES);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Uri uri = null;
        Intent it = null;

        switch (position){
                //Abrindo uma URL
            case 0:
                uri = Uri.parse("https://github.com/ivansimplicio");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
                //Realizando uma chamada
            case 1:
                uri = Uri.parse("tel:986876782");
                it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
                break;
                //Pesquisa uma posição no Mapa
            case 2:
                uri = Uri.parse("geo:0,0?q=Rua+Gama+Rosa,Arara");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
                //Executa música do SD Card
            case 3:
                uri = Uri.parse("file:///mnt/sdcard/musica.mp3");
                it = new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setDataAndType(uri, "audio/mp3");
                if(it.resolveActivity(getPackageManager()) != null){
                    startActivity(it);
                }else{
                    Toast.makeText(this, "Não foi possível executar a ação.", Toast.LENGTH_SHORT).show();
                }
                break;
                //Abrindo o editor de SMS
            case 4:
                uri = Uri.parse("sms:12345");
                it = new Intent(Intent.ACTION_VIEW, uri);
                it.putExtra("sms_body", "Enviando um SMS...");
                startActivity(it);
                break;
                //Compartilhar
            case 5:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Compartilhando via Intent.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
                //Ação customizada 1
            case 6:
                it = new Intent("com.dev.meuaplicativo.ACAO_CUSTOMIZADA");
                startActivity(it);
                break;
                //Ação customizada 2
            case 7:
                uri = Uri.parse("produto://Notebook/Slim");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            default:
                finish();
        }
    }
}