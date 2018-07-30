package com.example.bahaa.marketa.Movies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bahaa on 12/19/2017.
 */

public class MovieModel {


    @SerializedName("imgURL")
    private String imgURL;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("plot")
    private String moviePlot;

    @SerializedName("price")
    private String moviePrice;



    public MovieModel() {
        //Required Empty constructor
    }

    public MovieModel(String imgURL, String thumbnailURL, String movieTitle, String moviePlot, String moviePrice) {
        this.imgURL = imgURL;
        this.thumbnailURL = thumbnailURL;
        this.movieTitle = movieTitle;
        this.moviePlot = moviePlot;
        this.moviePrice = moviePrice;
    }


    public String getImgURL() {
        return imgURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public String getMoviePrice() {
        return moviePrice;
    }
}
