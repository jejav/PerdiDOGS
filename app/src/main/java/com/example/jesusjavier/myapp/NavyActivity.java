package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class NavyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;
    FragmentTransaction ft;

    ImageView userImageView;
    TextView userView,correoView;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

     String fotoUrl,correo,usuario;
     int optLog;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();

        VentanaPrincipal fragment =new VentanaPrincipal();
        ft.replace(R.id.maincontainer,fragment).commit();


        ///GOOGLE///
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Error en login",
                                Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    @Override
    protected void onResume() {
        prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);

        fotoUrl=prefs.getString("fotoperfil","");
        correo=prefs.getString("correo","null@mail.com");
        usuario=prefs.getString("usuario","NULL");
        optLog=prefs.getInt("optlog",0);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.navy, menu);
        //return true;

        /// nuevo
        getMenuInflater().inflate(R.menu.menu_vehiculos, menu);

        userImageView=(ImageView)findViewById(R.id.userImageView);
        userView=(TextView) findViewById(R.id.userView);
        correoView=(TextView) findViewById(R.id.correoView);
        correoView.setText(usuario);
        userView.setText(correo);
        loadImageFromUrl(fotoUrl);
        //////NUevo Bottom///////

        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return true;
    }


    private void loadImageFromUrl(String fotoperfil) {
        Picasso.with(this).load(fotoperfil).placeholder(R.mipmap.ic_launcher)
                .into(userImageView, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);
            editor=prefs.edit();
            editor.putInt("optLog",0);
            editor.commit();

            Intent intent = new Intent(NavyActivity.this,LoginActivity.class);

            switch (optLog) {
                case 1:
                    LoginManager.getInstance().logOut();
                    break;
                case 2:
                    /*intent.putExtra("optlog",optLog);
                    intent.putExtra("CorreoRe",correo);
                    intent.putExtra("ContrasenaRe",contraseñaR);*/
                    break;

                case 3:

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });

                    break;

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

                //return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            VentanaPrincipal fragment =new VentanaPrincipal();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();

           // mViewPager.setCurrentItem(1);
           // bottomNavigationView.setSelectedItemId(R.id.navigation_home);


        } else if (id == R.id.nav_gallery) {
            Perfiles fragment2 =new Perfiles();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment2).commit();

           // mViewPager.setCurrentItem(2);
            //bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);


        } else if (id == R.id.nav_slideshow) {
            Bottom_Fragment fragment =new Bottom_Fragment();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();
           // bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);


        }
        /*

        else if (id == R.id.nav_manage) {




        } else if (id == R.id.nav_share) {



        } else if (id == R.id.nav_send) {


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
