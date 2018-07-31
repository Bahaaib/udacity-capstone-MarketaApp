package com.example.bahaa.marketa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bahaa.marketa.Auth.LoginActivity;
import com.example.bahaa.marketa.Checkout.CheckoutModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public TabLayout tabLayout;
    public static Float totGamePrice, totMoviePrice, totBookPrice, subTotal, discount, grandTotal;

    static {
        totGamePrice = 0.0f;
        totMoviePrice = 0.0f;
        totBookPrice = 0.0f;
        subTotal = 0.0f;
        discount = 0.0f;
        grandTotal = 0.0f;
    }

    //Static items ArrayList to be guaranteed alive anytime at app navigation lifetime storing purchased items
    public static ArrayList<CheckoutModel> itemsList = new ArrayList<>();
    // Same idea for items prices
    public static ArrayList<Float> pricesList = new ArrayList<>();

    //Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    //Exit activity
    private boolean backPressedTwice;
    private Handler backHandler = new Handler();
    private final Runnable backRunnable = new Runnable() {
        @Override
        public void run() {
            backPressedTwice = false;
        }
    };
    private Toast backPressToast;

    //Firebase Analytics
    private FirebaseAnalytics firebaseAnalytics;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Getting last value saved for static variables before destroying activity
        if (savedInstanceState != null) {
            totGamePrice = savedInstanceState.getFloat("game");
            totMoviePrice = savedInstanceState.getFloat("movie");
            totBookPrice = savedInstanceState.getFloat("book");
            subTotal = savedInstanceState.getFloat("subTotal");
            discount = savedInstanceState.getFloat("discount");
            grandTotal = savedInstanceState.getFloat("grand");

            itemsList = (ArrayList) savedInstanceState.getSerializable("cartList");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Assigning all used objects to their views
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        //Adding Three tabs to the screen
        tabLayout.addTab(tabLayout.newTab().setText("Games"));
        tabLayout.addTab(tabLayout.newTab().setText("Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("Books"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Setting up the View Pager that allows flipping activity fragments horizontally like a page
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nv);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "You're Already in Home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.about:
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.credit:
                        Toast.makeText(MainActivity.this, "Credit", Toast.LENGTH_SHORT).show();
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

    // Temporarily saving our data for future use
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putFloat("game", totGamePrice);
        savedInstanceState.putFloat("movie", totMoviePrice);
        savedInstanceState.putFloat("book", totBookPrice);
        savedInstanceState.putFloat("subTotal", subTotal);
        savedInstanceState.putFloat("discount", discount);
        savedInstanceState.putFloat("grand", grandTotal);

        savedInstanceState.putSerializable("cartList", itemsList);
    }


    // Clear everything manually on Back is pressed
    @Override
    public void onBackPressed() {
        totGamePrice = 0.0f;
        totMoviePrice = 0.0f;
        totBookPrice = 0.0f;
        subTotal = 0.0f;
        discount = 0.0f;
        grandTotal = 0.0f;
        itemsList.clear();
        pricesList.clear();

        //Plugging a handler to wait for 1 second before receiving a confirmation to destroy Activity
        if (backPressedTwice) {
            Intent exitIntent = new Intent(Intent.ACTION_MAIN);
            exitIntent.addCategory(Intent.CATEGORY_HOME);
            exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exitIntent);

        }
        backPressedTwice = true;
        //Prevent the toast from appearing twice
        if (backPressToast != null) {
            backPressToast.cancel();
            backPressToast = null;
        } else {
            backPressToast = Toast.makeText(getApplicationContext(), R.string.backPressExit, Toast.LENGTH_SHORT);
            backPressToast.show();
        }
        backHandler.postDelayed(backRunnable, 1000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Release the Handler and its runnable from the memory on Activity destruction
        if (backHandler != null) {
            backHandler.removeCallbacks(backRunnable);
        }
    }

    //if user logged out, destroy the MainActivity with no way back immediately..
    public void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
