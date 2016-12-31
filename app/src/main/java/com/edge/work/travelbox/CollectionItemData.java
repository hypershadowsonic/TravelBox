package com.edge.work.travelbox;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Super on 12/30/2016.
 */

public class CollectionItemData {

    private static ConnectionClass connectionClass = new ConnectionClass();


    public static ArrayList<PlaceCard> getData(Context context, String category, String parentID){
        ArrayList<PlaceCard> data = new ArrayList<>();

        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("CollectionItemData", "Unable to connect to server");
            } else{

                Statement stmt = con.createStatement();
                if (category.endsWith("x")) {
                    ResultSet rs = stmt.executeQuery("SELECT CollectionItem.placeid, ShopInfo.thumblv1, ShopInfo.name, ShopInfo.price " +
                            "FROM CollectionItem, ShopInfo " +
                            "WHERE CollectionItem.placeid=ShopInfo.shopid AND CollectionItem.parentid='"+parentID+"';");
                    while (rs.next()) {
                        PlaceCard current = new PlaceCard();
                        current.category="x";
                        current.imgUrl=rs.getString("thumblv1");
                        current.placeID=rs.getString("placeid");
                        current.itemName=rs.getString("name");
                        current.price=rs.getString("price");
                        data.add(current);
                        Log.d("ItemData", "getData: "+"x"+parentID+current.imgUrl);
                    }
                } else if (category.endsWith("y")) {
                    ResultSet rs = stmt.executeQuery("SELECT CollectionItem.placeid, PlaceInfo.thumb, PlaceInfo.name " +
                            "FROM CollectionItem, PlaceInfo " +
                            "WHERE CollectionItem.placeid=PlaceInfo.placeid AND CollectionItem.parentid='"+parentID+"';");
                    while (rs.next()) {
                        PlaceCard current = new PlaceCard();
                        current.category="y";
                        current.imgUrl=rs.getString("thumb");
                        current.itemName=rs.getString("name");
                        current.placeID=rs.getString("placeid");
                        data.add(current);
                        Log.d("ItemData", "getData: "+"y"+parentID+current.imgUrl);
                    }
                }
            }
        } catch (Exception ex){
            Log.e("CollectionItemData", ex.toString());
        }


        return data;
    }
}
