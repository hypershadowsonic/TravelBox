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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ConnectionClass connectionClass = new ConnectionClass();
                try {
                    Connection con = connectionClass.CONN();
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM ShopInfo WHERE hash='"+ result.getContents()+"';");
                    if(rs.next()){
                        int pop = rs.getInt("popularity");
                        Bundle bundle = new Bundle();
                        bundle.putString("id",rs.getString("shopid"));
                        bundle.putString("query",getPushQuery(rs.getString("notificationzone")));
                        AwardFragment awardF = new AwardFragment();
                        awardF.setArguments(bundle);
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        String shopID = rs.getString("shopid");
                        Log.d("QrScan", "In Rs Result"+shopID);
                        ResultSet rs2 = stmt.executeQuery("SELECT archlv FROM UserArch WHERE ownerid='"+TravelBox.userId+"' AND arch='"+shopID+"';");
                        if(rs2.next()){
                            //Popularity +1
                            int lv = rs2.getInt("archlv");
                            stmt.executeUpdate("UPDATE ShopInfo SET popularity="+(pop+1)+" WHERE shopid='"+shopID+"';");
                            Log.d("QrScan", "In Rs2 Result");
                            //User has it, check if it's under lv4
                            if(lv<4){
                                ft.replace(R.id.main_container, awardF)
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                                Log.d("QrScan", "Committed");
                            } else {
                                //User doesn't have it, pass through directly
                                Toast.makeText(this, "You already have level 4 Arch.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            ft.replace(R.id.main_container, awardF)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            Log.d("QrScan", "Committed");
                        }

                    } else {
                        Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex){
                    Toast.makeText(this, "Unable to connect to server: " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getPushQuery(String zone){
        String query="SELECT TOP 1 * FROM PlaceInfo WHERE";
        if (zone.indexOf("A")!=-1){
            query += " zone='A'";
        }
        if (zone.indexOf("B")!=-1){
            query += " OR zone='B'";
        }
        if (zone.indexOf("C")!=-1){
            query += " OR zone='C'";
        }
        if (zone.indexOf("D")!=-1){
            query += " OR zone='D'";
        }
        if (zone.indexOf("E")!=-1){
            query += " OR zone='E'";
        }
        query += " ORDER BY NEWID();";

        Log.d("MainActivity", "getPushQuery: "+query);

        return query;
    }
}
