package com.example.jesusjavier.myapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class PerfilActivity extends AppCompatActivity {

    String fotoUrl,usuario,correo,contraseñaR;
    ImageView imgperfil;
    TextView userinfo,usermail;
    int optLog;

    GoogleApiClient mGoogleApiClient;



    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        imgperfil=(ImageView)findViewById(R.id.imgperfil);
        userinfo=(TextView)findViewById(R.id.username);
        usermail= (TextView) findViewById(R.id.usermail);

        Bundle extras = getIntent().getExtras();

        if (extras!=null) {
            optLog=extras.getInt("optlog");
            usuario=extras.getString("usuario");
            fotoUrl=extras.getString("fotoperfil");
            correo=extras.getString("correo");
            contraseñaR=extras.getString("contraseña");

            userinfo.setText(usuario);
            usermail.setText(correo);


           loadImageFromUrl(fotoUrl);

        }

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

        //onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadImageFromUrl(String fotoperfil) {
        Picasso.with(this).load(fotoperfil).placeholder(R.mipmap.ic_launcher)
                .into(imgperfil, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mprincipal:
                onBackPressed();
                break;

            case R.id.cerrar:
                prefs=getSharedPreferences("Mis Preferencias",MODE_PRIVATE);
                editor=prefs.edit();
                editor.putInt("optLog",0);
                editor.commit();

                intent = new Intent(PerfilActivity.this,LoginActivity.class);

                switch (optLog){
                    case 1:
                        LoginManager.getInstance().logOut();
                        break;
                    case 2:
                        intent.putExtra("optlog",optLog);
                        intent.putExtra("CorreoRe",correo);
                        intent.putExtra("ContrasenaRe",contraseñaR);
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

               // intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
