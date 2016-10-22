package com.example.fugliara.ccs002.server.dataobjects;

public class LatLnItem {

    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("name")
    public String name;
    @com.google.gson.annotations.SerializedName("latitud")
    public String latitud;
    @com.google.gson.annotations.SerializedName("longitud")
    public String longitud;

    public LatLnItem(String name, String latitud, String longitud) {
        id = "";
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
