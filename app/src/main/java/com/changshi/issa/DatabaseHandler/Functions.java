package com.changshi.issa.DatabaseHandler;

public class Functions {
    private String Name;
    private String FunctionURL = "";
    private int Image;

    public String getNameOfFunction() {
        return Name;
    }
    public void setNameOfFunction(String name) {Name = name;}

    public String getFunctionURL() {
        return FunctionURL;
    }
    public void setFunctionURL(String imageURL) { FunctionURL = imageURL; }

    public int getFunctionImage() {
        return Image;
    }
    public void setFunctionImage(int image) { Image = image; }
}
