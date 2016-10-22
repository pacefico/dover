package com.example.fugliara.ccs002.server.dataobjects;

import java.util.List;

public class OrderStatusItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("isActual")
    public boolean isActual;
    @com.google.gson.annotations.SerializedName("dateTime")
    public String dateTime;
    @com.google.gson.annotations.SerializedName("orderItem")
    public OrderItem orderItem;
    @com.google.gson.annotations.SerializedName("statusItem")
    public StatusDescriptionItem statusItem;
    @com.google.gson.annotations.SerializedName("imageItems")
    public List<ImageItem> imageItems;

    public OrderStatusItem() {
        id = "";
        isActual = false;
        dateTime = "";
        orderItem = null;
        statusItem = null;
        imageItems = null;
    }
}
