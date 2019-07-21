package com.onshop.yamuna.onshoppeeserver.Models;

public class Spice {
    private String Name,Price,Image,MenuId;

    public Spice() {
    }

    public Spice(String name, String price, String image, String menuid) {
        Name = name;
        Price = price;
        Image = image;
        MenuId = menuid;
    }

    public String getMenuid() {
        return MenuId;
    }

    public void setMenuid(String menuid) {
        MenuId = menuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
