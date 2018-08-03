package com.example.bahaa.marketa.Room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.bahaa.marketa.Checkout.CheckoutModel;

@Database(entities = {CheckoutModel.class},
        version = 1,
        exportSchema = false)
public abstract class CartDatabase extends RoomDatabase{
    public abstract DaoAccess DaoAccess();

}
