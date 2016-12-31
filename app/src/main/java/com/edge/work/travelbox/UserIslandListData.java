package com.edge.work.travelbox;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Super on 12/26/2016.
 */

public class UserIslandListData {

    private static ConnectionClass connectionClass = new ConnectionClass();


    public static ArrayList<UserIslandListInfo> getData(Context context){
        ArrayList<UserIslandListInfo> data = new ArrayList<>();
        String[] existArch = getExistArch();

        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("UserIslandList", "Unable to connect to server");
            } else{
                //Check if user has any Arch from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM UserArch WHERE ownerid='"+ TravelBox.userId +"';");

                while(rs.next()){
                    String shopid = rs.getString("arch");
                    Boolean notUsed = true;
                    for(int i=0;i<existArch.length;i++){
                        if(shopid.equals(existArch[i])){
                            notUsed = false;
                        }Log.d("UserIslandListData", shopid+","+existArch[i]+","+notUsed);
                    }
                    if(notUsed){
                        int lv = rs.getInt("archlv");
                        String exe = "SELECT thumblv"+lv+",archtype FROM ShopInfo WHERE shopid='"+shopid+"';";
                        Log.d("UserIslandList", exe);
                        Statement stmt2 = con.createStatement();
                        ResultSet cur = stmt2.executeQuery(exe);
                        if (cur.next()) {
                            UserIslandListInfo current = new UserIslandListInfo();
                            current.type = cur.getString("archtype");
                            current.url = cur.getString("thumblv" + lv);
                            current.arch = shopid;
                            data.add(current);
                        }
                    }
                }
            }
        } catch (Exception ex){
            Log.e("UserIslandList", ex.toString());
        }


        return data;
    }

    private static String[] getExistArch(){
        String[] existArch=new String[9];
        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("getExistArch: ", "Unable to connect to server");
            } else {
                //Check if user has any Arch on Island from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT archcount,arch0,arch1,arch2,arch3,arch4,arch5,arch6,arch7,arch8 FROM Island WHERE ownerid='" + TravelBox.userId + "';");
                if (rs.next()) {
                    for (int i=0;i<9;i++){
                        existArch[i]=rs.getString("arch"+i);
                        Log.d("UserIslandListDate", "getExistArch: "+existArch[i]);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("getExistArch: ", ex.toString());
        }
        return existArch;
    }
}
