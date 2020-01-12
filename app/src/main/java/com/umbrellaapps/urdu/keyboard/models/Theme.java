package com.umbrellaapps.urdu.keyboard.models;

public class Theme {

    private int imageId;
    private String bgImageId;
    private boolean selected = false;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getBgImageId() {
        return bgImageId;
    }

    public void setBgImageId(String bgImageId) {
        this.bgImageId = bgImageId;
    }
}
