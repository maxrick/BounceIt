package com.max.jumpingapp.views;

/**
 * Created by max on 5/29/2016.
 */
public class Buyable {
    private final int image;
    private final String description;
    private final int price;

    public Buyable(int image, String description, int price) {
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
