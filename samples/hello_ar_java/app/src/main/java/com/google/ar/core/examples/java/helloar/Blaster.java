package com.google.ar.core.examples.java.helloar;

import android.media.Image;

public class Blaster extends Customization {
    boolean active;
    String name;
    Image image;

    public Blaster(String custName, Image custImage){
        name = custName;
        image = custImage;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
