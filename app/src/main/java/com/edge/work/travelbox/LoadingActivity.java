package com.edge.work.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.telecom.Connection;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoadingActivity extends Activity {

    private ImageView jumpingHead;
    private Animation loadingJumpAnim;

    private ConnectionClass connectionClass;

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_loading);
        jumpingHead = (ImageView)findViewById(R.id.loading_img_jumpinghead);
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

        loginButton = (LoginButton)findViewById(R.id.loading_btn_login);
        callbackManager = CallbackManager.Factory.create();
        if(AccessToken.getCurrentAccessToken()!=null){
            //proceed to main activity
        } else {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setReadPermissions("email");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Intent k = new Intent(LoadingActivity.this,MainActivity.class);
                    startActivity(k);
                    Log.i("Facebook","Login Succeeded");
                }

                @Override
                public void onCancel() {
                    Log.i("Facebook","Login Cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.i("Facebook","Login Error");
            }
        });}

        connectionClass = new ConnectionClass();
    }

/*
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        //String userid = edtuserid.getText().toString();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess) {
                Intent i = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals(""))
                z = "Please login to Facebook to continue";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from Users where User_ID='" + userid + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {

                            z = "Login successful";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }*/
}

