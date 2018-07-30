package com.example.bahaa.marketa.Auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bahaa.marketa.MainActivity;
import com.example.bahaa.marketa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpText;
    private EditText loginMail, loginPassword;
    private Button loginButton;
    private Drawable errorIcon;
    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener userAuthListener;
    private String mailRes, passwordRes;
    private boolean successfulLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Prevent keyboard from automatic popping up once onCreate fragment..
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Establishing Connection with firebase..
        userAuth = FirebaseAuth.getInstance();

        userAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    // Action to be taken..
                }
            }
        };

        signUpText = (TextView) findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });


        loginMail = (EditText) findViewById(R.id.loginMail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);


        loginButton = (Button) findViewById(R.id.login);
        errorIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_error);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                successfulLogin = false;
                final String loginMailStr = loginMail.getText().toString();
                final String loginPasswordStr = loginPassword.getText().toString();
                errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());


                if (!isValidEmail(loginMailStr)) {
                    loginMail.setError(getString(R.string.invalidMail), errorIcon);
                    successfulLogin = false;

                } else {//send valid data
                    successfulLogin = true;
                    mailRes = loginMailStr;
                }

                if (!isValidPassword(loginPasswordStr)) {
                    successfulLogin = false;
                    loginPassword.setError(getString(R.string.passCharLess), errorIcon);
                } else {//Send valid Data
                    successfulLogin = true;
                    passwordRes = loginPasswordStr;


                }
                if (!successfulLogin) {
                    Toast.makeText(getApplicationContext(), R.string.loginDataProblem, Toast.LENGTH_LONG).show();
                } else {
                    onLogIn(mailRes, passwordRes);
                }


            }
        });

    }

    // validating email id
    private boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (!TextUtils.isEmpty(pass) && pass.length() > 7) {
            return true;
        }
        return false;
    }

    private void onLogIn(String mailStr, String passwordStr) {
        userAuth.signInWithEmailAndPassword(mailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = userAuth.getCurrentUser();
                            Log.i("AuthState", "Logged in Successfully!");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //Log in..
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.logInFailed, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
