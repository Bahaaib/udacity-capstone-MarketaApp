package com.example.bahaa.marketa.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class UpdateCartService extends IntentService {
    public static String ITEMS_LIST = "ITEMS_LIST";
    public static String QTY_LIST = "QUANTITY_LIST";


    public UpdateCartService() {
        super("UpdateIngService");
    }

    public static void startCartService(Context context, ArrayList<String> itemsList, ArrayList<String> qtyList) {
        Intent intent = new Intent(context, UpdateCartService.class);
        intent.putExtra(ITEMS_LIST, itemsList);
        intent.putExtra(QTY_LIST, qtyList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> itemsList = intent.getExtras().getStringArrayList(ITEMS_LIST);
            ArrayList<String> qtyList = intent.getExtras().getStringArrayList(QTY_LIST);

            handleActionUpdateCartWidgets(itemsList, qtyList);

        }
    }


    private void handleActionUpdateCartWidgets(ArrayList<String> itemsList, ArrayList<String> qtyList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(ITEMS_LIST, itemsList);
        intent.putExtra(QTY_LIST, qtyList);

        sendBroadcast(intent);
    }

}
