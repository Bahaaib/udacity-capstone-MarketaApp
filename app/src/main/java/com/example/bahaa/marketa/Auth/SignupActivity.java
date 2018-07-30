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

public class SignupActivity extends AppCompatActivity {

    private TextView alredayMemText;
    private Button signUpButton;
    private EditText userName, signMail, signPass;
    private Drawable errorIcon;
    private String usernameRes, mailRes, passRes;
    private FirebaseAuth nUserAuth;
    private FirebaseAuth.AuthStateListener nUserAuthListener;
    private boolean successfulSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Prevent keyboard from automatic popping up once onCreate fragment..
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Establishing Firebase connection..
        nUserAuth = FirebaseAuth.getInstance();
        nUserAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                } else {
                    //Action to be taken
                }
            }
        };

        alredayMemText = (TextView) findViewById(R.id.alreadyMem);
        alredayMemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            }
        });

        userName = (EditText) findViewById(R.id.userName);
        signMail = (EditText) findViewById(R.id.signMail);
        signPass = (EditText) findViewById(R.id.signPass);


        signUpButton = (Button) findViewById(R.id.signUpButton);
        errorIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_error);


        signUpButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                final String userNameStr = userName.getText().toString();
                final String signMailStr = signMail.getText().toString();
                final String signPassStr = signPass.getText().toString();
                errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
                successfulSignUp = false;


                if (!isValidUsername(userNameStr)) {
                    userName.setError(getString(R.string.invalidUsername), errorIcon);
                    successfulSignUp = false;
                } else {
                    userName.setError(null);
                    //send valid data
                    usernameRes = userNameStr;
                    successfulSignUp = true;
                }


                if (!isValidEmail(signMailStr)) {

                    signMail.setError(getString(R.string.invalidForm), errorIcon);
                    successfulSignUp = false;
                } else {//send valid data
                    mailRes = signMailStr;
                    successfulSignUp = true;
                }

                if (!isValidPassword(signPassStr)) {
                    signPass.setError(getString(R.string.passCharLess), errorIcon);
                    successfulSignUp = false;
                } else {//send valid data
                    passRes = signPassStr;
                    successfulSignUp = true;
                }

                if (!successfulSignUp) {
                    Toast.makeText(getApplicationContext(), R.string.signUpDataProblem, Toast.LENGTH_LONG)
                            .show();
                } else {
                    onSignUp(mailRes, passRes);
                    //Check sent data on console
                    Log.i("Result", usernameRes + mailRes + passRes);
                }

            }


        });
    }

    // validating Username
    private boolean isValidUsername(String userName) {
        if (!TextUtils.isEmpty(userName) && (userName.length() > 2)) {
            return true;
        }
        return false;
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

    //Pass Email & Password parameters to firebase for the 1st time..

    private void onSignUp(String mailStr, String passStr) {
        nUserAuth.createUserWithEmailAndPassword(mailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.signUpFailed, Toast.LENGTH_LONG)
                                    .show();
                        }else {
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

                        }
                    }
                });
    }

}
