package com.blogspot.darwinsapp.trollkoduvally;


public class Blog {

    private String title, desc, imageUrl, username,email;









    public Blog(String title, String desc, String imageUrl, String username, String email) {
        this.title = title;
        this.desc = desc;
        this.imageUrl=imageUrl;
        this.username = username;
        this.email=email;
    }
    public String getEmail() {
        return email;
    }



    public Blog() {
    }



    public String getUsername() {
        return username;
    }





    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }


}