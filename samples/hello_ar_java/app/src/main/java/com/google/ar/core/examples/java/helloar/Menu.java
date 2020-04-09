package com.google.ar.core.examples.java.helloar;

public class Menu {
    //constructor
    public Menu(){

    }

    public void updateMenu(){
        //update menu
    }

    public void goPurchase(){
        //go to purchases menu
    }

    public void goCustomizations(){
        //go to customizations menu
    }

    public void goBack(){
        //go to previous menu
    }

    public void goSettings(){
        //go to settings menu
    }

    public void goShooting(){
        //go to shooting gallery menu
    }

    private enum menus{
        PURCHASES,
        SETTINGS,
        SCOREBOARD,
        SRANGE
    }


}
