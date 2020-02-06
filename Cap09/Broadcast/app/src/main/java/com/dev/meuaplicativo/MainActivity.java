package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String MINHA_ACAO = "com.dev.meuaplicativo.MINHA_ACAO";

    InternoReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver = new InternoReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filterLocal = new IntentFilter(MINHA_ACAO);
        registerReceiver(receiver, filterLocal);

        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void enviarBroadcast(View view){
        Intent intent = new Intent(MINHA_ACAO);
        sendBroadcast(intent);
    }

    public void enviarLocalBroadcast(View view){
        Intent intent = new Intent(MINHA_ACAO);
        sendBroadcast(intent);
    }

    class InternoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView textView = findViewById(R.id.textViewMensagem);
            textView.setText("Ação:\n"+intent.getAction());
        }
    }
}