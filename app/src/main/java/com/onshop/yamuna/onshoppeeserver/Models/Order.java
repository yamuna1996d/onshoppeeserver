package com.onshop.yamuna.onshoppeeserver.Models;

public class Order {
    private String Spiceid,Spicename,Quantity,Price;

    public Order() {
    }

    public Order(String spiceid, String spicename, String quantity, String price) {
        Spiceid = spiceid;
        Spicename = spicename;
        Quantity = quantity;
        Price = price;
    }

    public String getSpiceid() {
        return Spiceid;
    }

    public void setSpiceid(String spiceid) {
        Spiceid = spiceid;
    }

    public String getSpicename() {
        return Spicename;
    }

    public void setSpicename(String spicename) {
        Spicename = spicename;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
