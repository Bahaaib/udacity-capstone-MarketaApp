package com.example.bahaa.marketa.Books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bahaa.marketa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bahaa on 12/19/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Fragment :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<BookModel> adapterModel;

    {
        adapterModel = new ArrayList<>();
    }

    public BookRecyclerAdapter(Context context, ArrayList<BookModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.book_card, parent, false);
        return new BookViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookPrice, bookAuthor;
        ImageView bookCover;


        public BookViewHolder(View itemView) {
            super(itemView);

            bookTitle = (TextView) itemView.findViewById(R.id.bookCardTitle);
            bookAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
            bookPrice = (TextView) itemView.findViewById(R.id.bookPriceValue);

            bookCover = (ImageView) itemView.findViewById(R.id.bookCardImage);


        }

        //Here where all the glory being made..!
        public void BindView(final int position) {

            bookTitle.setText(adapterModel.get(position).getBookTitle());
            bookAuthor.setText(adapterModel.get(position).getBookAuthor());

            String priceStr = adapterModel.get(position).getBookPrice();
            bookPrice.setText(context.getString(R.string.prices_str, priceStr));

            Picasso.with(context)
                    .load(adapterModel.get(position).getImgURL())
                    .into(bookCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Implement some sexy animation in the transition to/from each card to the next Activity.. ;)
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            bookCover, context.getResources().getString(R.string.book_transition)).toBundle();

                    Intent bookIntent = new Intent(context, BookDetailsActivity.class);
                    //Transfer all the card data to the details activity for further use..
                    bookIntent.putExtra(context.getResources().getString(R.string.book_img_xtra), adapterModel.get(position).getImgURL());
                    bookIntent.putExtra(context.getResources().getString(R.string.book_sum_xtra), adapterModel.get(position).getSummary());
                    bookIntent.putExtra(context.getResources().getString(R.string.book_title_xtra), adapterModel.get(position).getBookTitle());
                    bookIntent.putExtra(context.getResources().getString(R.string.book_price_xtra), Float.parseFloat(adapterModel.get(position).getBookPrice()));
                    context.startActivity(bookIntent, bundle);
                }
            });


        }


    }
}

