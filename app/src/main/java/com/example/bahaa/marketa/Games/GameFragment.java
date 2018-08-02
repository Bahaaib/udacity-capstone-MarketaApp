package com.example.bahaa.marketa.Games;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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


public class GameFragment extends Fragment {

    public ArrayList<GameModel> marketGames;
    public RecyclerView recyclerView;
    GameRecyclerAdapter adapter;
    GridLayoutManager gridLayoutManager;

    //Volley
    private RequestQueue requestQueue;
    public static ArrayList<String> strList;
    private String API_URL;


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        API_URL = getActivity().getResources().getString(R.string.game_api_url);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        //Volley
        requestQueue = Volley.newRequestQueue(getActivity());
        strList = new ArrayList<>();

        loadGames();

        // Main list that holds all the games cards Objects to convey them later to the RecyclerView
        marketGames = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.gameRecyclerView);


        //Passing the full list to the RecyclerView adapter to show them,
        // Passing the Activity context too letting the adapter know which Activity is calling in the whole App
        adapter = new GameRecyclerAdapter(this.getActivity(), marketGames);
        recyclerView.setAdapter(adapter);

        //Showing the RecyclerView Elements using the GridView Scheme, 2 Cards in each row, propagating vertically,
        //Wrapping all passed cards with no limit
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);


        //Now, ShowTime... :)
        return v;
    }

    private void loadGames() {

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
                                GameModel game = gson.fromJson(mainStr, GameModel.class);


                                Log.i("urls", game.getImageRef());


                                marketGames.add(game);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Notify the RecyclerView with the new data..
                        adapter.notifyDataSetChanged();

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
