package com.example.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
    @Override
    protected void onStart() {
        super.onStart();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2600);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MemberSelection.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }
    protected void onPause() {
        super.onPause();
        finish();
    }
}