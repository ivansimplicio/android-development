package com.dev.meuaplicativo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

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
}