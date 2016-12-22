package com.edge.work.travelbox;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class MyislandIslandFragment extends Fragment {

    private ImageView[][] land = new ImageView[6][6];
    private Land landProp = new Land();


    public MyislandIslandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_myisland_island, container, false);

        //Need to figure a better way of these
        //landProp.landElements[0][0].landImg = (ImageView) rootview.findViewById(R.id.land1_2);
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




        return rootview;
    }


}
