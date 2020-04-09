package com.google.ar.core.examples.java.helloar;

public class Customization {

    //constructor
    public Customization(){
        isPurchased = false;
        price = 0;
    }

    public int getModel(){
        return 0;
    }

    public int getPurchased(){
        return 0;
    }

    public void purchase(int choice){
        //validate payment through Google Play store

        isPurchased = true;
    }

    private boolean isPurchased;
    private double price;
}
