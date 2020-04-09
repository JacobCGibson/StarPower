package com.google.ar.core.examples.java.helloar;

public class Scoreboard {

    //constructor
    public Scoreboard(){

    }

    public int doesItPlace(int currentScore){
        //if current score is in top 10 scores on scoreboard
        //if yes, return rank
        //if no, return -1

        //probably database call to request top 10 scores;
        //iterate through these scores to find current score's place
        //return place

        //if it gets this far, just return -1
        return -1;
    }

    public void insertScore(int currentScore){
        //insert score into scoreboard
    }

    public void removeScore(int scoreID){
        //remove score by ID
    }

    public void goToMenu(){
        //go to a given menu
    }
}
