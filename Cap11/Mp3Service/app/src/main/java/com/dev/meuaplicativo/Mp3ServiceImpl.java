package com.dev.meuaplicativo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileInputStream;

public class Mp3ServiceImpl extends Service implements Mp3Service{

    public static final String EXTRA_ARQUIVO = "arquivo";
    public static final String EXTRA_ACAO = "acao";
    public static final String ACAO_PLAY = "play";
    public static final String ACAO_PAUSE = "pause";
    public static final String ACAO_STOP = "stop";

    private MediaPlayer player;
    private String arquivo;
    private boolean pausado;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Mp3Binder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            if(ACAO_PLAY.equals(intent.getStringExtra(EXTRA_ACAO))){
                play(intent.getStringExtra(EXTRA_ARQUIVO));
            }else if(ACAO_PAUSE.equals(intent.getStringExtra(EXTRA_ACAO))){
                pause();
            }else if(ACAO_STOP.equals(intent.getStringExtra(EXTRA_ACAO))){
                stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void play(String musica) {
        if(musica != null && !player.isPlaying() && !pausado){
            try{
                player.reset();
                FileInputStream fis = new FileInputStream(musica);
                player.setDataSource(fis.getFD());
                player.prepare();
                arquivo = musica;
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        pausado = false;
        player.start();
        criarNotificacao();
    }

    @Override
    public void pause() {
        if(player.isPlaying()){
            pausado = true;
            player.pause();
        }
    }

    @Override
    public void stop() {
        if(player.isPlaying() || pausado){
            pausado = false;
            player.stop();
            player.reset();
        }
        removerNotificacao();
    }

    @Override
    public String getMusicaAtual() {
        return arquivo;
    }

    @Override
    public int getTempoTotal() {
        if(player.isPlaying() || pausado){
            return player.getDuration();
        }
        return 0;
    }

    @Override
    public int getTempoDecorrido() {
        if(player.isPlaying() || pausado){
            return player.getCurrentPosition();
        }
        return 0;
    }

    private void criarNotificacao(){
        Intent intentPlay = new Intent(this, Mp3ServiceImpl.class);
        intentPlay.putExtra(EXTRA_ACAO, ACAO_PLAY);
        Intent intentPause = new Intent(this, Mp3ServiceImpl.class);
        intentPause.putExtra(EXTRA_ACAO, ACAO_PAUSE);
        Intent intentStop = new Intent(this, Mp3ServiceImpl.class);
        intentStop.putExtra(EXTRA_ACAO, ACAO_STOP);

        PendingIntent pitPlay = PendingIntent.getService(this, 1, intentPlay, 0);
        PendingIntent pitPause = PendingIntent.getService(this, 2, intentPause, 0);
        PendingIntent pitStop = PendingIntent.getService(this, 3, intentStop, 0);

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.layout_notificacao);
        views.setOnClickPendingIntent(R.id.imageButtonPlay, pitPlay);
        views.setOnClickPendingIntent(R.id.imageButtonPause, pitPause);
        views.setOnClickPendingIntent(R.id.imageButtonClose, pitStop);
        views.setTextViewText(R.id.textViewMusica, arquivo.substring(arquivo.lastIndexOf(File.separator)+1));

        Notification notification = new NotificationCompat.Builder(this, Mp3Activity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(views)
                .setOngoing(true)
                .build();

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(1, notification);
    }

    private void removerNotificacao(){
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.cancel(1);
    }
}