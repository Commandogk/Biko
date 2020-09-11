package com.example.project;



public class user {
    public String name;
    public String username;
    public String bio;
    public String email;
    public String propic;
    public user(){

    }


    public user(String bio1, String email1, String name1, String propic1, String username1 ) {
        this.bio = bio1;
        this.email = email1;
        this.username = username1;
        this.propic = propic1;
        this.name = name1;
    }
}
