package com.edge.work.travelbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class AwardFragment extends Fragment {

    private ImageView box, property, light;
    private RelativeLayout layer1, layer2;
    private Animation boxmove,fadein,fadein2,fadeout,flyup;
    private String shopID, userID;
    private ConnectionClass connectionClass = new ConnectionClass();
    private FragmentManager fm;

    public AwardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_award, container, false);
        box = (ImageView) rootview.findViewById(R.id.award_anim_box);
        property = (ImageView) rootview.findViewById(R.id.award_anim_property);
        light = (ImageView) rootview.findViewById(R.id.award_anim_light);
        layer1 = (RelativeLayout) rootview.findViewById(R.id.award_anim_pt1);
        layer2 = (RelativeLayout) rootview.findViewById(R.id.award_anim_pt2);
        boxmove = AnimationUtils.loadAnimation(getContext(),R.anim.award_box_move);
        boxmove.setFillAfter(true);
        boxmove.setStartOffset(2000);
        boxmove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                layer1.startAnimation(fadeout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeout = new AlphaAnimation(1.0f,0.0f);
        fadeout.setDuration(1000);
        fadeout.setFillAfter(true);
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layer2.setVisibility(View.VISIBLE);
                layer2.startAnimation(fadein);
                Log.d("Animation", "Layer 2 anim act");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadein = new AlphaAnimation(0.0f, 1.0f);
        fadein.setDuration(1000);
        fadein.setFillAfter(true);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadein.setDuration(700);
                light.setVisibility(View.VISIBLE);
                light.startAnimation(fadein2);
                property.setVisibility(View.VISIBLE);
                property.startAnimation(flyup);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadein2 = new AlphaAnimation(0.0f, 1.0f);
        fadein2.setDuration(700);
        fadein2.setFillAfter(true);
        flyup = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,-0.7f);
        flyup.setDuration(1000);
        flyup.setFillAfter(true);
        flyup.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fm=getFragmentManager();
                Bundle bd = new Bundle();
                bd.putString("shopid",shopID);
                SurveyDialogFragment surveyF = new SurveyDialogFragment();
                surveyF.show(fm, "fragment_dialog_survey");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        SQLiteDatabase sqLiteDB = getActivity().getBaseContext().openOrCreateDatabase("local-user.db", Context.MODE_PRIVATE, null);
        Cursor query = sqLiteDB.rawQuery("SELECT id FROM user", null);
        if (query.moveToFirst()){
            //Get ID from SQLite
            userID = query.getString(0);
            Log.d("SQLite", "Got user ID");
            sqLiteDB.close();
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shopID = bundle.getString("id");
        }
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getContext(), "Unable to connect to server, please try again", Toast.LENGTH_LONG).show();
                fm.popBackStack();
            } else {
                Statement stmt = con.createStatement();
                //See if user already have this arch
                ResultSet rs = stmt.executeQuery("SELECT * FROM UserArch WHERE ownerid='"+userID+"' AND arch='"+shopID+"';");
                if (rs.next()){
                    //User has it, upgrade and get arch url
                    int lv = rs.getInt("archlv");
                    stmt.executeUpdate("UPDATE UserArch SET archlv="+(lv+1)+" WHERE ownerid='"+userID+"' AND arch='"+shopID+"';");
                    String query1 = "SELECT thumblv"+(lv+1)+" FROM ShopInfo WHERE shopid='"+shopID+"';";
                    ResultSet rs2 = stmt.executeQuery(query1);
                    if(rs2.next()){
                    String url=rs2.getString("thumblv"+(lv+1));

                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .build();
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                            .defaultDisplayImageOptions(defaultOptions)
                            .build();
                    ImageLoader.getInstance().init(config);
                    ImageLoader.getInstance().displayImage(url,property);}

                    box.startAnimation(boxmove);
                } else {
                    //User doesn't have it, create a record and set to lv1
                    stmt.executeUpdate("INSERT INTO UserArch VALUES('"+userID+"','"+shopID+"',1);");
                    //Get lv1 arch url
                    String query1 = "SELECT thumblv1 FROM ShopInfo WHERE shopid='"+shopID+"';";
                    ResultSet rs2 = stmt.executeQuery(query1);
                    if(rs2.next()){
                    String url=rs2.getString("thumblv1");

                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .build();
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                            .defaultDisplayImageOptions(defaultOptions)
                            .build();
                    ImageLoader.getInstance().init(config);
                    ImageLoader.getInstance().displayImage(url,property);}


                    box.startAnimation(boxmove);
                }
            }
        } catch (Exception ex){
            Toast.makeText(getContext(), "Exection: "+ex.toString(), Toast.LENGTH_LONG).show();
            Log.e("SQL", ex.toString() );
        }

        return rootview;
    }
}
