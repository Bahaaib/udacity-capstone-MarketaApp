package com.example.bahaa.marketa.Checkout;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bahaa.marketa.AboutActivity;
import com.example.bahaa.marketa.Auth.LoginActivity;
import com.example.bahaa.marketa.CreditActivity;
import com.example.bahaa.marketa.MainActivity;
import com.example.bahaa.marketa.R;
import com.example.bahaa.marketa.Room.CartViewModel;
import com.example.bahaa.marketa.Widget.UpdateCartService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static com.example.bahaa.marketa.Checkout.CheckoutRecyclerAdapter.isRemoved;
import static com.example.bahaa.marketa.Checkout.CheckoutRecyclerAdapter.removePos;
import static com.example.bahaa.marketa.MainActivity.discount;
import static com.example.bahaa.marketa.MainActivity.grandTotal;
import static com.example.bahaa.marketa.MainActivity.itemsList;
import static com.example.bahaa.marketa.MainActivity.pricesList;
import static com.example.bahaa.marketa.MainActivity.subTotal;


public class CheckoutActivity extends AppCompatActivity {

    public RecyclerView checkuotRV;
    public CheckoutRecyclerAdapter checkoutAdapter;
    public LinearLayoutManager linearLayoutManager;
    public TextView subPrice, discountPrice, grandPrice;

    public Float discountFactor;
    public Float gPrice, mPrice, bPrice;

    public static ArrayList<String> items, qty;



    public SwipeRefreshLayout updateRefresher;

    //Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    //Room DB
    private static final String DATABASE_NAME = "cart_db";
    //public static CartDatabase cartDatabase;
    public static CartViewModel cartViewModel;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);



        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        items = new ArrayList<>();
        qty = new ArrayList<>();


        //Assigning all used objects to their views
        updateRefresher = (SwipeRefreshLayout) findViewById(R.id.updateRefresher);

        checkuotRV = (RecyclerView) findViewById(R.id.checkoutRV);


        checkoutAdapter = new CheckoutRecyclerAdapter(this, itemsList);

        checkuotRV.setAdapter(checkoutAdapter);

        linearLayoutManager = new LinearLayoutManager(this);

        checkuotRV.setLayoutManager(linearLayoutManager);


        //Room Live data
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getCartContent().observe(CheckoutActivity.this, new Observer<List<CheckoutModel>>() {
            @Override
            public void onChanged(@Nullable List<CheckoutModel> checkoutModels) {
                checkoutModels = itemsList;
                checkoutAdapter.addItems(checkoutModels);
                Log.i("RoomMSG", "Observed DB Change!");
            }
        });

        for (CheckoutModel dbItem : itemsList){
            cartViewModel.insertItem(dbItem);
        }

        for (CheckoutModel widgetItem : itemsList){
            items.add(widgetItem.getCheckTitle());
            qty.add(String.valueOf(widgetItem.getCheckQty()));
        }
        UpdateCartService.startCartService(this, items, qty);


        subPrice = (TextView) findViewById(R.id.subPrice);
        discountPrice = (TextView) findViewById(R.id.disPrice);
        grandPrice = (TextView) findViewById(R.id.grandTotalPrice);

        //Gather all prices data from sending classes for final calculation
        gPrice = getIntent().getFloatExtra("gameFinalPrice", 0);
        mPrice = getIntent().getFloatExtra("movieFinalPrice", 0);
        bPrice = getIntent().getFloatExtra("bookFinalPrice", 0);


        //Fill the prices list with the category purchased!
        // THE TRICK: There will always be one category at a time has a price and the others 0's
        if (gPrice > 0) {
            pricesList.add(gPrice);
        } else if (mPrice > 0) {
            pricesList.add(mPrice);
        } else {
            pricesList.add(bPrice);
        }

        //Clearing the subtotal price everytime logging in to the activity to prevent previous carts accumulation
        subTotal = 0.0f;

        //Calcualting subtotal by adding all list prices
        for (int i = 0; i < pricesList.size(); i++) {
            subTotal += pricesList.get(i);
        }

        //Gather voucher Coupon if found,
        //If not, by default would calculate no discounts
        discountFactor = getIntent().getFloatExtra("vCoupon", 1);

        discount = (subTotal * discountFactor);
        grandTotal = subTotal - discount;

        //Showing current purchased items receipt
        subPrice.setText(String.format("%.2f", subTotal) + "$");
        discountPrice.setText(" - " + String.format("%.2f", discount) + "$");
        grandPrice.setText(String.format("%.2f", grandTotal) + "$");



        //Setting up A Swipe refresher to update the UI corresponding to any change occurs in data
        updateRefresher.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));

        updateRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //If item removed, Re-Calcualte the receipt..
                if (isRemoved) {
                    subTotal -= pricesList.get(removePos);
                    pricesList.remove(removePos);
                    discount = (subTotal * discountFactor);
                    grandTotal = subTotal - discount;
                    isRemoved = false;

                }

                //Show the new receipt
                subPrice.setText(String.format("%.2f", subTotal) + "$");
                discountPrice.setText(" - " + String.format("%.2f", discount) + "$");
                grandPrice.setText(String.format("%.2f", grandTotal) + "$");

                //And notify RecyclerView Adapter to remove the corresponding card up from the UI
                checkoutAdapter.notifyDataSetChanged();

                //Let the circular disappear jumping back up after updating
                updateRefresher.setRefreshing(false);
            }
        });

        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.shopping_cart);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nv);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent drawerIntent;
                switch (id) {
                    case R.id.home:
                        drawerIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                        startActivity(drawerIntent);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(CheckoutActivity.this, AboutActivity.class));
                        return true;
                    case R.id.credit:
                        startActivity(new Intent(CheckoutActivity.this, CreditActivity.class));
                        return true;
                    case R.id.logout:
                        mAuth.signOut();
                        updateUI();
                        return true;
                    default:
                        return true;
                }
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // Return back to MainActivity on Back Pressed,
    //Clear the back Stack due to start the App flow normally from the start!
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(CheckoutActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        super.onBackPressed();
    }

    //if user logged out, destroy the Activity with no way back immediately..
    public void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(CheckoutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
