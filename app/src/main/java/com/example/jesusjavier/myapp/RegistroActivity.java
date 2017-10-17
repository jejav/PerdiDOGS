package com.example.jesusjavier.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;


public class RegistroActivity extends AppCompatActivity {
     String correo, contrasena,contrasenaRep;
     EditText eCorreo, eContrasena,eRepcontrasena;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        eCorreo=(EditText)findViewById(R.id.eCorreo);
        eContrasena=(EditText)findViewById(R.id.eContrasena);
        eRepcontrasena=(EditText)findViewById(R.id.eRepcontrasena);

    }

    @Override
    protected void onResume() {
        prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);

        //correo=prefs.getString("correo","null@mail.com");
        //contrasena=prefs.getString("contrasena","null--");

        super.onResume();
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void registrar(View view){

        correo=eCorreo.getText().toString();
        contrasena=eContrasena.getText().toString();
        contrasenaRep=eRepcontrasena.getText().toString();
        if(TextUtils.isEmpty(correo) ){
            eCorreo.setError("Ingrese un correo válido");

            return;
        }
        if (!validarEmail(correo)){
            eCorreo.setError("Email no válido");
            return;
        }
        if(TextUtils.isEmpty(contrasena) ){
            eContrasena.setError("Ingrese una contraseña");
            return;
        }
        if(TextUtils.isEmpty(contrasenaRep) ){
            eRepcontrasena.setError("Ingrese la contraseña nuevamente");
            return;
        }

        if (!contrasena.equals(contrasenaRep)){
            Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();

        }else {
            //Validaciones
            Intent intent =new Intent();
            prefs=getSharedPreferences("datoscompartidos",MODE_PRIVATE);
            editor= prefs.edit();
            editor.putString("CorreoRe",correo);
            editor.putString("ContrasenaRe",contrasena);
            editor.commit();
            Toast.makeText(getApplicationContext(),"Cuenta creada con éxito",Toast.LENGTH_SHORT).show();
          //  intent.putExtra("CorreoRe",correo);
            //intent.putExtra("ContrasenaRe",contrasena);
            setResult(RESULT_OK,intent);
            finish();
        }

    }
}
