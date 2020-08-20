package com.blogspot.darwinsapp.trollkoduvally;

public class Users {
    String Username,dp,email,phone;

    public Users() {
    }

    public Users(String username, String dp, String email, String phone) {
        Username = username;
        this.dp = dp;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
