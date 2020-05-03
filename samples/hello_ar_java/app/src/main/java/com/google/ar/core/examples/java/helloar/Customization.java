package com.google.ar.core.examples.java.helloar;


import android.graphics.drawable.Drawable;

public class Customization {
    private String name;
    private Integer image;

    //constructor
    public Customization(String custName, Integer custImage) {
        name = custName;
        image = custImage;
    }

    public String getName() {return name;}

    public Integer getImage() { return image; }

}
