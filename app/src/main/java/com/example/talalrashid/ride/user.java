package com.example.talalrashid.ride;

/**
 * Created by Talal Rashid on 5/2/2017.
 */import android.content.SharedPreferences;

import java.io.Serializable;

public class user implements Serializable{

    private String  email, password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public user(String email, String password) {

        this.email = email;
        this.password = password;
    }
}


