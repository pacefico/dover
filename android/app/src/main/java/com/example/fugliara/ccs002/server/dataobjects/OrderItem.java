package com.example.fugliara.ccs002.server.dataobjects;

import java.util.List;

public class OrderItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("routeItem")
    public RouteItem route;
    @com.google.gson.annotations.SerializedName("userItem")
    public UserItem user;
    @com.google.gson.annotations.SerializedName("dateTime")
    public String date;
    @com.google.gson.annotations.SerializedName("comments")
    public String comments;
    @com.google.gson.annotations.SerializedName("productDetailItems")
    public List<ProductDetailItem> productDetailItems;

    public OrderItem() {
        id = "";
        route = null;
        user = null;
        date = "";
        comments = "";
        productDetailItems = null;
    }
}
