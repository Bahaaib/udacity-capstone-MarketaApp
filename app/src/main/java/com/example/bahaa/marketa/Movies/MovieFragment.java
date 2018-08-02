package com.example.bahaa.marketa.Movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bahaa.marketa.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MovieFragment extends Fragment {

    public ArrayList<MovieModel> marketMovies;
    public RecyclerView movieRV;
    MovieRecyclerAdapter movieAdapter;
    LinearLayoutManager linearLayoutManager;

    //Volley
    private RequestQueue requestQueue;
    public static ArrayList<String> strList;
    private String API_URL;


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        API_URL = getContext().getResources().getString(R.string.movie_api_url);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);

        //Volley
        requestQueue = Volley.newRequestQueue(getActivity());
        strList = new ArrayList<>();

        loadMovies();

        marketMovies = new ArrayList<>();
        movieRV = (RecyclerView) v.findViewById(R.id.movieRecyclerView);



        movieAdapter = new MovieRecyclerAdapter(this.getActivity(), marketMovies);
        movieRV.setAdapter(movieAdapter);

        //Showing the RecyclerView Elements using the Linear Scheme, 1 Card in each row, propagating vertically,
        //Wrapping all passed cards with no limit
        linearLayoutManager = new LinearLayoutManager(getActivity());

        movieRV.setLayoutManager(linearLayoutManager);




        return v;
    }

    private void loadMovies() {

        StringRequest request = new StringRequest(Request.Method.GET, API_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Statuss", "Got Response!");


                        // Initialize Gson and start new transaction
                        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return false;
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        }).create();

                        Log.i("titles", response);

                        try {
                            JSONArray mainArray = new JSONArray(response);



                            for (int i = 0; i < mainArray.length(); i++) {
                                String mainStr = mainArray.get(i).toString();
                                strList.add(mainStr);
                                MovieModel movie = gson.fromJson(mainStr, MovieModel.class);


                                Log.i("titles", movie.getMovieTitle());


                                marketMovies.add(movie);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Notify the RecyclerView with the new data..
                        movieAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener()


        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        requestQueue.add(request);

    }





}
