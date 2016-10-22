package com.example.fugliara.ccs002.server.dataobjects;

public class RouteItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("from")
    public LatLnItem from;
    @com.google.gson.annotations.SerializedName("to")
    public LatLnItem to;
    @com.google.gson.annotations.SerializedName("user")
    public UserItem user;

    public RouteItem() {
        id = "";
        from = null;
        to = null;
        user = null;
    }

}
