package com.example.fugliara.ccs002.server.dataobjects;

public class ProductTypeItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("description")
    public String description;

    public ProductTypeItem() {
        id = "";
        description = "";
    }
}
