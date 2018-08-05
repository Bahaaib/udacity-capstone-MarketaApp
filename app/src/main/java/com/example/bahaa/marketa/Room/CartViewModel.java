package com.example.bahaa.marketa.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.bahaa.marketa.Checkout.CheckoutModel;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private LiveData<List<CheckoutModel>> cartContent;
    public static CartDatabase cartDatabase;
    private static final String DATABASE_NAME = "cart_db";



    public CartViewModel(@NonNull Application application) {
        super(application);

        cartDatabase = Room.databaseBuilder(application.getApplicationContext(), CartDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        cartContent = cartDatabase.DaoAccess().fetchItemList();
    }

    public LiveData<List<CheckoutModel>> getCartContent() {
        return cartContent;
    }

    //delete item
    public void deleteItem(CheckoutModel model) {
        new deleteAsyncTask(cartDatabase).execute(model);
    }

    //insert item
    public void insertItem(CheckoutModel model){
        new insertAsyncTask(cartDatabase).execute(model);
    }

    //perform deletion in background
    private static class deleteAsyncTask extends AsyncTask<CheckoutModel, Void, Void> {

        private CartDatabase db;

        deleteAsyncTask(CartDatabase cartDatabase) {

            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(CheckoutModel... checkoutModels) {

            db.DaoAccess().deleteItemFromDB(checkoutModels[0]);
            return null;
        }
    }

    //perform insertion in background
    private static class insertAsyncTask extends AsyncTask<CheckoutModel, Void, Void> {

        private CartDatabase db;

        insertAsyncTask(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(final CheckoutModel... checkoutModels) {
            db.DaoAccess().insertITEMToDB(checkoutModels[0]);
            return null;
        }
    }
}
