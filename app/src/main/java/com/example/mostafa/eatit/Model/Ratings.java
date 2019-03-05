package com.example.mostafa.eatit.Model;

/**
 * Created by mostafa on 1/6/2018.
 */

public class Ratings {
    private String userPhone;
    private String foodId;
    private String rateValue;
    private String comment;

    public Ratings() {
    }

    public Ratings(String userPhone, String foodId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
