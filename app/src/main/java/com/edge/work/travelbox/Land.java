package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import bolts.Bolts;

/**
 * Created by Super_000 on 12/22/2016.
 */

public class Land{

    private Boolean[][] availableArray = new Boolean[6][6];
    private Boolean[][] isHead = new Boolean[6][6];
    private Boolean[] fillfalse = new Boolean[]{false,false,false,false,false,false};
    private Boolean[] filltrue = new Boolean[]{true,true,true,true,true,true};
    public String[][] content = new String[6][6];
    private ConnectionClass connectionClass = new ConnectionClass();
    private String[] arch = new String[9];
    private static int[] archposx = new int[9];
    private static int[] archposy = new int[9];
    private Boolean isOpen = false;



    public Boolean[][] initLands(View rootview,Context context){
        //Initial all block to True and isHead to false
        for(int i=0;i<6;i++){
            Arrays.fill(isHead[i],false);
            Arrays.fill(availableArray[i],true);
        }

        //Get lands
        try{
            SQLiteDatabase sqLiteDB = context.openOrCreateDatabase("Local_Data.db", context.MODE_PRIVATE, null);
            Cursor cursor = sqLiteDB.rawQuery("SELECT arch, archposx, archposy FROM Island WHERE ownerid='" + TravelBox.userId + "';",null);
            while (cursor.moveToNext()) {
                //User has Arch, Get arch info (Island table)
                int x = cursor.getInt(cursor.getColumnIndex("archposx"));
                int y = cursor.getInt(cursor.getColumnIndex("archposy"));
                isHead[x][y] = true;
                content[x][y] = cursor.getString(cursor.getColumnIndex("arch"));
                Log.d("ishead", "set true" + cursor.getInt(cursor.getColumnIndex("archposx")) + cursor.getInt(cursor.getColumnIndex("archposy")));
                Log.d("Land", "initLands: Got Arch id and coordinate");

                //Get arch type (ShopInfo table)
                Cursor cursor1 = sqLiteDB.rawQuery("SELECT archtype FROM ShopInfo WHERE shopid='" + cursor.getString(cursor.getColumnIndex("arch")) + "';", null);
                if (cursor1.moveToNext()) {
                    if (cursor1.getString(cursor1.getColumnIndex("archtype")).contains("S") || cursor1.getString(cursor1.getColumnIndex("archtype")).contains("s")) {
                        //Mark 2*2 area
                        availableArray[x][y] = false;
                        availableArray[x + 1][y] = false;
                        availableArray[x + 1][y - 1] = false;
                        availableArray[x][y - 1] = false;
                    } else if (cursor1.getString(cursor1.getColumnIndex("archtype")).contains("B") || cursor1.getString(cursor1.getColumnIndex("archtype")).contains("b")) {
                        //Mark 2*3 area
                        availableArray[x][y] = false;
                        availableArray[x + 1][y] = false;
                        availableArray[x + 1][y - 1] = false;
                        availableArray[x][y - 1] = false;
                        availableArray[x][y - 2] = false;
                        availableArray[x + 1][y - 2] = false;
                    }
                    cursor1.close();
                    Log.d("Land", "initLands: Marked on availableArray");
                }
            }
        } catch (Exception ex) {
            Log.e("Land", "initLands: "+ex.toString());
        }
        return isHead;
    }

    public String getContent(int x, int y){
        return content[x][y];
    }

    public Boolean[][] getAvailableArray(){
        return availableArray;
    }

    public Boolean getAvailableArray(int x, int y){
        return availableArray[x][y];
    }

    public Boolean[][] scanAvailableLand(Boolean isBig){
        Boolean[][] temp = new Boolean[6][6];

        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                temp[i][j]=availableArray[i][j];
                Log.d("TAG", "Copying availableArray: "+temp[i][j]);
            }
        }
        //Mark impossible area
        for(int i=0;i<6;i++){
            availableArray[5][i]=false;
        }
        for(int i=0;i<6;i++){
            availableArray[i][0] = false;
        }

        //Search for available lands.
        if(isBig){
            //Search for 2*3 area
            for(int i=0; i<5; i++){
                for(int j=2; j<6; j++){
                    if(temp[i][j] == false || temp[i+1][j] == false || temp[i+1][j-1] == false || temp[i][j-1] == false || temp[i][j-2] == false || temp[i+1][j-2] == false){
                        availableArray[i][j] = false;
                    }
                }
            }
            //Mark extra impossible area for big arch
            for(int i=0; i<6; i++){
                availableArray[i][1] = false;
            }
        } else {
            //Search for 2*2 area
            for(int i=0; i<5; i++){
                for(int j=1; j<6; j++){
                    if(temp[i][j] == false || temp[i+1][j] == false || temp[i+1][j-1] == false || temp[i][j-1] == false){
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


}
