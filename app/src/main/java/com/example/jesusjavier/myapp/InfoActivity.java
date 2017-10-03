package com.example.jesusjavier.myapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();

        AutoFragment fragment =new AutoFragment();
        ft.add(R.id.frame,fragment).commit();
    }

    public void Cambiar(View view) {
        ft=fm.beginTransaction();

        MotoFragment fragment =new MotoFragment();
        ft.replace(R.id.frame,fragment).commit();

    }
}
