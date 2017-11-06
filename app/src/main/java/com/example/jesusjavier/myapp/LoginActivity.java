package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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

    String correoUser,passUser,correoCom,contraseñaCom;
    String nombreG,correoG,urlfotoG,username,fotoUrl;

    EditText eCorreo,eContrasena;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        eCorreo=(EditText)findViewById(R.id.eCorreo);
//        eContrasena=(EditText)findViewById(R.id.eContrasena);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correoUser = extras.getString("CorreoRe");
            passUser = extras.getString("ContrasenaRe");
        }

        ////////google/////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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

        ////////////////////////////LOGIN con FACEBOOK ///////////////////////////////////////

        callbackmanager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

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
        /////////////////////////////////////////Firebase////////////////////////////////////////////////////////

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                    //IR a la Actividad principál aquí
                    //GoogleSignInAccount acct = result.getSignInAccount();
                    nombreG=user.getDisplayName();
                    correoG=user.getEmail();
                    urlfotoG=user.getPhotoUrl().toString();
                    optLog=3;
                    goMainActivity();
                }

            }
        };

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error en Inicio de sesión", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    protected void onResume() {
        prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);
        optLog=prefs.getInt("optlog",3);
        fotoUrl=prefs.getString("fotoperfil","");
        correoUser=prefs.getString("CorreoRe","");
        passUser=prefs.getString("ContrasenaRe","");
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener!=null)
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 8765);
    }

/*    public void registrarse(View view){
        Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
        startActivityForResult(intent,1234);

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==1234 && resultCode ==RESULT_OK){


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
            firebaseAuthWithGoogle(acct);

        } else {
            Toast.makeText(getApplicationContext(), "Error en login", Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Falló el inicio de sesión", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }

    public void Iniciar(View view) {
/*        correoCom=eCorreo.getText().toString();
        contraseñaCom=eContrasena.getText().toString();


        if( (correoCom.equals(correoUser)) &&  (contraseñaCom.equals(passUser)) ){
            optLog = 2;
            goMainActivity();
        }
        else {
            Toast.makeText(getApplicationContext(),"EL usuario ingresado no Existe",Toast.LENGTH_SHORT).show();
        }*/

    }

    public void goMainActivity() {
        prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);
        editor= prefs.edit();
        editor.putInt("optlog",optLog);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, NavyActivity.class);

        if (optLog == 1) { //facebook
            editor.putString("correo",correoR);
            editor.putString("usuario",username);
            editor.putString("fotoperfil",fotoUrl);
            editor.putInt("optlog",optLog);
            editor.commit();

        } else if (optLog == 2) {//correo y contraseña Login Propio

            editor.putString("correo",correoUser);
            editor.putString("contrasena",passUser);
            editor.putInt("optlog",optLog);
            editor.commit();

        } else if (optLog == 3) { //login con google
            editor.putString("correo",correoG);
            editor.putString("usuario",nombreG);
            editor.putString("fotoperfil",urlfotoG);
            editor.putInt("optlog",optLog);
            editor.commit();

        }
        startActivity(intent);
        finish();
    }
}
