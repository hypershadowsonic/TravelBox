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

public class CollectionItemData {

    private static ConnectionClass connectionClass = new ConnectionClass();


    public static ArrayList<PlaceCard> getData(Context context, String category, String parentID){
        ArrayList<PlaceCard> data = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
            if (category.endsWith("x")) {
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT CollectionItem.placeid, ShopInfo.thumblv1, ShopInfo.name, ShopInfo.price " +
                        "FROM CollectionItem, ShopInfo " +
                        "WHERE CollectionItem.placeid=ShopInfo.shopid AND CollectionItem.parentid='"+parentID+"';",null);
                while (cursor.moveToNext()){
                    PlaceCard current = new PlaceCard();
                    current.category="x";
                    current.imgUrl=cursor.getString(cursor.getColumnIndex("thumblv1"));
                    current.placeID=cursor.getString(cursor.getColumnIndex("placeid"));
                    current.itemName=cursor.getString(cursor.getColumnIndex("name"));
                    current.price=cursor.getString(cursor.getColumnIndex("price"));
                    data.add(current);
                    Log.d("ItemData", "getData: "+"x"+parentID+current.imgUrl);
                }
                cursor.close();
                sqLiteDatabase.close();
            } else if (category.endsWith("y")) {
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT CollectionItem.placeid, PlaceInfo.thumb, PlaceInfo.name " +
                        "FROM CollectionItem, PlaceInfo " +
                        "WHERE CollectionItem.placeid=PlaceInfo.placeid AND CollectionItem.parentid='"+parentID+"';",null);
                while (cursor.moveToNext()){
                    PlaceCard current = new PlaceCard();
                    current.category="y";
                    current.imgUrl=cursor.getString(cursor.getColumnIndex("thumb"));
                    current.itemName=cursor.getString(cursor.getColumnIndex("name"));
                    current.placeID=cursor.getString(cursor.getColumnIndex("placeid"));
                    data.add(current);
                    Log.d("ItemData", "getData: "+"y"+parentID+current.imgUrl);
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        } catch (Exception ex){
            Log.e("CollectionItemData", ex.toString());
        }
        return data;
    }
}
