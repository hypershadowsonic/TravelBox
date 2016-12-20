package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

import static com.google.android.gms.internal.zzs.TAG;


public class StatusFragment extends Fragment{


    private static ImageView btnMyIsland,btnQR;
    private IntentIntegrator integrator;
    private FragmentManager fm;
    private static boolean myislandIsAlive=false;
    //private String name,picurl;
    private int count_coin, count_trophy;
    private TextView name,coin,trophy;
    private ImageView pic;


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
        name = (TextView)rootview.findViewById(R.id.status_text_username);
        pic = (ImageView)rootview.findViewById(R.id.status_img_profile);
        integrator = new IntentIntegrator(getActivity());

        SQLiteDatabase sqLiteDB = getActivity().getBaseContext().openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDB.rawQuery("SELECT * FROM user", null);
        if (query.moveToFirst()){
            //name = query.getString(1);
            //picurl = query.getString(2);
            name.setText(query.getString(1));
            pic.setImageDrawable(LoadImageFromWebOperations(query.getString(2)));
        }

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
                    fm.popBackStack();
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

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
