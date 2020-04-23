package com.google.ar.core.examples.java.helloar;

public class Timer {

    long timeLeftinMilliseconds;
    int timerDuration = 20; //in seconds
    String timeLeftText = "10:00";

    //constructor
    public Timer(){
    }

    public String getTimeLeftText() { return timeLeftText; }

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
