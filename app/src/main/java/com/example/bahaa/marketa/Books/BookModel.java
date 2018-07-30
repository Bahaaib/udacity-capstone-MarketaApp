package com.example.bahaa.marketa.Books;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bahaa on 12/19/2017.
 */

public class BookModel {

    @SerializedName("title")
    private String bookTitle;

    @SerializedName("price")
    private String bookPrice;

    @SerializedName("author")
    private String bookAuthor;

    @SerializedName("plot")
    private String summary;

    @SerializedName("imgURL")
    private String imgURL;


    public BookModel() {
        //Required Empty Constructor
    }

    public BookModel(String bookTitle, String bookPrice, String bookAuthor, String summary, String imgURL) {
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.bookAuthor = bookAuthor;
        this.summary = summary;
        this.imgURL = imgURL;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getSummary() {
        return summary;
    }

    public String getImgURL() {
        return imgURL;
    }
}
