package com.example.fugliara.ccs002.server.dataobjects;

public class UserRatingItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("comments")
    public String comments;
    @com.google.gson.annotations.SerializedName("value")
    public int value;
    @com.google.gson.annotations.SerializedName("userSource")
    public UserItem userSource;
    @com.google.gson.annotations.SerializedName("userDestin")
    public UserItem userDestin;
    @com.google.gson.annotations.SerializedName("orderItem")
    public OrderItem orderItem;
    @com.google.gson.annotations.SerializedName("dateTime")
    public String dateTime;

    public UserRatingItem() {
        id = "";
        comments = "";
        value = 0;
        userDestin = null;
        userSource = null;
        orderItem = null;
        dateTime = "";
    }
}
