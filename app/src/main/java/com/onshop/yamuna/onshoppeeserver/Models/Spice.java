package com.onshop.yamuna.onshoppeeserver.Models;

public class Spice {
    private String Name,Price,Image,menuId;

    public Spice() {
    }

    public Spice(String name, String price, String image, String menuId) {
        Name = name;
        Price = price;
        Image = image;
        menuId = menuId;
    }

    public String getmenuId() {
        return menuId;
    }

    public void setmenuId(String menuId) {
        this.menuId = menuId;
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
