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
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from Users where id='" + userID + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        TravelBox.userId=userID;
                        SQLiteDatabase sqLiteDB = getBaseContext().openOrCreateDatabase("local-user.db", MODE_PRIVATE, null);

                        if(rs.next())
                        {
                            //User found, update info
                            TravelBox.coin = rs.getInt("amount_coin");
                            TravelBox.trophy = rs.getInt("amount_trophy");
                            sqLiteDB.execSQL("DROP TABLE IF EXISTS user");
                            sqLiteDB.execSQL("CREATE TABLE user(id TEXT, name TEXT, profileimg_url TEXT);");
                            sqLiteDB.execSQL("INSERT INTO user (id, name, profileimg_url) VALUES('"+userID+"','"+userName+"','"+userPicurl+"');");
                            stmt.executeUpdate("UPDATE Users SET name=N'"+userName+"', profileimg_url='"+userPicurl+"', email='"+userEmail+"', birthday='"+userBday+"', gender='"+userGender+"' WHERE id='"+userID+"';");
                            sqLiteDB.close();
                            z = "Login successful";
                            isSuccess = true;
                        }
                        else
                        {
                            //User not found, create new user
                            try {
                                stmt.executeUpdate("INSERT INTO Users (id, name, profileimg_url, email, birthday, gender) VALUES('"+userID+"',N'"+userName+"','"+userPicurl+"','"+userEmail+"','"+userBday+"','"+userGender+"');");
                                stmt.executeUpdate("INSERT INTO Island (ownerid, archcount) VALUES('"+userID+"', 0)");
                                sqLiteDB.execSQL("DROP TABLE IF EXISTS user");
                                sqLiteDB.execSQL("CREATE TABLE user(id TEXT, name TEXT, profileimg_url TEXT);");
                                sqLiteDB.execSQL("INSERT INTO user (id, name, profileimg_url) VALUES('"+userID+"','"+userName+"','"+userPicurl+"');");
                                sqLiteDB.close();
                                Log.d("DoSync", "INSERT Successful");
                            } catch (Exception ex) {
                                isSuccess = false;
                                z = "Creating user: "+ex.toString();
                            }

                            z = "New user created";
                            isSuccess = true;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex.toString();
                }
            }
            Log.d("DoSync", z);
            return z;
        }
    }
}

