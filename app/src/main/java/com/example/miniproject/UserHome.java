package com.example.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }
    public void checkQuality(View v)
    {
        Intent intent=new Intent(this,SensorActivity.class);
        startActivity(intent);
    }
    public void signout(View v)
    {
        auth.signOut();
        startActivity(new Intent(UserHome.this,UserLogin.class));
        finish();
    }
}
