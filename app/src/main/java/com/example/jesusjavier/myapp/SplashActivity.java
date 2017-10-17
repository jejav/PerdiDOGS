package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs=getSharedPreferences("Mis Preferencias",MODE_PRIVATE);
        final int optlog=prefs.getInt("optLog",0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if(optlog==0)
                       intent= new Intent(SplashActivity.this,LoginActivity.class);
                else
                    intent= new Intent(SplashActivity.this,NavyActivity.class);

                startActivity(intent);
                finish();

            }
        };

        Timer timer=new Timer();
        timer.schedule(task,1000);
    }
}
