package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private static String userID;


    public static ArrayList<UserIslandListInfo> getData(Context context){
        ArrayList<UserIslandListInfo> data = new ArrayList<>();

        SQLiteDatabase sqLiteDB = context.openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDB.rawQuery("SELECT id FROM user", null);
        if (query.moveToFirst()){
            //Get ID from SQLite
            userID = query.getString(0);
            Log.d("UserIslandList", "SQLite Got user ID");
            sqLiteDB.close();
        }

        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("UserIslandList", "Unable to connect to server");
            } else{
                //Check if user has any Arch from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM UserArch WHERE ownerid='"+userID+"';");
                Boolean first=true;
                if (rs.next()) {
                    int lv = rs.getInt("archlv");
                    String exe = "SELECT archtype, thumblv" + lv + " FROM ShopInfo WHERE shopid='" + rs.getString("arch") + "';";
                    Log.d("UserIslandList", exe);
                    ResultSet cur = stmt.executeQuery(exe);
                    Log.d("UserIslandList", "Row: " + cur.getRow());
                    UserIslandListInfo current = new UserIslandListInfo();
                    current.type = cur.getString("archtype");
                    current.url = cur.getString("thumblv"+lv);

                    data.add(current);
                }
                while(rs.next()){
                    int lv = rs.getInt("archlv");
                    String exe = "SELECT archtype, thumblv"+lv+" FROM ShopInfo WHERE shopid='"+rs.getString("arch")+"';";
                    Log.d("UserIslandList", exe);
                    ResultSet cur = stmt.executeQuery(exe);

                    UserIslandListInfo current = new UserIslandListInfo();
                    current.type = cur.getString("archtype");
                    current.url = cur.getString("thumblv"+lv);

                    data.add(current);
                }
            }
        } catch (Exception ex){
            Log.e("UserIslandList", ex.toString());
        }


        return data;
    }
}
