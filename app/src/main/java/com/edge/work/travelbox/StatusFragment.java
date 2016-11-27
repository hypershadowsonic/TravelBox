package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.google.android.gms.internal.zzs.TAG;


public class StatusFragment extends Fragment{


    private static ImageView btnMyIsland,btnQR;
    private IntentIntegrator integrator;
    private FragmentManager fm;
    private static boolean myislandIsAlive=false;

    public StatusFragment() {
        // Required empty public constructor
    }

    public static class myIslandStatus{

        public static void setMyislandStatus(boolean setSt){
            if(setSt==true){
                btnMyIsland.setImageResource(R.mipmap.status_nav_myisland_triggered);
            } else {
                btnMyIsland.setImageResource(R.mipmap.status_nav_myisland);
            }
            myislandIsAlive=setSt;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_status, container, false);
        btnMyIsland = (ImageView)rootview.findViewById(R.id.status_btn_MyIsland);
        btnQR = (ImageView)rootview.findViewById(R.id.status_btn_QRScan);
        integrator = new IntentIntegrator(getActivity());

        btnMyIsland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: MyIsland");
                fm = getFragmentManager();
                if(myislandIsAlive==false){
                fm.beginTransaction()
                        .replace(R.id.main_container, new MyislandAssemblyFragment())
                        .addToBackStack(null)
                        .commit();} else {
                }
            }
        });
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: QR");
                integrator.initiateScan();
            }
        });
        return rootview;
    }


}
