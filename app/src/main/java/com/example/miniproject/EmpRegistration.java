package com.example.miniproject;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmpRegistration extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    boolean isInternet;
    DatabaseReference db;
    EditText uName,uPass,uEmail,uId,uloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_registration);
        if(!isInternetConnected()) {
            isInternet=true;
            showSnack(false);
        }
        else {
            isInternet=true;
        }
        uName=findViewById(R.id.eu);
        uPass = findViewById(R.id.ep);
        uId=findViewById(R.id.eid);
        uloc=findViewById(R.id.eloc);
        uEmail = findViewById(R.id.eemail);
        db= FirebaseDatabase.getInstance().getReference("employee");
    }
    @Override
    protected void onResume() {
        super.onResume();
        InternetStatus.getInstance().setConnectivityListener(this);
    }
    public void createUser(View view) {
        if(isInternet) {
            String email = uEmail.getText().toString();
            final String password = uPass.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getBaseContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getBaseContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            String un = uName.getText().toString();
            String uid = uId.getText().toString();
            String phn = uloc.getText().toString();
            UserData node = new UserData(un, uid, phn, email);
            db.child(uid).setValue(node);
            Toast.makeText(getBaseContext(), "Successfully Registered.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
        }
    }
    private void showSnack(boolean isConnected) {
        String message;
        Snackbar snackbar;
        if (isConnected) {
            isInternet=true;
            message = "Internet Connected";
            snackbar = Snackbar.make(findViewById(R.id.cl5), message, Snackbar.LENGTH_SHORT);
        } else {
            isInternet=false;
            message = "No Active Internet";
            snackbar = Snackbar.make(findViewById(R.id.cl5), message, Snackbar.LENGTH_INDEFINITE);
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
