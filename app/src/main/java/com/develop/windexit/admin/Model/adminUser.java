package com.develop.windexit.admin.Model;

/**
 * Created by WINDEX IT on 08-May-18.
 */

public class adminUser {
    private String name;
    private String password;
    private String phone;

    public adminUser() {
    }

    public adminUser(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
