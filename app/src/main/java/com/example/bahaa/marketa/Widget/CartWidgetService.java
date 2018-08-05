package com.example.bahaa.marketa.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bahaa.marketa.R;

import java.util.ArrayList;

import static com.example.bahaa.marketa.Widget.CartWidgetProvider.cartItemsList;
import static com.example.bahaa.marketa.Widget.CartWidgetProvider.cartQtyList;

public class CartWidgetService extends RemoteViewsService {

    ArrayList<String> remoteViewItemsList;
    ArrayList<String> remoteViewQtyList;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext = null;

        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteViewItemsList = cartItemsList;
            remoteViewQtyList = cartQtyList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewItemsList.size();

        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);

            views.setTextViewText(R.id.widget_list_view_item_title, remoteViewItemsList.get(position));

            views.setTextViewText(R.id.widget_list_view_item_qty, remoteViewQtyList.get(position));


            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_list_view_item_title, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }

}
