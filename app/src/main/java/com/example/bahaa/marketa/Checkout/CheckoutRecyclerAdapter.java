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
import java.util.List;

import static com.example.bahaa.marketa.Checkout.CheckoutActivity.cartViewModel;
import static com.example.bahaa.marketa.Checkout.CheckoutActivity.items;
import static com.example.bahaa.marketa.Checkout.CheckoutActivity.qty;
import static com.example.bahaa.marketa.MainActivity.itemsList;


/**
 * Created by Bahaa on 12/18/2017.
 */

public class CheckoutRecyclerAdapter extends RecyclerView.Adapter {

    private Context cContext;
    private List<CheckoutModel> checkModel;
    public static boolean isRemoved;
    public static int removePos;

    private ArrayList<String> mItemsList, qtyList;


    {
        checkModel = new ArrayList<>();

    }

    public CheckoutRecyclerAdapter(Context context, List<CheckoutModel> adapterModel) {
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

    public void addItems(List<CheckoutModel> adapterList) {
        this.checkModel = adapterList;
        notifyDataSetChanged();
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

            String quantity = checkModel.get(position).getCheckQty().toString();
            cardQty.setText(cContext.getString(R.string.chk_qty_str, quantity));

            Picasso.with(cContext)
                    .load(checkModel.get(position).getCheckImg())
                    .into(cardImage);

            Log.i("CartSize", String.valueOf(getItemCount()));


            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When an item is deleted from the checkout Recyclerview, set the flags to alert Checkout Activity,
                    //Send its exact position in the List
                    //Then delete it from the List
                    isRemoved = true;
                    removePos = position;

                    //mItemsList.remove(mItemsList.get(position));

                    cartViewModel.deleteItem(checkModel.get(position));

                    itemsList.remove(checkModel.get(position));

                    Toast.makeText(cContext, R.string.item_removed_toast, Toast.LENGTH_LONG).show();

                    items.remove(position);
                    qty.remove(position);

                    UpdateCartService.startCartService(cContext, items, qty);


                }
            });


        }


    }
}
