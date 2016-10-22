package com.example.fugliara.ccs002.server.dataobjects;

public class ImageItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("imageUrl")
    public String imageUrl;

    public ImageItem() {
        id = "";
        imageUrl = "";
    }
}
