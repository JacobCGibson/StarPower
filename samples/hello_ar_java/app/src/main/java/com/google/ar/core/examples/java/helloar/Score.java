package com.google.ar.core.examples.java.helloar;

public class Score {

    //constructor
    public Score(){
        initials = "";
        points = 0;
    }

    public void addPoints(int newPoints){
        points += newPoints;
    }

    public void resetScore(){
        points = 0;
    }

    public void setInitials(String name){
        initials = name;
    }

    public String getInitials(){
        //return initials instance variable
        return initials;
    }

    public int getPoints(){
        //return points instance variable
        return points;
    }

    private String initials;
    private int points;

}
