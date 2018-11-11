package com.develop.windexit.admin.Model;

/**
 * Created by WINDEX IT on 27-Feb-18.
 */

public class Category {
    private String Name;
    private String Image;
    private String ActiveInactive;

    public Category() {
    }

    public Category(String name, String image, String activeInactive) {
        Name = name;
        Image = image;
        ActiveInactive = activeInactive;
    }

    public String getActiveInactive() {
        return ActiveInactive;
    }

    public void setActiveInactive(String activeInactive) {
        ActiveInactive = activeInactive;
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
