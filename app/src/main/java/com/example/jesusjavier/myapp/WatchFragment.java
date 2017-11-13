package com.example.jesusjavier.myapp;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;


/**
 * A simple {@link Fragment} subclass.
 */
public class WatchFragment extends Fragment {
    Bitmap myBitmap;


    public WatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_watch, container, false);
        ImageView myImage=(ImageView) view.findViewById(R.id.codigoView);
        myBitmap= QRCode.from("www.perdidogs.com").bitmap();
        myImage.setImageBitmap(myBitmap);
        return view;
    }

}
