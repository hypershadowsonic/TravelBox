package com.edge.work.travelbox;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.telecom.Connection;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

public class LoadingActivity extends Activity {

    private ImageView jumpingHead;
    private Animation loadingJumpAnim;
    private TextView loadingText;

    private ConnectionClass connectionClass = new ConnectionClass();

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String userID, userEmail, userName, userBday, userPicurl, userGender;


    private Intent k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        //AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_loading);
        jumpingHead = (ImageView)findViewById(R.id.loading_img_jumpinghead);
        loadingText = (TextView)findViewById(R.id.loading_text_loading);
        loadingJumpAnim = AnimationUtils.loadAnimation(this, R.anim.loading_jump);
        loadingJumpAnim.setFillAfter(true);
        loadingJumpAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpingHead.startAnimation(loadingJumpAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        jumpingHead.startAnimation(loadingJumpAnim);
        k = new Intent(this,MainActivity.class);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},0);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.loading_btn_login);
        loginButton.setReadPermissions(Arrays.asList("user_birthday","read_custom_friendlists"));
        //loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                jumpingHead.setVisibility(View.VISIBLE);
                loadingText.setVisibility(View.VISIBLE);

                accessToken = loginResult.getAccessToken();
                Log.d("Facebook","Access Token Got.");

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                userID = object.optString("id");
                                TravelBox.userId=userID;
                                Log.d("Facebook", "onCompleted: Got ID "+ userID);
                                userEmail = object.optString("email");
                                Log.d("Facebook", "onCompleted: Got eMail "+ userEmail);
                                userName = object.optString("name");
                                Log.d("Facebook", "onCompleted: Got Name "+ userName);
                                userBday = object.optString("birthday");
                                Log.d("Facebook", "onCompleted: Got Birthday "+ userBday);
                                userGender = object.optString("gender");
                                Log.d("Facebook", "onCompleted: Got Gender "+ userGender);
                                userPicurl = object.optJSONObject("picture").optJSONObject("data").optString("url");
                                Log.d("Facebook", "onCompleted: Got Pic URL "+ userPicurl);
                                DoSync doSync = new DoSync();
                                doSync.execute("");
                                loadingText.setText(R.string.loading_server_sync);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,gender,picture{url}");
                request.setParameters(parameters);
                request.executeAsync();
                loadingText.setText(R.string.loading_facebook_fetching);
            }

            @Override
            public void onCancel() {
                Log.i("Facebook","Login Cancelled");
                loadingText.setText(R.string.loading_facebook_cancel);
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("Facebook","Login Error");
                loadingText.setText(R.string.loading_error);
            }
        });


        if(AccessToken.getCurrentAccessToken()!=null){
            //Update Facebook Data
            accessToken=AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Insert your code here
                            userID = object.optString("id");
                            TravelBox.userId=userID;
                            Log.d("Facebook", "onCompleted: Got ID "+ userID);
                            userEmail = object.optString("email");
                            Log.d("Facebook", "onCompleted: Got eMail "+ userEmail);
                            userName = object.optString("name");
                            Log.d("Facebook", "onCompleted: Got Name "+ userName);
                            userBday = object.optString("birthday");
                            Log.d("Facebook", "onCompleted: Got Birthday "+ userBday);
                            userGender = object.optString("gender");
                            Log.d("Facebook", "onCompleted: Got Gender "+ userGender);
                            userPicurl = object.optJSONObject("picture").optJSONObject("data").optString("url");
                            Log.d("Facebook", "onCompleted: Got Pic URL "+ userPicurl);
                            DoSync doSync = new DoSync();
                            loadingText.setText(R.string.loading_server_sync);
                            doSync.execute("");
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,birthday,gender,picture{url}");
            request.setParameters(parameters);
            loadingText.setText(R.string.loading_facebook_fetching);
            request.executeAsync();
        } else {
            loginButton.setVisibility(View.VISIBLE);
            jumpingHead.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }


    private class DoSync extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess) {
                startActivity(k);
                finish();
            } else {
                loadingText.setText(z);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(userID.trim().equals(""))
                z = "Please login to Facebook to continue";
            else
            {
                //Check user with server
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from Users where id='" + userID + "';";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            //User found, update info
                            TravelBox.coin = rs.getInt("amount_coin");
                            TravelBox.trophy = rs.getInt("amount_trophy");
                            stmt.executeUpdate("UPDATE Users SET name=N'"+userName+"', profileimg_url='"+userPicurl+"', email='"+userEmail+"', birthday='"+userBday+"', gender='"+userGender+"' WHERE id='"+userID+"';");
                            z = "Login successful";
                            isSuccess = true;
                        }
                        else
                        {
                            //User not found, create new user
                            stmt.executeUpdate("INSERT INTO Users (id, name, profileimg_url, email, birthday, gender) VALUES('"+userID+"',N'"+userName+"','"+userPicurl+"','"+userEmail+"','"+userBday+"','"+userGender+"');");
                            stmt.executeUpdate("INSERT INTO Island (ownerid, archcount) VALUES('"+userID+"', 0);");
                            Log.d("DoSync", "INSERT Successful");
                            z = "New user created";
                            isSuccess = true;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions: "+ex.toString();
                }



                //Download data from server
                try{
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        //Re-Create Users Table
                        SQLiteDatabase sqLiteDB = getBaseContext().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS Users;");
                        sqLiteDB.execSQL("CREATE TABLE Users(id TEXT, name TEXT, profileimg_url TEXT,amount_coin INTEGER, amount_trophy INTEGER, last_harvest BIGINT);");

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM Users;");
                        while(rs.next()){
                            sqLiteDB.execSQL("INSERT INTO Users(id, name, profileimg_url, amount_coin, amount_trophy, last_harvest) VALUES('"+rs.getString("id")+"', '"+rs.getString("name")+"', '"+rs.getString("profileimg_url")+"', "+rs.getInt("amount_coin")+", "+rs.getInt("amount_trophy")+", "+rs.getLong("last_harvest")+");");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "Users Done");



                        //Re-Create UserArch Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS UserArch;");
                        sqLiteDB.execSQL("CREATE TABLE UserArch(ownerid TEXT, arch TEXT, archlv INTEGER);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM UserArch;");
                        while(rs.next()){
                            sqLiteDB.execSQL("INSERT INTO UserArch(ownerid, arch, archlv) VALUES('"+rs.getString("ownerid")+"', '"+rs.getString("arch")+"', "+rs.getInt("archlv")+");");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "UserArch Done");



                        //Re-Create Island Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS Island;");
                        sqLiteDB.execSQL("CREATE TABLE Island(ownerid TEXT, arch TEXT, archposx TINYINT, archposy TINYINT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM Island;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO Island(ownerid, arch, archposx, archposy) VALUES('"+rs.getString("ownerid")+"', '"+rs.getString("arch")+"', "+rs.getInt("archposx")+", "+rs.getInt("archposy")+");");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "Island Done");



                        //TODO: Add info database version check
                        //Re-Create ShopInfo Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS ShopInfo;");
                        sqLiteDB.execSQL("CREATE TABLE ShopInfo(shopid CHARACTER(5), name TEXT, area CHARACTER(1), district CHARACTER(1), category CHARACTER(1), address TEXT, description TEXT, idea TEXT, businesshour TEXT, phone TEXT, price TEXT, popularity INTEGER, facebookurl TEXT, hash CHARACTER(32), thumblv1 TEXT, thumblv2 TEXT, thumblv3 TEXT, thumblv4 TEXT, thumblv5 TEXT, archlv1 TEXT, archlv2 TEXT, archlv3 TEXT, archlv4 TEXT, archlv5 TEXT, archtype CHARACTER(1), latitude FLOAT, longitude FLOAT, titleimg TEXT, gallery1 TEXT, gallery2 TEXT, gallery3 TEXT, gallery4 TEXT, gallery5 TEXT, gallery6 TEXT, notificationzone TEXT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM ShopInfo;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO ShopInfo(shopid , name , area, district, category, address, description, idea, businesshour, phone, price, popularity, facebookurl, hash, thumblv1, thumblv2, thumblv3, thumblv4, thumblv5, archlv1, archlv2, archlv3, archlv4, archlv5, archtype , latitude, longitude, titleimg, gallery1, gallery2, gallery3, gallery4, gallery5, gallery6, notificationzone) " +
                                    "VALUES('"+rs.getString("shopid")+"', '"+rs.getString("name")+"', '"+rs.getString("area")+"',' "+rs.getString("district")+"','"+rs.getString("category")+"','"+rs.getString("address")+"','"+rs.getString("description")+"','"+rs.getString("idea")+"','"+rs.getString("businesshour")+"','"+rs.getString("phone")+"','"+rs.getString("price")+"','"+rs.getInt("popularity")+"','"+rs.getString("facebookurl")+"','"+rs.getString("hash")+"','"+rs.getString("thumblv1")+"','"+rs.getString("thumblv2")+"','"+rs.getString("thumblv3")+"','"+rs.getString("thumblv4")+"','"+rs.getString("thumblv5")+"','"+rs.getString("archlv1")+"','"+rs.getString("archlv2")+"','"+rs.getString("archlv3")+"','"+rs.getString("archlv4")+"','"+rs.getString("archlv5")+"','"+rs.getString("archtype")+"','"+rs.getFloat("latitude")+"','"+rs.getFloat("longitude")+"','"+rs.getString("titleimg")+"','"+rs.getString("gallery1")+"','"+rs.getString("gallery2")+"','"+rs.getString("gallery3")+"','"+rs.getString("gallery4")+"','"+rs.getString("gallery5")+"','"+rs.getString("gallery6")+"','"+rs.getString("notificationzone")+"');");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "ShopInfo Done");



                        //Re-Create Place Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS PlaceInfo;");
                        sqLiteDB.execSQL("CREATE TABLE PlaceInfo(placeid TEXT, name TEXT, address TEXT, description TEXT, businesshour TEXT, area CHARACTER(1), district CHARACTER(1), category CHARACTER(1), zone CHARACTER(1), thumb TEXT, titleimg TEXT, facebookurl TEXT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM PlaceInfo;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO PlaceInfo(placeid, name, address, description, businesshour, area, district, category, zone, thumb, titleimg, facebookurl) " +
                                    "VALUES('"+rs.getString("placeid")+"', '"+rs.getString("name")+"', '"+rs.getString("address")+"',' "+rs.getString("description")+"','"+rs.getString("businesshour")+"','"+rs.getString("area")+"','"+rs.getString("district")+"','"+rs.getString("category")+"','"+rs.getString("zone")+"','"+rs.getString("thumb")+"','"+rs.getString("titleimg")+"','"+rs.getString("facebookurl")+"');");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "PlaceInfo Done");



                        //Re-Create Collection Title Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS CollectionTitle;");
                        sqLiteDB.execSQL("CREATE TABLE CollectionTitle(collectionid TEXT, name TEXT, titleimg TEXT, counties TEXT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM CollectionTitle;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO CollectionTitle(collectionid, name, titleimg, counties) VALUES('"+rs.getString("collectionid")+"', '"+rs.getString("name")+"', '"+rs.getString("titleimg")+"','"+rs.getString("counties")+"');");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "CollectionTitle Done");



                        //Re-Create Collection Item Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS CollectionItem;");
                        sqLiteDB.execSQL("CREATE TABLE CollectionItem(parentid TEXT, category TEXT, placeid TEXT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM CollectionItem;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO CollectionItem(parentid, category, placeid) VALUES('"+rs.getString("parentid")+"', '"+rs.getString("category")+"','"+rs.getString("placeid")+"');");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "CollectionItem Done");



                        //Re-Create Articles Table
                        sqLiteDB.execSQL("DROP TABLE IF EXISTS Articles;");
                        sqLiteDB.execSQL("CREATE TABLE Articles(title TEXT, url TEXT, thumb TEXT, shopid TEXT);");

                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM Articles;");
                        while (rs.next()){
                            sqLiteDB.execSQL("INSERT INTO Articles(title, url, thumb, shopid) VALUES('"+rs.getString("title")+"', '"+rs.getString("url")+"', '"+rs.getString("thumb")+"', '"+rs.getString("shopid")+"');");
                        }
                        stmt.close();
                        rs.close();
                        Log.d("Syncing", "Articles Done");
                        sqLiteDB.close();
                        Log.d("Syncing", "All Done");
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Syncing User Exceptions: "+ex.toString();
                }
            }

            Log.d("DoSync", z);
            return z;
        }
    }
}

