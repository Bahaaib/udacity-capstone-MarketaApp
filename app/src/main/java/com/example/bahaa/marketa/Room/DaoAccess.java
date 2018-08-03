package com.example.bahaa.marketa.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.bahaa.marketa.Checkout.CheckoutModel;

import java.util.List;

@Dao
public interface DaoAccess {


    @Insert
    void insertITEMToDB(CheckoutModel model);

    @Query("SELECT * FROM CheckoutModel WHERE checkTitle = :checkTitle")
    CheckoutModel fetchItemById(String checkTitle);

    @Query("SELECT * FROM CheckoutModel")
    List<CheckoutModel> fetchItemList();

    @Update
    void updateItemOnDB(CheckoutModel model);

    @Delete
    void deleteItemFromDB(CheckoutModel model);
}
