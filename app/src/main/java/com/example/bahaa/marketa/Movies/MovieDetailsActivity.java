package com.example.bahaa.marketa.Movies;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bahaa.marketa.AboutActivity;
import com.example.bahaa.marketa.Auth.LoginActivity;
import com.example.bahaa.marketa.Checkout.CheckoutActivity;
import com.example.bahaa.marketa.Checkout.CheckoutModel;
import com.example.bahaa.marketa.CreditActivity;
import com.example.bahaa.marketa.MainActivity;
import com.example.bahaa.marketa.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static com.example.bahaa.marketa.MainActivity.itemsList;


public class MovieDetailsActivity extends AppCompatActivity {

    public ImageView movieCoverImg;
    public CardView movieDetailsCard;
    public TextView moviePlot;
    public TextView movieCart;
    public PopupWindow popWindow;

    public EditText quantity, voucher;
    public TextView proceed;

    public Float moviePrice = 0.0f;

    public Integer itemQty = 1;

    public Float movieFinalPrice;

    public String voucherStr;

    public Float disFactor;

    Context context = MovieDetailsActivity.this;


    RelativeLayout relativeLayout;

    //Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //AdMob
    private String DEVICE_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //AdMob
        DEVICE_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        relativeLayout = (RelativeLayout) findViewById(R.id.detRelativeLayout);
        final Intent intent = getIntent();

        movieCoverImg = (ImageView) findViewById(R.id.movieDetCoverImg);
        movieDetailsCard = (CardView) findViewById(R.id.movieDetailsCard);


        movieCart = (TextView) findViewById(R.id.movieCart);
        moviePlot = (TextView) findViewById(R.id.moviePlot);

        //Starting card Animation Setup
        AnimatorSet animationSet = new AnimatorSet();

        //Translating Card in Y Scale
        ObjectAnimator movieCard = ObjectAnimator.ofFloat(movieDetailsCard, View.TRANSLATION_Y, 70);
        movieCard.setDuration(2500);
        movieCard.setRepeatMode(ValueAnimator.REVERSE);
        movieCard.setRepeatCount(ValueAnimator.INFINITE);
        movieCard.setInterpolator(new LinearInterpolator());


        animationSet.play(movieCard);
        animationSet.start();

        Picasso.with(context)
                .load(intent.getStringExtra(getString(R.string.mov_coverimg_xtra)))
                .into(movieCoverImg);
        moviePlot.setText(intent.getStringExtra(getString(R.string.mov_plot_xtra)));

        //Poping up the Purchasing info Window on clicking the text..
        movieCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onShowPopup(view);
            }
        });


        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nv);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent drawerIntent;
                switch (id) {
                    case R.id.home:
                        drawerIntent = new Intent(MovieDetailsActivity.this, MainActivity.class);
                        startActivity(drawerIntent);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(MovieDetailsActivity.this, AboutActivity.class));
                        return true;
                    case R.id.credit:
                        startActivity(new Intent(MovieDetailsActivity.this, CreditActivity.class));
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

        //Launch AdMob
        adView();


    }


    //Here We describe everything about the Popup Window
    //Also declaring all its views here, NOTE: Not in the Activity above; They have different contexts!
    public void onShowPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.popwin_layout, null, false);

        RelativeLayout headerView = (RelativeLayout) inflatedView.findViewById(R.id.headerLayout);

        quantity = (EditText) inflatedView.findViewById(R.id.quantity);


        proceed = (TextView) inflatedView.findViewById(R.id.proceed);



        moviePrice = getIntent().getFloatExtra(getString(R.string.mov_price_xtra), 0);


        //Get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //set height depends on the device size making it responsive to almost all devices
        popWindow = new PopupWindow(inflatedView, width - 50, height - 50, true);

        //set a background drawable with round corners
        popWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_bg));

        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //Set The Springy Animation style from the script in styles..
        popWindow.setAnimationStyle(R.style.PopupAnimation);

        //Show the popup at bottom of the screen and set some margin at the bottom
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 200);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validCoupon, validQty;
                Intent checkoutIntent = new Intent(MovieDetailsActivity.this, CheckoutActivity.class);

                //Find out the user input in Quantity field considering the input is 0 if left blank
                try {
                    itemQty = Integer.parseInt(quantity.getText().toString());

                } catch (NumberFormatException e) {
                    itemQty = 0;
                }
                movieFinalPrice = moviePrice * itemQty;

                voucher = (EditText) inflatedView.findViewById(R.id.voucher);
                voucherStr = voucher.getText().toString();

                //Check Voucher Coupon validation among 2 Coupons available OR no Coupon is OK!
                if (voucherStr.equals(getString(R.string.voucher_off50))) {
                    disFactor = 0.5f;
                    validCoupon = true;


                } else if (voucherStr.equals(getString(R.string.voucher_off25))) {
                    disFactor = 0.25f;
                    validCoupon = true;

                } else if (voucherStr.equals("")) {
                    disFactor = 0.0f;
                    validCoupon = true;
                } else {
                    validCoupon = false;
                }
                if (itemQty <= 0) {
                    validQty = false;
                } else {
                    validQty = true;
                }
                //Showing the snackbar upon the above fields states..
                if (!validQty && !validCoupon) {
                    createSnackbar(getString(R.string.snack_invalid));

                } else if (!validQty) {
                    createSnackbar(getString(R.string.snack_qty));
                } else if (!validCoupon && validQty){
                    createSnackbar(getString(R.string.snack_outdate));
                }

                //Freeze the Popup window preventing it from moving to checkout if any field input is invalid!
                if (validCoupon && validQty) {
                    CheckoutModel model = new CheckoutModel();
                    model.setCheckImg(getIntent().getStringExtra(getString(R.string.mov_smallimg_xtra)));
                    model.setCheckTitle(getIntent().getStringExtra(getString(R.string.mov_title_xtra)));
                    model.setCheckQty(itemQty);


                    itemsList.add(model);

                    checkoutIntent.putExtra(getString(R.string.item_list_xtra), itemsList);
                    checkoutIntent.putExtra(getString(R.string.movie_fprice_xtra), movieFinalPrice);
                    checkoutIntent.putExtra(getString(R.string.voucher_xtra), disFactor);

                    startActivity(checkoutIntent);

                }

            }
        });

    }

    //Snackbar Structure
    public void createSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        TextView snackbarText = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackbarText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();

    }

    //if user logged out, destroy the Activity with no way back immediately..
    public void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MovieDetailsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void adView() {
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(DEVICE_ID)
                .build();
        mAdView.loadAd(adRequest);
    }

}