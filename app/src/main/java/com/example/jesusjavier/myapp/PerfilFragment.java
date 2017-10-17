package com.example.jesusjavier.myapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    ImageView imgperfil ;
    TextView infousermail,infousername;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;




    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myinflateview= inflater.inflate(R.layout.fragment_perfil, container, false);
        infousermail=(TextView)myinflateview.findViewById(R.id.infousermail);
        infousername=(TextView)myinflateview.findViewById(R.id.infousername);
        imgperfil=(ImageView)myinflateview.findViewById(R.id.imgFragperfil);
        SharedPreferences prefs = getActivity().getSharedPreferences("datoscompartidos",Context.MODE_PRIVATE);

        infousermail.setText(prefs.getString("correo","null@mail.com"));
        infousername.setText(prefs.getString("usuario","Null"));
        loadImageFromUrl(prefs.getString("fotoperfil",""));
        return  myinflateview;

    }

    private void loadImageFromUrl(String fotoperfil) {
        Picasso.with(this.getContext()).load(fotoperfil).placeholder(R.mipmap.ic_launcher)
                .into(imgperfil, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });
    }


}
