package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MyislandIslandFragment extends Fragment {

    private ImageView[][] land = new ImageView[6][6];
    private Land landProp = new Land();
    private View.OnClickListener mListener;
    private ConnectionClass connectionClass = new ConnectionClass();
    private String userID;
    private ImageView[] iv;
    private FrameLayout fl;
    private RecyclerView recyclerView;
    private UserIslandListAdapter adapter;


    public MyislandIslandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_myisland_island, container, false);
        landProp.initLands(rootview);
        //Initialize Universal Image Loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        //Get user ID
        SQLiteDatabase sqLiteDB = getActivity().getBaseContext().openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDB.rawQuery("SELECT * FROM user", null);
        if (query.moveToFirst()){
            userID = query.getString(0);
        }

        //Need to figure a better way of these
        land[0][0]=(ImageView) rootview.findViewById(R.id.land1_1);
        land[0][1]=(ImageView) rootview.findViewById(R.id.land1_2);
        land[0][2]=(ImageView) rootview.findViewById(R.id.land1_3);
        land[0][3]=(ImageView) rootview.findViewById(R.id.land1_4);
        land[0][4]=(ImageView) rootview.findViewById(R.id.land1_5);
        land[0][5]=(ImageView) rootview.findViewById(R.id.land1_6);

        land[1][0]=(ImageView) rootview.findViewById(R.id.land2_1);
        land[1][1]=(ImageView) rootview.findViewById(R.id.land2_2);
        land[1][2]=(ImageView) rootview.findViewById(R.id.land2_3);
        land[1][3]=(ImageView) rootview.findViewById(R.id.land2_4);
        land[1][4]=(ImageView) rootview.findViewById(R.id.land2_5);
        land[1][5]=(ImageView) rootview.findViewById(R.id.land2_6);

        land[2][0]=(ImageView) rootview.findViewById(R.id.land3_1);
        land[2][1]=(ImageView) rootview.findViewById(R.id.land3_2);
        land[2][2]=(ImageView) rootview.findViewById(R.id.land3_3);
        land[2][3]=(ImageView) rootview.findViewById(R.id.land3_4);
        land[2][4]=(ImageView) rootview.findViewById(R.id.land3_5);
        land[2][5]=(ImageView) rootview.findViewById(R.id.land3_6);

        land[3][0]=(ImageView) rootview.findViewById(R.id.land4_1);
        land[3][1]=(ImageView) rootview.findViewById(R.id.land4_2);
        land[3][2]=(ImageView) rootview.findViewById(R.id.land4_3);
        land[3][3]=(ImageView) rootview.findViewById(R.id.land4_4);
        land[3][4]=(ImageView) rootview.findViewById(R.id.land4_5);
        land[3][5]=(ImageView) rootview.findViewById(R.id.land4_6);

        land[4][0]=(ImageView) rootview.findViewById(R.id.land5_1);
        land[4][1]=(ImageView) rootview.findViewById(R.id.land5_2);
        land[4][2]=(ImageView) rootview.findViewById(R.id.land5_3);
        land[4][3]=(ImageView) rootview.findViewById(R.id.land5_4);
        land[4][4]=(ImageView) rootview.findViewById(R.id.land5_5);
        land[4][5]=(ImageView) rootview.findViewById(R.id.land5_6);

        land[5][0]=(ImageView) rootview.findViewById(R.id.land6_1);
        land[5][1]=(ImageView) rootview.findViewById(R.id.land6_2);
        land[5][2]=(ImageView) rootview.findViewById(R.id.land6_3);
        land[5][3]=(ImageView) rootview.findViewById(R.id.land6_4);
        land[5][4]=(ImageView) rootview.findViewById(R.id.land6_5);
        land[5][5]=(ImageView) rootview.findViewById(R.id.land6_6);

        fl = (FrameLayout)rootview.findViewById(R.id.island_container);

        recyclerView = (RecyclerView)rootview.findViewById(R.id.arch_recyclerview);
        adapter = new UserIslandListAdapter(getContext(), UserIslandListData.getData(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(landProp.getIsOpen()) {
                    if (landProp.getAvailableArray(view.getTag().toString().charAt(0), view.getTag().toString().charAt(1))){

                    }
                }
            }
        };

        for(int i=0;i<6;i++){
            for (int j=0;j<6;j++){
                land[i][j].setOnClickListener(mListener);
            }
        }



        return rootview;
    }

    public void setPickOpen(Boolean isBig){

        landProp.scanAvailableLand(isBig);
        landProp.setIsOpen(true);
        Boolean[][] table = landProp.getAvailableArray();
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                if (table[i][j]){
                    land[i][j].setColorFilter(brightIt(100));
                }
            }
        }
    }

    public void setPickClose(View rootview){
        landProp.setIsOpen(false);

        //Sync with server

        //Update land status
        landProp.initLands(rootview);
    }

    public void initShowAllArch(){
        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                if (landProp.landElements[i][j].getIsHead()){
                    try {
                        //Get arch image url from server
                        Connection con = connectionClass.CONN();
                        Statement stmt = con.createStatement();
                        //Determine what level does the user have
                        ResultSet rs = stmt.executeQuery("SELECT archlv FROM UserArch WHERE ownerid='"+userID+"' AND arch='"+ landProp.landElements[i][j].getContent() +"';");
                        int lv = rs.getInt(0);
                        //Fetch the target level image url of arch
                        rs = stmt.executeQuery("SELECT archlv"+lv+" FROM ShopInfo WHERE shopid='"+landProp.landElements[i][j].getContent()+"';");
                        String url = rs.getString(0);
                        //New ImageView
                        double marginleft=28.5*j-28.5*i;
                        double marginbottom=16.5*j+16.5*i;
                        iv[i*10+j]=new ImageView(getContext());
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.gravity=0x01|0x50; //Gravity= Center Horizontal + Bottom
                        lp.leftMargin= ((int) marginleft);
                        lp.bottomMargin= ((int) marginbottom);
                        fl.addView(iv[i*10+j],lp);
                    } catch (Exception ex){
                        Log.e("SQL", "initShowAllArch: " + ex.toString());
                    }
                }
            }
        }
    }

    public static ColorMatrixColorFilter brightIt(int fb) {
        //USE:  TargetImageView.setColorFilter(brightIt(100));
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[] {
                1, 0, 0, 0, fb,
                0, 1, 0, 0, fb,
                0, 0, 1, 0, fb,
                0, 0, 0, 1, 0   });

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(cmB);
        //Canvas c = new Canvas(b2);
        //Paint paint = new Paint();
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
        //paint.setColorFilter(f);
        return f;
    }

}
