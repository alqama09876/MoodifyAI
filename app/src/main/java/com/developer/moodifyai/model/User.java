package com.developer.moodifyai.model;

public class User {
    private String uid;
    private String userName;
    private String email;
    private String role;

    public User() {
    }

    public User(String uid, String userName, String email, String role) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return userName;
    }

    public void setName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}