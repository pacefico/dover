package com.example.fugliara.ccs002.server.dataobjects;

public class ProductAddressItem {
    @com.google.gson.annotations.SerializedName("id")
    public String id;
    @com.google.gson.annotations.SerializedName("address")
    public String address;
    @com.google.gson.annotations.SerializedName("number")
    public String number;
    @com.google.gson.annotations.SerializedName("location")
    public String location;
    @com.google.gson.annotations.SerializedName("city")
    public String city;
    @com.google.gson.annotations.SerializedName("state")
    public String state;
    @com.google.gson.annotations.SerializedName("postalCode")
    public String postalCode;
    @com.google.gson.annotations.SerializedName("reference")
    public String reference;
    @com.google.gson.annotations.SerializedName("productDetailItem")
    public ProductDetailItem productDetailItem;

    public ProductAddressItem() {
        id = "";
        address = "";
        number = "";
        location = "";
        city = "";
        state = "";
        postalCode = "";
        reference = "";
        productDetailItem = null;
    }
}
