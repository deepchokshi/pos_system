package com.chokshi.deep.pos_system;

public class User {
    int userId;
    int userPin;
    String userRole;

    public User(int userId, int userPin, String userRole) {
        this.userId = userId;
        this.userPin = userPin;
        this.userRole = userRole;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserPin() {
        return userPin;
    }

    public void setUserPin(int userPin) {
        this.userPin = userPin;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
