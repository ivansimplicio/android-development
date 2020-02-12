package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "meu_canal_001";

    private static final int NOTIFICACAO_SIMPLES = 1;
    private static final int NOTIFICACAO_COMPLETA = 2;
    private static final int NOTIFICACAO_BIG = 3;

    EditText editText;
    MeuReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarCanalDeNotificacao();

        editText = findViewById(R.id.editText);
        receiver = new MeuReceiver();
        registerReceiver(receiver, new IntentFilter(NotificationUtils.ACAO_DELETE));
        registerReceiver(receiver, new IntentFilter(NotificationUtils.ACAO_NOTIFICATION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void criarNotificacaoSimples(View view){
        NotificationUtils.criarNotificacaoSimples(this, editText.getText().toString(), NOTIFICACAO_SIMPLES);
    }

    public void criarNotificacaoCompleta(View view){
        NotificationUtils.criarNotificacaoCompleta(this, editText.getText().toString(), NOTIFICACAO_COMPLETA);
    }

    public void criarNotificacaoBig(View view){
        NotificationUtils.criarNotificationBig(this, NOTIFICACAO_BIG);
    }

    class MeuReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

    private void criarCanalDeNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal Padrão";
            String description = "Descrição: Canal padrão para notificações";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}