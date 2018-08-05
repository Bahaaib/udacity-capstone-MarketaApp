package com.example.bahaa.marketa.Widget;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class UpdateCartService extends IntentService {
    public static String ITEMS_LIST = "ITEMS_LIST";
    public static String QTY_LIST = "QUANTITY_LIST";

    private String channelId = "cart_widget";
    private String channelName = "widget_channel";
    private final int imp = 0;


    public UpdateCartService() {
        super("UpdateCartService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Log.i("widgetMSG", "Service onCreate!");
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, imp);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        startForeground(1, new NotificationCompat.Builder(this, channelId).build());
    }

    public static void startCartService(Context context, ArrayList<String> itemsList, ArrayList<String> qtyList) {
        Intent intent = new Intent(context, UpdateCartService.class);
        intent.putExtra(ITEMS_LIST, itemsList);
        intent.putExtra(QTY_LIST, qtyList);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.i("widgetMSG", "I'm on Oreo");
            context.startForegroundService(intent);

        } else {
            Log.i("widgetMSG", "I'm on Nougat");
            context.startService(intent);
        }
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
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(ITEMS_LIST, itemsList);
        intent.putExtra(QTY_LIST, qtyList);

        if (itemsList != null) {
            sendBroadcast(intent);
        }
        Log.i("widgetMSG", "Broad Sent!");

        stopSelf();
    }

}
