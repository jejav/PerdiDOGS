package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    String correoR,contraseñaR;
    GoogleApiClient mGoogleApiClient;

    String fotoUrl,usuario;
    int optLog;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            optLog=extras.getInt("optlog");
            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contrasena");
            usuario=extras.getString("usuario");
            fotoUrl=extras.getString("fotoperfil");

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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mmiPerfil:
                intent = new Intent(MainActivity.this, PerfilActivity.class);
                switch (optLog){
                    case 2:
                        intent.putExtra("optlog",optLog);
                        intent.putExtra("contraseña",contraseñaR);
                        //intent.putExtra("fotoperfil",fotoUrl.toString());
                        intent.putExtra("usuario",usuario);
                        intent.putExtra("correo",correoR);
                        break;
                    default:
                        intent.putExtra("optlog",optLog);
                        intent.putExtra("fotoperfil",fotoUrl.toString());
                        intent.putExtra("usuario",usuario);
                        intent.putExtra("correo",correoR);

                        break;
                }
                startActivity(intent);
                break;

            case R.id.cerrar:
                prefs=getSharedPreferences("Mis Preferencias",MODE_PRIVATE);
                editor=prefs.edit();
                editor.putInt("optLog",0);

                intent = new Intent(MainActivity.this,LoginActivity.class);
                prefs=getSharedPreferences("Mis Preferencias",MODE_PRIVATE);
                editor=prefs.edit();

                switch (optLog){
                    case 1:

                            LoginManager.getInstance().logOut();
                            break;
                    case 2:

                        intent.putExtra("optlog",optLog);
                        intent.putExtra("CorreoRe",correoR);
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
                startActivity(intent);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
