package com.max.jumpingapp.views;

/**
 * Created by max on 5/29/2016.
 */
public class Buyable {
    private final int image;
    private final int lockedImage;
    private final String description;
    private final int price;

    public Buyable(int image, int lockedImage, String description, int price) {
        this.image = image;
        this.lockedImage = lockedImage;
        this.description = description;
        this.price = price;
    }

    public int getImage() {
        return image;
    }
    
    public int getLockedImageImage() {
        return lockedImage;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
