package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;



public class LoginActivity extends AppCompatActivity {
    private String correoR,contraseñaR;
    private LoginButton loginButton;
    CallbackManager callbackmanager;
    GoogleApiClient mGoogleApiClient;
    private int optLog; //1 Facebook 2 Email 3 Google

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String correoUser,passUser;
    String correoCom,contraseñaCom;

    String username,fotoUrl;
    String nombreG,correoG,urlfotoG;

    EditText eCorreo,eContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eCorreo=(EditText)findViewById(R.id.eCorreo);
        eContrasena=(EditText)findViewById(R.id.eContrasena);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correoUser = extras.getString("CorreoRe");
            passUser = extras.getString("ContrasenaRe");
        }

        ////////google/////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"error en login",Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton =(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        ////////////////////////////Log con FACEBOOK ///////////////////////////////////////

       //// loginButton.setReadPermissions(Arrays.asList("public_profile‌​","email"));
        callbackmanager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //
                //optLog = 1;

                if (AccessToken.getCurrentAccessToken() != null) {
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            final JSONObject json = response.getJSONObject();

                            try {//Facebook
                                if (json != null) {
                                    optLog = 1;
                                    username = object.getString("name");
                                    correoR = object.getString("email");
                                    //fotoUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String id = object.getString("id");
                                    fotoUrl ="https://graph.facebook.com/" + id + "/picture?type=large";
                                    Toast.makeText(getApplicationContext(),"Inicio exitoso",Toast.LENGTH_LONG).show();
                                    goMainActivity();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,picture");
                    request.setParameters(parameters);
                    request.executeAsync();
                }//fin metodo

                //fotoUrl="https://1.bp.blogspot.com/-pNIe14h4NC0/VZpa_-UOD0I/AAAAAAAAARA/cfDik7qkFvg/w1200-h630-p-k-no-nu/DBS.jpg";
                //username="Cualquiervaina";

               // goMainActivity();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Sesión Cancelada",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();

            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.jesusjavier.myapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
/////////////////////////////////////////end FB log////////////////////////////////////////////////////////

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 8765);
    }

    public void registrarse(View view){
        Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
        startActivityForResult(intent,1234);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==1234 && resultCode ==RESULT_OK){
            //correoR = data.getExtras().getString("correo");
            //contraseñaR = data.getExtras().getString("contraseña");
            correoUser = data.getExtras().getString("CorreoRe");
            passUser = data.getExtras().getString("ContrasenaRe");
           // Log.d("correo",correoR);
            //Log.d("contraseña",contraseñaR);

        }
        else if (requestCode == 8765) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackmanager.onActivityResult(requestCode,resultCode,data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),acct.getDisplayName(),Toast.LENGTH_SHORT).show();
            optLog=3;
            nombreG=acct.getDisplayName();
            correoG=acct.getEmail();
            urlfotoG=acct.getPhotoUrl().toString();
            goMainActivity();

        } else {
            Toast.makeText(getApplicationContext(), "Error en login", Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.

        }
    }

    public void Iniciar(View view) {
        correoCom=eCorreo.getText().toString();
        contraseñaCom=eContrasena.getText().toString();


        if(TextUtils.isEmpty(correoCom) ){
            eCorreo.setError("Ingrese un correo ");
            return;
        }
        if(TextUtils.isEmpty(contraseñaCom) ){
            eContrasena.setError("Ingrese una contraseña");
            return;
        }


        if( (correoCom.equals(correoUser)) &&  (contraseñaCom.equals(passUser)) ){
            optLog = 2;
            goMainActivity();
        }
        else {
            Toast.makeText(getApplicationContext(),"EL usuario ingresado no Existe",Toast.LENGTH_SHORT).show();
        }


    }

    public void goMainActivity() {

        prefs=getSharedPreferences("Mis Preferencias",MODE_PRIVATE);
        editor=prefs.edit();
        editor.putInt("optLog",optLog);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (optLog == 1) { //facebook


            //extraigo el correo y la foto
            //fotoUrl="";//+profile.getProfilePictureUri(450,450).toString();
            intent.putExtra("correo",correoR);
            intent.putExtra("optlog",optLog);
            intent.putExtra("usuario", username);
            intent.putExtra("fotoperfil", fotoUrl);

        } else if (optLog == 2) {//correo y contraseña Login Propio
            intent.putExtra("optlog",optLog);
            intent.putExtra("correo", correoUser);
            intent.putExtra("contrasena", passUser);

        } else if (optLog == 3) { //login con google
            intent.putExtra("optlog",optLog);
            intent.putExtra("correo",correoG);
            intent.putExtra("usuario",nombreG);
            intent.putExtra("fotoperfil",urlfotoG);

        }
        startActivity(intent);
        finish();
    }
}
