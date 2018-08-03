package com.example.bahaa.marketa.Checkout;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Bahaa on 12/18/2017.
 */
@Entity
public class CheckoutModel implements Serializable{

    @NonNull
    @PrimaryKey
    private String checkTitle;

    private String checkImg;
    private Integer checkQty;

    public CheckoutModel(String checkTitle, Integer checkQty, String checkImg) {
        this.checkTitle = checkTitle;
        this.checkQty = checkQty;
        this.checkImg = checkImg;
    }

    public CheckoutModel() {
        //Required empty constructor
    }

    public String getCheckTitle() {
        return checkTitle;
    }

    public void setCheckTitle(String checkTitle) {
        this.checkTitle = checkTitle;
    }

    public Integer getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(Integer checkQty) {
        this.checkQty = checkQty;
    }

    public String getCheckImg() {
        return checkImg;
    }

    public void setCheckImg(String imageRef) {
        this.checkImg = imageRef;
    }


}
