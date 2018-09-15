package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MemberSelection extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    boolean isInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_selection);
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

    protected void user(View v) {
        if(isInternet) {
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }

    protected void empolyee(View v) {
        if(isInternet) {
            Intent intent = new Intent(this, EmpolyeeLogin.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }
    private void showSnack(boolean isConnected) {
        String message;
        Snackbar snackbar;
        if (isConnected) {
            isInternet=true;
            message = "Internet Connected";
            snackbar = Snackbar.make(findViewById(R.id.cl1), message, Snackbar.LENGTH_SHORT);
        } else {
            isInternet=false;
            message = "No Active Internet";
            snackbar = Snackbar.make(findViewById(R.id.cl1), message, Snackbar.LENGTH_INDEFINITE);
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
