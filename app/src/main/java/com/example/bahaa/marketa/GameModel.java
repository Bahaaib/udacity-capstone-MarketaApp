package com.example.bahaa.marketa;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bahaa on 12/15/2017.
 */

public class GameModel {

    @SerializedName("imgURL")
    private String imageRef;

    @SerializedName("title")
    private String gameTitle;

    @SerializedName("price")
    private String priceValue;

    @SerializedName("rating")
    private Float rateValue;

    @SerializedName("plot")
    private String plot;

    @SerializedName("releaseDate")
    private String relDate;

    @SerializedName("modes")
    private String mode;

    @SerializedName("developers")
    private String devs;

    @SerializedName("platforms")
    private String platforms;



    public GameModel() {
        //Required Empty Constructor
    }

    public GameModel(String imageRef, String gameTitle, String priceValue, Float rateValue) {
        this.imageRef = imageRef;
        this.gameTitle = gameTitle;
        this.priceValue = priceValue;
        this.rateValue = rateValue;
    }

    public String getImageRef() {
        return imageRef;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getPriceValue() {
        return priceValue;
    }

    public Float getRateValue() {
        return rateValue;
    }

    public String getPlot() {
        return plot;
    }

    public String getRelDate() {
        return relDate;
    }

    public String getMode() {
        return mode;
    }

    public String getDevs() {
        return devs;
    }

    public String getPlatforms() {
        return platforms;
    }
}

