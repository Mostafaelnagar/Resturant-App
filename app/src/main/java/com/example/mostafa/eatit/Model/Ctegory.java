package com.example.mostafa.eatit.Model;

/**
 * Created by mostafa on 9/3/2017.
 */

public class Ctegory {
    private String Name;
    private String Image;

    public Ctegory() {
    }

    public Ctegory(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
