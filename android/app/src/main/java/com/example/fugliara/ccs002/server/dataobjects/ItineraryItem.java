package com.example.fugliara.ccs002.server.dataobjects;

public class ItineraryItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("time")
    public String time;
    @com.google.gson.annotations.SerializedName("date")
    public String date;
    @com.google.gson.annotations.SerializedName("routeItem")
    public RouteItem route;

    public ItineraryItem() {
        id = "";
        time = "";
        date = "";
        route = null;
    }
}
