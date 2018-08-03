package com.example.bahaa.marketa.Checkout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahaa.marketa.R;
import com.example.bahaa.marketa.Widget.UpdateCartService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.bahaa.marketa.Checkout.CheckoutActivity.cartDatabase;


/**
 * Created by Bahaa on 12/18/2017.
 */

public class CheckoutRecyclerAdapter extends RecyclerView.Adapter {

    private Context cContext;
    private ArrayList<CheckoutModel> checkModel;
    public static boolean isRemoved;
    public static int removePos;

    private ArrayList<String> itemsList, qtyList;


    {
        checkModel = new ArrayList<>();
        itemsList = new ArrayList<>();
        qtyList = new ArrayList<>();


    }

    public CheckoutRecyclerAdapter(Context context, ArrayList<CheckoutModel> adapterModel) {
        this.cContext = context;
        this.checkModel = adapterModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(cContext).inflate(R.layout.checkout_card, parent, false);
        return new CheckoutRecyclerAdapter.CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CheckoutRecyclerAdapter.CheckoutViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return checkModel.size();
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {

        TextView cardTitle, cardQty;
        ImageView cardImage, deleteIcon;


        public CheckoutViewHolder(View itemView) {
            super(itemView);
            cardTitle = (TextView) itemView.findViewById(R.id.checkTitle);
            cardQty = (TextView) itemView.findViewById(R.id.checkQty);
            cardImage = (ImageView) itemView.findViewById(R.id.checkImg);
            deleteIcon = (ImageView) itemView.findViewById(R.id.delImg);


        }

        public void BindView(final int position) {


            cardTitle.setText(checkModel.get(position).getCheckTitle());
            itemsList.add(checkModel.get(position).getCheckTitle());

            cardQty.setText("(" + checkModel.get(position).getCheckQty().toString() + ")");
            qtyList.add(checkModel.get(position).getCheckQty().toString());

            Picasso.with(cContext)
                    .load(checkModel.get(position).getCheckImg())
                    .into(cardImage);

            //Add to Room
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cartDatabase.DaoAccess().insertITEMToDB(checkModel.get(position));
                    Log.i("RoomMSG", "Added to DB");
                    }
            }).start();

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When an item is deleted from the checkout Recyclerview, set the flags to alert Checkout Activity,
                    //Send its exact position in the List
                    //Then delete it from the List
                    isRemoved = true;
                    removePos = position;

                    itemsList.remove(itemsList.get(position));
                    //Remove from Room
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cartDatabase.DaoAccess().deleteItemFromDB(checkModel.get(position));
                            Log.i("RoomMSG", "Removed from DB");
                            }
                    }).start();
                    Toast.makeText(cContext, R.string.item_removed_toast, Toast.LENGTH_LONG).show();

                }
            });

            UpdateCartService.startCartService(cContext, itemsList, qtyList);
        }


    }
}
