package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Super on 12/30/2016.
 */

public class CollectionTitleData {

    private static ConnectionClass connectionClass = new ConnectionClass();

    public static ArrayList<CollectionTitleInfo> getData(Context context){
        ArrayList<CollectionTitleInfo> data = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM CollectionTitle WHERE counties='"+TravelBox.activeCounty+"';",null);
            while (cursor.moveToNext()){
                CollectionTitleInfo current = new CollectionTitleInfo();
                current.collectionID = cursor.getString(cursor.getColumnIndex("collectionid"));
                current.collectionName = cursor.getString(cursor.getColumnIndex("name"));
                current.titleImg = cursor.getString(cursor.getColumnIndex("titleimg"));
                data.add(current);
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception ex){
            Log.e("CollectionTitleData", ex.toString());
        }

        return data;
    }

}
