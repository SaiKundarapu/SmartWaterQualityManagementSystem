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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistration extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    boolean isInternet;
    private EditText uEmail,uPass,uName,uAadharId,uPhn;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    DatabaseReference db;
    // [END declare_auth]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        if(!isInternetConnected()) {
            isInternet=true;
            showSnack(false);
        }
        else {
            isInternet=true;
        }
        uName=findViewById(R.id.un);
        uPass = findViewById(R.id.up);
        uAadharId=findViewById(R.id.uid);
        uPhn=findViewById(R.id.uphn);
        uEmail = findViewById(R.id.uemail);
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference("user");
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
                Toast.makeText(UserRegistration.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(UserRegistration.this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.length()<6)
            {
                Toast.makeText(UserRegistration.this, "Password Should Minimum 6 Charaters!", Toast.LENGTH_SHORT).show();
                return;
            }
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String un = uName.getText().toString();
                                String uid = uAadharId.getText().toString();
                                String phn = uPhn.getText().toString();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    UserData node = new UserData(un, uid, phn, user.getEmail());
                                    db.child(user.getUid()).setValue(node);
                                    Toast.makeText(UserRegistration.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(UserRegistration.this, UserLogin.class);
                                    startActivity(i);
                                }
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                if(!isInternetConnected())
                                {
                                    Toast.makeText(getBaseContext(),"Please Check Internet Connectivity",Toast.LENGTH_SHORT).show();
                                }
                                else
                                Toast.makeText(UserRegistration.this, "Enter Valid Details!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
            snackbar = Snackbar.make(findViewById(R.id.cl3), message, Snackbar.LENGTH_SHORT);
        } else {
            isInternet=false;
            message = "No Active Internet";
            snackbar = Snackbar.make(findViewById(R.id.cl3), message, Snackbar.LENGTH_INDEFINITE);
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