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

public class CollectionTitleData {

    private static ConnectionClass connectionClass = new ConnectionClass();

    public static ArrayList<CollectionTitleInfo> getData(Context context){
        ArrayList<CollectionTitleInfo> data = new ArrayList<>();

        try {
            Connection con = connectionClass.CONN();
            if(con==null){
                Log.e("CollectionTitleData", "Unable to connect to server");
            } else{

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CollectionTitle WHERE counties='"+TravelBox.activeCounty+"';");

                while(rs.next()){
                    CollectionTitleInfo current = new CollectionTitleInfo();
                    current.collectionID = rs.getString("collectionid");
                    current.collectionName = rs.getString("name");
                    current.titleImg = rs.getString("titleimg");
                    data.add(current);
                }
            }
        } catch (Exception ex){
            Log.e("CollectionTitleData", ex.toString());
        }

        return data;
    }

}
