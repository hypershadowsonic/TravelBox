package com.edge.work.travelbox;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Super on 12/31/2016.
 */

public class ListData {
    private static ConnectionClass connectionClass = new ConnectionClass();

    public static ArrayList<PlaceCard> getData(Context context, String area){
        ArrayList<PlaceCard> data = new ArrayList<>();
        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("CollectionItemData", "Unable to connect to server");
            } else{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name, thumblv1, price, shopid, district FROM ShopInfo WHERE area='"+area+"';");
                while (rs.next()) {
                    PlaceCard current = new PlaceCard();
                    current.category="x";
                    current.imgUrl=rs.getString("thumblv1");
                    current.placeID=rs.getString("shopid");
                    current.itemName=rs.getString("name");
                    current.price=rs.getString("price");
                    current.district=rs.getString("district");
                    data.add(current);
                    Log.d("ItemData", "getData: "+"x"+area+current.imgUrl);
                }
            }
        } catch (Exception ex){
            Log.e("CollectionItemData", ex.toString());
        }
        return data;
    }
}
