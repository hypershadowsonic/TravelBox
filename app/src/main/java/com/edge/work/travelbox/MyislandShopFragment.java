package com.edge.work.travelbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;


public class MyislandShopFragment extends Fragment {

    public MyislandShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_shop, container, false);
        final String FORMAT = "%02d:%02d";
        SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DecTemp(slot1 TEXT, slot2 TEXT, slot3 TEXT, lastupdate LONG DEFAULT 0);");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM DecTemp",null);
        if (cursor.moveToNext()){

        } else {
            sqLiteDatabase.execSQL("INSERT");
        }
        new CountDownTimer(360000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();

        sqLiteDatabase.close();
        return rootview;
    }

    public void updateDec(){
        SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT decid FROM Decoration ORDER BY RANDOM() LIMIT 3;",null);
        for (int i=1;i<=3;i++) {
            cursor.moveToNext();
            sqLiteDatabase.execSQL("UPDATE DecTemp SET slot"+i+"='"+cursor.getString(cursor.getColumnIndex("decid"))+"';");
        }
    }

    public void showDec(){

    }


}
