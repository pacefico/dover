package com.example.fugliara.ccs002.server.dataobjects;

public class UserItem {

    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("name")
    public String name;
    @com.google.gson.annotations.SerializedName("email")
    public String email;
    @com.google.gson.annotations.SerializedName("password")
    public String password;
    @com.google.gson.annotations.SerializedName("userFacebookItem")
    public UserFacebookItem facebookItem;

    public UserItem() {
        id = "";
        name = "";
        email = "";
        password = "";
        facebookItem = null;
    }
}
