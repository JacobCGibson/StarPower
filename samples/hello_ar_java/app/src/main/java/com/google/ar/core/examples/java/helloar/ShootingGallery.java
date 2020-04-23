package com.google.ar.core.examples.java.helloar;

import android.content.Intent;
import android.os.CountDownTimer;

public class ShootingGallery {

    long timeLeftinMilliseconds;
    int timerDuration = 5; //in seconds
    String timeLeftText = "";

    //constructor
    public ShootingGallery(){

    }

    public void updateShootingGallery(){
        //update shooting gallery
    }

    public void displayGalleryModel(){
        //display a shooting gallery model
    }

    public void hideGalleryModel(){
        //hide a shooting gallery model
    }

    public void displayGalleryScore(){
        //display shooting gallery score
    }

    public String getTimeLeftText() { return timeLeftText; }
    
    public int getTimerDuration() {return timerDuration;}

    public void startTimer() {
        timeLeftinMilliseconds = timerDuration*10000;
        updateTimer();
    }

    public long tickTimer(){
        timeLeftinMilliseconds -= 300;
        if (timeLeftinMilliseconds<0) timeLeftinMilliseconds = 0;
        updateTimer();
        return timeLeftinMilliseconds;
    }

    public void updateTimer(){
        int minutes = (int) timeLeftinMilliseconds / 600000;
        int seconds = (int) timeLeftinMilliseconds % 600000 / 10000;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

    }

    public long getTimeLeft(){return timeLeftinMilliseconds;}

}
