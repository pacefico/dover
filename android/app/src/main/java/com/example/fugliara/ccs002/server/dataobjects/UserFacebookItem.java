package com.example.fugliara.ccs002.server.dataobjects;

public class UserFacebookItem {

    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("email")
    public String email;
    @com.google.gson.annotations.SerializedName("name")
    public String name;
    @com.google.gson.annotations.SerializedName("dateTime")
    public String date;
    @com.google.gson.annotations.SerializedName("token")
    public String token;

    public UserFacebookItem() {
        id = "";
        date = "";
        name = "";
        email = "";
        token = "";
    }
}
