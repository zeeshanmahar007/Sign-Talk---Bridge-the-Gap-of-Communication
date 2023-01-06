package com.example.signtalk.ModeSelectionAndNavDrawer;

import java.io.Serializable;

public class CategoryClass implements Serializable {

    String Name;
    String Videoid;

    public CategoryClass() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVideoid() {
        return Videoid;
    }

    public void setVideoid(String videoid) {
        Videoid = videoid;
    }

    public CategoryClass(String name, String videoid) {
        Name = name;
        Videoid = videoid;
    }



}
