package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String user;
    private String Name;
    private String Password;
    private  String email;
    public User() {
    }
    public User(String user, String name, String password, String gmail) {
        this.user = user;
        Name = name;
        Password = password;
        this.email=gmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",Name);
        return result;
    }
    public Map<String, Object> toMap1(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("password",Password);
        return result;
    }
}
