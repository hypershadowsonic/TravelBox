package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainActivity extends FragmentActivity {
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        StatusFragment statusF=(StatusFragment) fm.findFragmentById(R.id.status_bar);
        if(statusF==null || ! statusF.isInLayout()){
            statusF = new StatusFragment();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.status_bar, statusF)
                    .commit();
        }

        MainFragment mainF=(MainFragment) fm.findFragmentById(R.id.main_container);
        if(mainF==null || ! mainF.isInLayout()){
            mainF = new MainFragment();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_container, mainF)
                    .commit();
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, getString(R.string.award_cancelled), Toast.LENGTH_LONG).show();
            } else {
                try {
                    SQLiteDatabase sqLiteDB = getBaseContext().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                    Cursor cursor = sqLiteDB.rawQuery("SELECT * FROM ShopInfo WHERE hash='"+ result.getContents()+"';",null);
                    if (cursor.moveToNext()){
                        int pop = cursor.getInt(cursor.getColumnIndex("popularity"));
                        Bundle bundle = new Bundle();
                        bundle.putString("id",cursor.getString(cursor.getColumnIndex("shopid")));
                        bundle.putString("query",getPushQuery(cursor.getString(cursor.getColumnIndex("notificationzone"))));
                        AwardFragment awardF = new AwardFragment();
                        awardF.setArguments(bundle);
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        String shopID = cursor.getString(cursor.getColumnIndex("shopid"));
                        Log.d("QrScan", "In Rs Result"+shopID);
                        Cursor cursor2 = sqLiteDB.rawQuery("SELECT archlv FROM UserArch WHERE ownerid='"+TravelBox.userId+"' AND arch='"+shopID+"';",null);
                        if(cursor2.moveToNext()){
                            //Popularity +1
                            int lv = cursor2.getInt(cursor2.getColumnIndex("archlv"));
                            sqLiteDB.execSQL("UPDATE ShopInfo SET popularity="+(pop+1)+" WHERE shopid='"+shopID+"';");

                            //Update Server Data
                            ConnectionClass connectionClass = new ConnectionClass();
                            Connection connection = connectionClass.CONN();
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("UPDATE ShopInfo SET popularity="+(pop+1)+" WHERE shopid='"+shopID+"';");

                            //User has it, check if it's under lv4
                            if(lv<4){
                                ft.replace(R.id.main_container, awardF)
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                                Log.d("QrScan", "Committed");
                            } else {
                                Toast.makeText(this, getString(R.string.award_level4), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //User doesn't have it, pass through directly
                            ft.replace(R.id.main_container, awardF)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            Log.d("QrScan", "Committed");
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.award_invalid), Toast.LENGTH_LONG).show();
                    }
                    sqLiteDB.close();
                } catch (Exception ex){
                    Toast.makeText(this, getString(R.string.unable_connect) + ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getPushQuery(String zone){
        String query="SELECT * FROM PlaceInfo WHERE";
        if (zone.contains("A")){
            query += " zone='A'";
        }
        if (zone.contains("B")){
            query += " OR zone='B'";
        }
        if (zone.contains("C")){
            query += " OR zone='C'";
        }
        if (zone.contains("D")){
            query += " OR zone='D'";
        }
        if (zone.contains("E")){
            query += " OR zone='E'";
        }
        query += " ORDER BY RANDOM() LIMIT 1;";

        Log.d("MainActivity", "getPushQuery: "+query);

        return query;
    }

    public void popBack(){
        fm.popBackStack();
    }
}
