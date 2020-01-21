package com.dev.meuaplicativo;

import java.io.Serializable;

public class Pessoa implements Serializable {

    public int idade;
    public String nome;

    public Pessoa(int idade, String nome){
        this.idade = idade;
        this.nome = nome;
    }
}