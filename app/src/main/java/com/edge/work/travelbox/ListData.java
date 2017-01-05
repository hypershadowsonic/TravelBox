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
 * Created by Super on 12/31/2016.
 */

public class ListData {
//    private static ConnectionClass connectionClass = new ConnectionClass();

    public static ArrayList<PlaceCard> getData(Context context, String area){
        ArrayList<PlaceCard> data = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT name, thumblv1, price, shopid, district FROM ShopInfo WHERE area='"+area+"';",null);
            while (cursor.moveToNext()){
                PlaceCard current = new PlaceCard();
                current.category="x";
                current.imgUrl=cursor.getString(cursor.getColumnIndex("thumblv1"));
                current.placeID=cursor.getString(cursor.getColumnIndex("shopid"));
                current.itemName=cursor.getString(cursor.getColumnIndex("name"));
                current.price=cursor.getString(cursor.getColumnIndex("price"));
                current.district=cursor.getString(cursor.getColumnIndex("district"));
                data.add(current);
                Log.d("ItemData", "getData: "+"x"+area+current.imgUrl);
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception ex){
            Log.e("CollectionItemData", ex.toString());
        }
        return data;
    }
}
