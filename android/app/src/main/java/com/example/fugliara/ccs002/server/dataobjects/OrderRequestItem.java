package com.example.fugliara.ccs002.server.dataobjects;

import java.util.List;

public class OrderRequestItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("from")
    public LatLnItem from;
    @com.google.gson.annotations.SerializedName("to")
    public LatLnItem to;
    @com.google.gson.annotations.SerializedName("maxDistance")
    public int maxDistance;
    @com.google.gson.annotations.SerializedName("user")
    public UserItem user;
    @com.google.gson.annotations.SerializedName("results")
    public int results;
    @com.google.gson.annotations.SerializedName("routeItems")
    public List<RouteItem> routeItens;

    public OrderRequestItem() {
        id = "";
        from = null;
        to = null;
        maxDistance = 0;
        user = null;
        results = 0;
        routeItens = null;
    }
}
