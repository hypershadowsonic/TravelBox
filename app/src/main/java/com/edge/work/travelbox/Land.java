package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Super_000 on 12/22/2016.
 */

public class Land{

    public LandElement[][] landElements = new LandElement[6][6];
    private Boolean[][] availableArray = new Boolean[6][6];
    private ConnectionClass connectionClass = new ConnectionClass();
    private String id;
    private String[] arch;
    private int[] archposx,archposy;


    public void initLands(View rootview){
        //Initial all block as True
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                availableArray[i][j] = true;
            }
        }

        //Get lands from server
        try{
            Connection con = connectionClass.CONN();
            if (con == null){
                //Failed to connect to server
            } else {
                //Get id from local SQLite Database
                SQLiteDatabase sqLiteDB = rootview.getContext().openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
                Cursor query = sqLiteDB.rawQuery("SELECT id FROM user", null);
                if (query.moveToFirst()){
                    id = query.getString(0);
                }
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT archcount FROM Island WHERE ownerid="+id+";");
                if (rs.getInt(0) == 0){
                   //Do nothing
                } else {
                    for(int i=0; i<rs.getInt(0); i++){
                        //Get arch info from server
                        ResultSet rs1 = stmt.executeQuery("SELECT arch"+i+", archposx"+i+", archposy"+i+" FROM Island WHERE ownerid="+id+";");
                        arch[i] = rs1.getString(i);
                        archposx[i] = rs1.getInt(i);
                        archposy[i] = rs1.getInt(i);
                        //Get arch type from server
                        ResultSet rs2 = stmt.executeQuery("SELECT archtype FROM ShopInfo WHERE id="+arch+";");
                        if (rs2.getString(0) == "S" || rs2.getString(0) == "s"){
                            //Mark 2*2 area
                            availableArray[archposx[i]][archposy[i]] = false;
                            availableArray[archposx[i]+1][archposy[i]] = false;
                            availableArray[archposx[i]+1][archposy[i]-1] = false;
                            availableArray[archposx[i]][archposy[i]-1] = false;
                        } else if (rs2.getString(0) == "B" || rs2.getString(0) == "b"){
                            //Mark 2*3 area
                            availableArray[archposx[i]][archposy[i]] = false;
                            availableArray[archposx[i]+1][archposy[i]] = false;
                            availableArray[archposx[i]+1][archposy[i]-1] = false;
                            availableArray[archposx[i]][archposy[i]-1] = false;
                            availableArray[archposx[i]][archposy[i]-2] = false;
                            availableArray[archposx[i]+1][archposy[i]-2] = false;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Land", "initLands: "+ex.toString());
        }
    }

    public Boolean[][] getAvailableLand(Boolean isBig){

        //Search for available lands.
        if(isBig){
            //Search for 2*3 area
        for(int i=0; i<5; i++){
            for(int j=2; j<6; j++){
                if(landElements[i][j].getContent() != null){
                    availableArray[i][j] = false;
                }
            }
        }
        } else {
            //Search for 2*2 area
            for(int i=0; i<5; i++){
                for(int j=1; j<6; j++){
                    if(landElements[i][j].getContent() != null){
                        availableArray[i][j] = false;
                    }
                }
            }
        }
        return availableArray;
    }




    public class LandElement {
        private String content=null;
        private Boolean isHead=false;
        private int width=2;
        //public ImageView landImg;

        public void initContent(String con, int wid){
            content = con;
            width = wid;
        }
        public String getContent(){
            return content;
        }
        public int getWidth(){
            return width;
        }

        /*public void setViewId(View rootview, int idAddress){
            Log.d("Land Class", "setViewId: "+idAddress);
            //landImg = new ImageView(rootview.getContext());
            ImageView landImg = (ImageView)rootview.findViewById(idAddress);
        }*/
    }
}
