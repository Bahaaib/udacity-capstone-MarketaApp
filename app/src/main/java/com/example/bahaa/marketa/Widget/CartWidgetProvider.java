package com.example.bahaa.marketa.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.bahaa.marketa.Checkout.CheckoutActivity;
import com.example.bahaa.marketa.R;

import java.util.ArrayList;

import static com.example.bahaa.marketa.Widget.UpdateCartService.ITEMS_LIST;
import static com.example.bahaa.marketa.Widget.UpdateCartService.QTY_LIST;

public class CartWidgetProvider extends AppWidgetProvider{

    static ArrayList<String> cartItemsList = new ArrayList<>();
    static ArrayList<String> cartQtyList = new ArrayList<>();



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cart_widget);

        //call activity when widget is clicked, but resume activity from stack so you do not pass intent.extras afresh
        Intent appIntent = new Intent(context, CheckoutActivity.class);
        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        // Set the IngWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, CartWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    public static void updateCartWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CartWidgetProvider.class));

        final String action = intent.getAction();

        if (action.equals(context.getString(R.string.widget_action))) {
            cartItemsList = intent.getExtras().getStringArrayList(ITEMS_LIST);
            cartQtyList = intent.getExtras().getStringArrayList(QTY_LIST);


            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            //Now update all widgets
            CartWidgetProvider.updateCartWidgets(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }

}
