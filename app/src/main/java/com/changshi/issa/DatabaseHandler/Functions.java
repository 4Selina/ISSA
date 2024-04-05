package com.changshi.issa.DatabaseHandler;

import android.graphics.drawable.Drawable;

public class Functions {
    private String Name;
    private String ImageURL = "";
    private int Image;


    public String getNameOfFunction() {
        return Name;
    }
    public void setNameOfFunction(String name) {Name = name;}

    public String getfuncationImageURL() {
        return ImageURL;
    }
    public void setfuncationImage(String imageURL) { ImageURL = imageURL; }

    public int getfuncationImage() {
        return Image;
    }
    public void setfuncationImage(int image) { Image = image; }
}
