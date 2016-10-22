package com.example.fugliara.ccs002.server.dataobjects;

public class StatusDescriptionItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("name")
    public String name;
    @com.google.gson.annotations.SerializedName("type")
    public int type;
    @com.google.gson.annotations.SerializedName("userType")
    public int userType;

    public StatusDescriptionItem() {
        id = "";
        name = "";
        type = 0;
        userType = 0;
    }
}
