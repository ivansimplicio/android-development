package com.dev.meuaplicativo;

import android.os.Binder;

public class Mp3Binder extends Binder {

    private Mp3Service servico;

    public Mp3Binder(Mp3Service servico){
        this.servico = servico;
    }

    public Mp3Service getServico(){
        return this.servico;
    }
}