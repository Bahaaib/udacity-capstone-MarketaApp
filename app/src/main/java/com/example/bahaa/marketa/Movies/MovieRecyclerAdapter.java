package com.example.bahaa.marketa.Movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bahaa.marketa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bahaa on 12/19/2017.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MovieModel> adapterModel;

    {
        adapterModel = new ArrayList<>();
    }

    public MovieRecyclerAdapter(Context context, ArrayList<MovieModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder  {

        TextView movieTitle, moviePlot, moviePrice;
        ImageView movieCardCover, movieCardSmall;
        View layer_bg;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movieCardTitle);
            moviePlot = (TextView) itemView.findViewById(R.id.movieCardPlot);
            moviePrice = (TextView) itemView.findViewById(R.id.moviePrice);

            movieCardCover = (ImageView) itemView.findViewById(R.id.movieCardImg);
            movieCardSmall = (ImageView) itemView.findViewById(R.id.movieCardSmallImg);
            layer_bg = itemView.findViewById(R.id.vw_blacklayer);

        }

        public void BindView(final int position) {

            movieTitle.setText(adapterModel.get(position).getMovieTitle());
            moviePlot.setText(adapterModel.get(position).getMoviePlot());


            String priceStr = adapterModel.get(position).getMoviePrice();
            moviePrice.setText(context.getString(R.string.prices_str, priceStr));

            Picasso.with(context)
                    .load(adapterModel.get(position).getThumbnailURL())
                    .into(movieCardCover);

            Picasso.with(context)
                    .load(adapterModel.get(position).getImgURL())
                    .into(movieCardSmall);

            //Implement some sexy animation with time.. ;)
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.2f);
            anim.setDuration(1500);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setRepeatMode(Animation.REVERSE);
            layer_bg.startAnimation(anim);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            movieCardCover, context.getResources().getString(R.string.movie_transition)).toBundle();

                    //Transfer all the card data to the details activity for further use..
                    Intent movieIntent = new Intent(context, MovieDetailsActivity.class);
                    movieIntent.putExtra(context.getResources().getString(R.string.mov_plot_xtra), adapterModel.get(position).getMoviePlot());
                    movieIntent.putExtra(context.getResources().getString(R.string.mov_coverimg_xtra),adapterModel.get(position).getThumbnailURL());
                    movieIntent.putExtra(context.getResources().getString(R.string.mov_title_xtra),adapterModel.get(position).getMovieTitle());
                    movieIntent.putExtra(context.getResources().getString(R.string.mov_smallimg_xtra),adapterModel.get(position).getImgURL());
                    movieIntent.putExtra(context.getResources().getString(R.string.mov_price_xtra),Float.parseFloat(adapterModel.get(position).getMoviePrice()));

                    context.startActivity(movieIntent, bundle);



                }
            });


        }

    }
}
