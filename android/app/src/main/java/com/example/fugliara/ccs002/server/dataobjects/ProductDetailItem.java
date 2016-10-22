package com.example.fugliara.ccs002.server.dataobjects;

import java.util.List;

public class ProductDetailItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("contentDescription")
    public String contentDescription;
    @com.google.gson.annotations.SerializedName("address")
    public String address;
    @com.google.gson.annotations.SerializedName("title")
    public String title;
    @com.google.gson.annotations.SerializedName("service")
    public String service;
    @com.google.gson.annotations.SerializedName("height")
    public int height;
    @com.google.gson.annotations.SerializedName("width")
    public int width;
    @com.google.gson.annotations.SerializedName("length")
    public int length;
    @com.google.gson.annotations.SerializedName("weightKg")
    public int weightKg;
    @com.google.gson.annotations.SerializedName("productTypeItem")
    public ProductTypeItem productTypeItem;
    @com.google.gson.annotations.SerializedName("serviceTypeItem")
    public ServiceTypeItem serviceTypeItem;
    @com.google.gson.annotations.SerializedName("orderItem")
    public OrderItem orderItem;
    @com.google.gson.annotations.SerializedName("imageItems")
    public List<ImageItem> imageItems;

    public ProductDetailItem() {
        id = "";
        contentDescription = "";
        title = "";
        height = 0;
        width = 0;
        length = 0;
        weightKg = 0;
        productTypeItem = null;
        serviceTypeItem = null;
        orderItem = null;
        imageItems = null;
    }
}
