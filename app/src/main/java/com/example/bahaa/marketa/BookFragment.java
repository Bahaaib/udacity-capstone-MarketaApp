package com.example.bahaa.marketa;

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
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class BookFragment extends Fragment {

    public ArrayList<BookModel> marketBooks;
    public RecyclerView bookRV;
    BookRecyclerAdapter bookAdapter;
    GridLayoutManager gridLayoutManager;

    //Volley
    private RequestQueue requestQueue;
    public static ArrayList<String> strList;
    private String API_URL = "https://api.myjson.com/bins/14cqju";


    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_book, container, false);

        //Volley
        requestQueue = Volley.newRequestQueue(getActivity());
        strList = new ArrayList<>();

        loadBooks();

        marketBooks = new ArrayList<>();
        bookRV = (RecyclerView) v.findViewById(R.id.bookRecyclerView);



        bookAdapter = new BookRecyclerAdapter(this.getActivity(), marketBooks);
        bookRV.setAdapter(bookAdapter);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        bookRV.setLayoutManager(gridLayoutManager);

        return v;
    }

    private void loadBooks() {

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
                                BookModel book = gson.fromJson(mainStr, BookModel.class);


                                Log.i("titles", book.getBookTitle());


                                marketBooks.add(book);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Notify the RecyclerView with the new data..
                        bookAdapter.notifyDataSetChanged();

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
