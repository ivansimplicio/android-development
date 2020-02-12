package com.dev.meuaplicativo;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Mp3Activity extends Activity implements ServiceConnection, AdapterView.OnItemClickListener {

    public static final String CHANNEL_ID = "meu_canal_001";

    private Mp3Service mp3Service;
    private ProgressBar progressBar;
    private TextView textViewMusica;
    private TextView textViewDuracao;
    private String musica;

    private Handler handler = new Handler();

    private Thread threadProgresso = new Thread(){
        @Override
        public void run() {
            atualizarTela();
            if(mp3Service.getTempoTotal() > mp3Service.getTempoDecorrido()){
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        criarCanalDeNotificacao();

        progressBar = findViewById(R.id.progressBar);
        textViewMusica = findViewById(R.id.textViewMusica);
        textViewDuracao = findViewById(R.id.textViewTempo);

        String[] colunas = new String[]{
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns._ID
        };
        int[] componentes = new int[]{
                android.R.id.text1,
                android.R.id.text2
        };
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, colunas,
                MediaStore.Audio.AudioColumns.IS_MUSIC+" = 1", null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                cursor, colunas, componentes, 0);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, Mp3ServiceImpl.class);
        startService(intent);
        bindService(intent, this, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        handler.removeCallbacks(threadProgresso);
    }

    public void buttonPlayClick(View view){
        handler.removeCallbacks(threadProgresso);
        if(musica != null){
            mp3Service.play(musica);
            handler.post(threadProgresso);
        }
    }

    public void buttonPauseClick(View view){
        mp3Service.pause();
        handler.removeCallbacks(threadProgresso);
    }

    public void buttonStopClick(View view){
        mp3Service.stop();
        handler.removeCallbacks(threadProgresso);
        progressBar.setProgress(0);
        textViewDuracao.setText(DateUtils.formatElapsedTime(0));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mp3Service = ((Mp3Binder)service).getServico();
        handler.post(threadProgresso);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mp3Service = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor)parent.getItemAtPosition(position);
        String musica = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
        handler.removeCallbacks(threadProgresso);
        if(musica != null){
            mp3Service.stop();
            mp3Service.play(musica);
            handler.post(threadProgresso);
        }
    }

    private void atualizarTela(){
        musica = mp3Service.getMusicaAtual();
        textViewMusica.setText(musica);
        progressBar.setMax(mp3Service.getTempoTotal());
        progressBar.setProgress(mp3Service.getTempoDecorrido());
        textViewDuracao.setText(DateUtils.formatElapsedTime(mp3Service.getTempoDecorrido() / 1000));
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