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
    private Boolean isOpen=false;



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
                //Get arch count from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT archcount FROM Island WHERE ownerid='"+id+"';");
                if (rs.getInt(0) == 0){
                   //User don't have any arch, do nothing
                } else {
                    //User has arches
                    for(int i=0; i<rs.getInt(0); i++){
                        //Get arch info from server (Island table)
                        ResultSet rs1 = stmt.executeQuery("SELECT arch"+i+", archposx"+i+", archposy"+i+" FROM Island WHERE ownerid='"+id+"';");
                        arch[i] = rs1.getString(i);
                        archposx[i] = rs1.getInt(i);
                        archposy[i] = rs1.getInt(i);
                        landElements[archposx[i]][archposy[i]].isHead = true;
                        landElements[archposx[i]][archposy[i]].content = arch[i];
                        //Get arch type from server (ShopInfo table)
                        ResultSet rs2 = stmt.executeQuery("SELECT archtype FROM ShopInfo WHERE id='"+arch[i]+"';");
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

    public Boolean[][] getAvailableArray(){
        return availableArray;
    }

    public Boolean getAvailableArray(int x, int y){
        return availableArray[x][y];
    }

    public Boolean[][] scanAvailableLand(Boolean isBig){

        //Search for available lands.
        if(isBig){
            //Search for 2*3 area
            for(int i=0; i<5; i++){
                for(int j=2; j<6; j++){
                    if(availableArray[i][j] == false || availableArray[i+1][j] == false || availableArray[i+1][j-1] == false || availableArray[i][j-1] == false || availableArray[i][j-2] == false || availableArray[i+1][j-2] == false){
                        availableArray[i][j] = false;
                    }
                }
            }
        } else {
            //Search for 2*2 area
            for(int i=0; i<5; i++){
                for(int j=1; j<6; j++){
                    if(availableArray[i][j] == false || availableArray[i+1][j] == false || availableArray[i+1][j-1] == false || availableArray[i][j-1] == false){
                        availableArray[i][j] = false;
                    }
                }
            }
        }

        return availableArray;
    }

    public void setIsOpen(Boolean setValue){
        isOpen = setValue;
    }

    public Boolean getIsOpen(){
        return isOpen;
    }

    public class LandElement {
        private String content=null;
        private Boolean isHead=false;

        public String getContent(){
            return content;
        }
        public Boolean getIsHead(){
            return isHead;
        }
    }
}
