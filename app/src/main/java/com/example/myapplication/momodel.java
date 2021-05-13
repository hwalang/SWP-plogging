package com.example.myapplication;

public class momodel {
    String image,name,location;

    public momodel(){

    }

    public momodel(String image, String name, String location) {
        this.image = image;
        this.name = name;
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
