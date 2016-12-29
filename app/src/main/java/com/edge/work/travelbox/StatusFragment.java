package com.edge.work.travelbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

import static com.google.android.gms.internal.zzs.TAG;


public class StatusFragment extends Fragment{


    private static ImageView btnMyIsland,btnQR;
    private IntentIntegrator integrator;
    private FragmentManager fm;
    private static boolean myislandIsAlive=false;
    //private String name,picurl;
    private TextView name;
    private static TextView coin,trophy;
    private ImageView pic;
    private static ConnectionClass connectionClass = new ConnectionClass();


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
        coin = (TextView)rootview.findViewById(R.id.status_text_coin);
        coin.setText(""+TravelBox.coin);
        trophy = (TextView)rootview.findViewById(R.id.status_text_trophy);
        trophy.setText(""+TravelBox.trophy);
        integrator = new IntentIntegrator(getActivity());

        SQLiteDatabase sqLiteDB = getActivity().getBaseContext().openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDB.rawQuery("SELECT * FROM user WHERE id='"+TravelBox.userId+"';", null);
        if (query.moveToFirst()){
            //Set name and profile pic
            name.setText(query.getString(1));
            ImageLoader.getInstance().displayImage(query.getString(2),pic);
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

    public static void coinPaySave(Boolean isSave, int amount){
        if (isSave){
            startCountAnimation(coin, amount);
            TravelBox.coin += amount;
            DoSync doSync = new DoSync();
            doSync.execute("");
        } else {
            startCountAnimation(coin, -amount);
            TravelBox.coin -= amount;
            DoSync doSync = new DoSync();
            doSync.execute("");
        }


    }

    private static class DoSync extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    Log.e("SQL", "Unable to connect to server");
                } else {
                    String query = "UPDATE Users SET amount_coin="+TravelBox.coin+" where id='" + TravelBox.userId + "';";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                }
            } catch (Exception ex){
                Log.e(TAG, "doInBackground: "+ex.toString());
            }
            return null;
        }
    }

    private static void startCountAnimation(final TextView textView, int amount) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(TravelBox.coin, TravelBox.coin+amount);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
