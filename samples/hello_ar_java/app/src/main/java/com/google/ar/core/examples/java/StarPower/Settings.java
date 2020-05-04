package com.google.ar.core.examples.java.StarPower;

public class Settings {

    //constructor
    public Settings(){
        volume = 50;
    }

    public void raiseVolume(int increase){
        volume += increase;
    }

    public void lowerVolume(int decrease){
        volume -= decrease;
    }

    public void setVolume(int newVolume){
        volume += newVolume;
    }

    public int getVolume() { return volume; }

    public void selectCustomization(){

    }

    private int volume;
    //customizations variable?
    //purchased customizations variable?
}
