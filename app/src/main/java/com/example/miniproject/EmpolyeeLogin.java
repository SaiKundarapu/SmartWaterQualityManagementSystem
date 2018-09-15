package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmpolyeeLogin extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    boolean isInternet;
    DatabaseReference db;
    EditText e1,e2;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empolyee_login);
        e1=findViewById(R.id.euser);
        e2=findViewById(R.id.epass);
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        InternetStatus.getInstance().setConnectivityListener(this);
    }
    protected void empLogin(View v)
    {
        if(isInternet) {
            db = FirebaseDatabase.getInstance().getReference("employee");
            Query userval = db.orderByChild("username").equalTo(e1.getText().toString());
            userval.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Toast.makeText(getBaseContext(), "Successfully login", Toast.LENGTH_SHORT).show();

                    /*Intent i=new Intent(MainActivity.this,profilePage.class);
                    startActivity(i);*/
                    } else {
                        Toast.makeText(getBaseContext(), "enter valid details", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();

                }
            });
        }
        else {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }
    protected void empRegister(View v)
    {
        if(isInternet) {
            Intent i = new Intent(this, EmpRegistration.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }
    public void forgotPassword(View view)
    {
        //startActivity(new Intent(LoginPage.this, ForgotPasswordActivity.class));
    }
    private void showSnack(boolean isConnected) {
        String message;
        Snackbar snackbar;
        if (isConnected) {
            isInternet=true;
            message = "Internet Connected";
            snackbar = Snackbar.make(findViewById(R.id.cl4), message, Snackbar.LENGTH_SHORT);
        } else {
            isInternet=false;
            message = "No Active Internet";
            snackbar = Snackbar.make(findViewById(R.id.cl4), message, Snackbar.LENGTH_INDEFINITE);
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