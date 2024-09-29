package com.example.lvnc02;

public class ItemsList {
    private String foodName, foodLink, foodLocation;
    private int foodImage;
    public ItemsList() {}
    public ItemsList(String foodName, String foodLink, int foodImage, String foodLocation) {
        this.foodName = foodName;
        this.foodLink = foodLink;
        this.foodImage = foodImage;
        this.foodLocation = foodLocation;
    }
    public String getFoodName() { return foodName; }
    public String getFoodLink() { return foodLink; }
    public String getFoodLocation() { return foodLocation; }
    public int getFoodImage() { return foodImage; }
    public void setFoodImage(int foodImage) { this.foodImage = foodImage; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setFoodLink(String foodLink) { this.foodLink = foodLink; }
    public void setFoodLocation(String foodLocation) { this.foodLocation = foodLocation; }
}
