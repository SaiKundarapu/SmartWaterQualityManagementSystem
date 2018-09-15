package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserLogin extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    boolean isInternet;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        inputEmail=findViewById(R.id.uname);
        inputPassword=findViewById(R.id.upass);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isInternetConnected()) {
            isInternet=true;
            showSnack(false);
        }
        else {
            isInternet=true;
        }
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(UserLogin.this, UserHome.class));
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        InternetStatus.getInstance().setConnectivityListener(this);
    }

    public void forgotPassword(View view) {
        //startActivity(new Intent(LoginPage.this, ForgotPasswordActivity.class));
    }

    public void signin(View view) {
        if(isInternet) {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            //authenticate user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if(!isInternetConnected())
                        {
                            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UserLogin.this, "Authentication failed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(UserLogin.this, "successfully login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserLogin.this, UserHome.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view)
    {
        if(isInternet) {
            startActivity(new Intent(UserLogin.this, UserRegistration.class));
        }
        else
        {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }

    private void showSnack(boolean isConnected) {
        String message;
        Snackbar snackbar;
        if (isConnected) {
            isInternet=true;
            message = "Internet Connected";
            snackbar = Snackbar.make(findViewById(R.id.cl2), message, Snackbar.LENGTH_SHORT);
        } else {
            isInternet=false;
            message = "No Active Internet";
            snackbar = Snackbar.make(findViewById(R.id.cl2), message, Snackbar.LENGTH_INDEFINITE);
        }
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}