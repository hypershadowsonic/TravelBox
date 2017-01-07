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


    public static ArrayList<UserIslandListInfo> getData(Context context){
        ArrayList<UserIslandListInfo> data = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDB = context.openOrCreateDatabase("Local_Data.db", context.MODE_PRIVATE, null);
            Cursor cursor = sqLiteDB.rawQuery("SELECT * FROM UserArch WHERE ownerid='"+ TravelBox.userId +"';",null);
            while(cursor.moveToNext()){
                Cursor cursor1 = sqLiteDB.rawQuery("SELECT * FROM Island WHERE ownerid='" + TravelBox.userId + "' AND arch='" + cursor.getString(cursor.getColumnIndex("arch")) + "';",null);
                if (cursor1.moveToNext() == false){
                    Cursor cursor2 = sqLiteDB.rawQuery("SELECT thumblv"+cursor.getString(cursor.getColumnIndex("archlv"))+",archtype FROM ShopInfo WHERE shopid='"+cursor.getString(cursor.getColumnIndex("arch"))+"';",null);
                    if (cursor2.moveToNext()){
                        UserIslandListInfo current = new UserIslandListInfo();
                        current.type = cursor2.getString(cursor2.getColumnIndex("archtype"));
                        current.url = cursor2.getString(cursor2.getColumnIndex("thumblv"+cursor.getString(cursor.getColumnIndex("archlv"))));
                        current.arch = cursor.getString(cursor.getColumnIndex("arch"));
                        Log.d("UserIslandList", "getData: 6");
                        data.add(current);

                    }
                    cursor2.close();
                }
                cursor1.close();
            }
            cursor.close();
            sqLiteDB.close();
        } catch (Exception ex){
            Log.e("UserIslandList", ex.toString());
        }


        return data;
    }
}
