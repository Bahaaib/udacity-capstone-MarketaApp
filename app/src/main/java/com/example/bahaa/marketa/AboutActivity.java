package com.example.bahaa.marketa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bahaa.marketa.Auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AboutActivity extends AppCompatActivity {

    private TextView aboutTV;

    //Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutTV = (TextView) findViewById(R.id.about_tv);

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.about_action_bar);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nv);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(AboutActivity.this, MainActivity.class));
                        return true;
                    case R.id.credit:
                        startActivity(new Intent(AboutActivity.this, CreditActivity.class));
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

    //if user logged out, destroy the Activity with no way back immediately..
    public void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
