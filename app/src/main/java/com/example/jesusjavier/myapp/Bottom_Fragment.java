package com.example.jesusjavier.myapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Bottom_Fragment extends Fragment {

    FrameLayout botcontainer;

    FragmentManager fm;
    FragmentTransaction ft;


    public Bottom_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_bottom_, container, false);
       View view= inflater.inflate(R.layout.fragment_bottom_, container, false);

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        botcontainer=(FrameLayout)view.findViewById(R.id.botcontainer);
        fm=getFragmentManager();

        ft=fm.beginTransaction();

        VentanaPrincipal fragment =new VentanaPrincipal();
        ft.replace(R.id.botcontainer,fragment).commit();
        return view;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    ft=fm.beginTransaction();
                    VentanaPrincipal fragment =new VentanaPrincipal();
                    ft.replace(R.id.botcontainer,fragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    ft=fm.beginTransaction();

                    ScannerFragment fragment2 =new ScannerFragment();
                    ft.replace(R.id.botcontainer,fragment2).commit();
                    return true;
                case R.id.navigation_notifications:
                    ft=fm.beginTransaction();

                    UbicacionFragment fragment3 =new UbicacionFragment();
                    ft.replace(R.id.botcontainer,fragment3).commit();
                    return true;
            }
            return false;
        }

    };


}
