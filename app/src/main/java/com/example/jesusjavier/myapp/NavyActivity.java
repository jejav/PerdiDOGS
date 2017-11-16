package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser currentFirebaseUser;

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

        PrincipalFragment fragment =new PrincipalFragment();
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
        ///FIREBASE
        firebaseAuth =FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();

        //crea al nuevo usuario
        makeUser();

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
        getMenuInflater().inflate(R.menu.menu, menu);
        userImageView=(ImageView)findViewById(R.id.userImageView);
        userView=(TextView) findViewById(R.id.userView);
        correoView=(TextView) findViewById(R.id.correoView);
        correoView.setText(usuario);
        userView.setText(correo);

        if (fotoUrl!=""){
            loadImageFromUrl(fotoUrl);
        }

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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);
            editor=prefs.edit();
            editor.putInt("optLog",0);
            editor.commit();

            Intent intent = new Intent(NavyActivity.this,LoginActivity.class);

            switch (optLog) {
                case 1:
                    
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                    break;
                case 2:

                    break;

                case 3:

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                }
                            });
                    FirebaseAuth.getInstance().signOut();

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
            PrincipalFragment fragment =new PrincipalFragment();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();

        } else if (id == R.id.nav_gallery) {
            ScannerFragment fragment =new ScannerFragment();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();


        } else if (id == R.id.nav_slideshow) {
            WatchFragment fragment =new WatchFragment();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();

        }

        else if (id == R.id.nav_manage) {
            PetPerfil fragment =new PetPerfil();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();

        } else if (id == R.id.nav_share) {

            UserPerfil fragment =new UserPerfil();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();

        } else if (id == R.id.nav_send) {
            UbicacionFragment fragment =new UbicacionFragment();
            ft=fm.beginTransaction();
            ft.replace(R.id.maincontainer,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void makeUser(){


 /*       Animal animal=new Animal(R.drawable.logo,"Null","Null","Null",usuario,correo);
        myRef=database.getReference("users").child(""+currentFirebaseUser.getUid());
        myRef.setValue(animal);*/

        myRef=database.getReference("users");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(currentFirebaseUser.getUid()).exists()){
                    //si el usuario ya existe no se hace nada

                }
                else {
                    Animal animal=new Animal(R.drawable.logo,"Null","Null","Null",usuario,correo);
                    myRef=database.getReference("users").child(""+currentFirebaseUser.getUid());
                    myRef.setValue(animal);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}