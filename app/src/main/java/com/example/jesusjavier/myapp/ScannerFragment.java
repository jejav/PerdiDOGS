package com.example.jesusjavier.myapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.os.Vibrator;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private LinearLayout qrCamera;


    public ScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_scanner, container, false);
        //View view= inflater.inflate(R.layout.fragment_scanner, container, false);
        qrCamera=(LinearLayout)view.findViewById(R.id.qrCamera);
        mScannerView = new ZXingScannerView(getContext());   // Programmatically initialize the scanner view
       // setContentView(mScannerView);

        mScannerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        qrCamera.addView(mScannerView);

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 100 milliseconds
        v.vibrate(100);
        String codigo;
        codigo=result.getText();
        Toast.makeText(getContext(), "codigo: "+codigo, Toast.LENGTH_LONG).show();



    }
}
