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

/**
 * Created by Super_000 on 12/22/2016.
 */

public class Land{

    //public LandElement[][] landElements = new LandElement[6][6];
    private Boolean[][] availableArray = new Boolean[6][6];
    private Boolean[][] isHead = new Boolean[6][6];
    private Boolean[] fillfalse = new Boolean[]{false,false,false,false,false,false};
    private Boolean[] filltrue = new Boolean[]{true,true,true,true,true,true};
    public String[][] content = new String[6][6];
    private ConnectionClass connectionClass = new ConnectionClass();
    private String userID = TravelBox.userId;
    private String[] arch = new String[9];
    private static int[] archposx = new int[9];
    private static int[] archposy = new int[9];
    private Boolean isOpen = false;



    public Boolean[][] initLands(View rootview){
        //Initial all block to True and isHead to false
        for(int i=0;i<6;i++){
            Arrays.fill(isHead[i],false);
            Arrays.fill(availableArray[i],true);
        }

        //Get lands from server
        try{
            Connection con = connectionClass.CONN();
            if (con == null){
                //Failed to connect to server
            } else {
                //Get arch count from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT archcount FROM Island WHERE ownerid='"+ userID +"';");
                if(rs.next()){
                    int archcount = rs.getInt("archcount");
                    if (archcount == 0){
                       //User don't have any arch, do nothing
                    } else {
                        //User has arches
                        for(int cur=0; cur<9; cur++){

                            //Get arch info from server (Island table)
                            ResultSet rs1 = stmt.executeQuery("SELECT arch"+cur+", archposx"+cur+", archposy"+cur+" FROM Island WHERE ownerid='"+ userID +"';");

                            if(rs1.next()){

                                arch[cur] = rs1.getString("arch"+cur);
                                archposx[cur] = rs1.getInt("archposx"+cur);
                                archposy[cur] = rs1.getInt("archposy"+cur);
                                Log.d("ishead", "set true"+archposx[cur]+archposy[cur]);

                                isHead[archposx[cur]][archposy[cur]]=true;
                                content[archposx[cur]][archposy[cur]]=arch[cur];

                                rs1.close();
                                Log.d("Land", "initLands: rs1 completed");
                            }

                            //Get arch type from server (ShopInfo table)
                            ResultSet rs2 = stmt.executeQuery("SELECT archtype FROM ShopInfo WHERE shopid='"+arch[cur]+"';");
                            if(rs2.next()){
                                if (rs2.getString("archtype").endsWith("S") || rs2.getString("archtype").endsWith("s")){
                                    //Mark 2*2 area
                                    availableArray[archposx[cur]][archposy[cur]] = false;
                                    availableArray[archposx[cur]+1][archposy[cur]] = false;
                                    availableArray[archposx[cur]+1][archposy[cur]-1] = false;
                                    availableArray[archposx[cur]][archposy[cur]-1] = false;
                                } else if (rs2.getString("archtype").endsWith("B") || rs2.getString("archtype").endsWith("b")){
                                    //Mark 2*3 area
                                    availableArray[archposx[cur]][archposy[cur]] = false;
                                    availableArray[archposx[cur]+1][archposy[cur]] = false;
                                    availableArray[archposx[cur]+1][archposy[cur]-1] = false;
                                    availableArray[archposx[cur]][archposy[cur]-1] = false;
                                    availableArray[archposx[cur]][archposy[cur]-2] = false;
                                    availableArray[archposx[cur]+1][archposy[cur]-2] = false;
                                }
                                rs2.close();
                                Log.d("Land", "initLands: rs2 completed");
                            }
                        }

                    }
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
