package com.develop.windexit.admin.Model;

/**
 * Created by WINDEX IT on 27-Feb-18.
 */

public class User {
    private String image;
    private String email;
    private String Name;
    private String Password;
    private String Phone;
    //private String IsStaff;
    private String homeAddress;
   // private Object balance;
    private String birthday;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        //IsStaff = "false";
    }

    public User(String image, String email, String name, String password, String phone, String homeAddress, String birthday) {
        this.image = image;
        this.email = email;
        Name = name;
        Password = password;
        Phone = phone;
        this.homeAddress = homeAddress;

        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
