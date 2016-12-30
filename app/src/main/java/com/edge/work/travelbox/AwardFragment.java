package com.edge.work.travelbox;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
    private String shopID;
    private ConnectionClass connectionClass = new ConnectionClass();
    private FragmentManager fm;
    private String findpush;

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shopID = bundle.getString("id");
            findpush = bundle.getString("query");
        }
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getContext(), "Unable to connect to server, please try again", Toast.LENGTH_LONG).show();
                fm.popBackStack();
            } else {
                Statement stmt = con.createStatement();
                //See if user already have this arch
                ResultSet rs = stmt.executeQuery("SELECT * FROM UserArch WHERE ownerid='"+TravelBox.userId+"' AND arch='"+shopID+"';");
                if (rs.next()){
                    //User has it, upgrade and get arch url
                    int lv = rs.getInt("archlv");
                    stmt.executeUpdate("UPDATE UserArch SET archlv="+(lv+1)+" WHERE ownerid='"+TravelBox.userId+"' AND arch='"+shopID+"';");
                    String query1 = "SELECT thumblv"+(lv+1)+" FROM ShopInfo WHERE shopid='"+shopID+"';";
                    ResultSet rs2 = stmt.executeQuery(query1);
                    if(rs2.next()){
                    String url=rs2.getString("thumblv"+(lv+1));
                    ImageLoader.getInstance().displayImage(url,property);}

                    box.startAnimation(boxmove);
                } else {
                    //User doesn't have it, create a record and set to lv1
                    stmt.executeUpdate("INSERT INTO UserArch VALUES('"+TravelBox.userId+"','"+shopID+"',1);");
                    //Get lv1 arch url
                    String query1 = "SELECT thumblv1 FROM ShopInfo WHERE shopid='"+shopID+"';";
                    ResultSet rs2 = stmt.executeQuery(query1);
                    if(rs2.next()){
                    String url=rs2.getString("thumblv1");
                    ImageLoader.getInstance().displayImage(url,property);}


                    box.startAnimation(boxmove);
                }
            }
        } catch (Exception ex){
            Toast.makeText(getContext(), "Exection: "+ex.toString(), Toast.LENGTH_LONG).show();
            Log.e("SQL", ex.toString() );
        }


        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                fm.popBackStack();
            } else {
                Statement stmt = con.createStatement();
                //See if user already have this arch
                ResultSet rs = stmt.executeQuery(findpush);
                if (rs.next()) {
                    //Build notification content
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                    builder.setContentTitle("Travel Box 旅行盒子");
                    builder.setContentText("景點推薦："+rs.getString("name"));
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setTicker("你還可以看看這裡！");
                    builder.setAutoCancel(true);

                    //Intent
                    Intent i = new Intent(getActivity(), DialogActivity.class);
                    i.putExtra("name", rs.getString("name"))
                            .putExtra("address", rs.getString("address"))
                            .putExtra("description", rs.getString("description"))
                            .putExtra("buisnesshour", rs.getString("buisnesshour"))
                            .putExtra("titleimg", rs.getString("titleimg"))
                            .putExtra("facebookurl", rs.getString("facebookurl"));


                    //Back Stack
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                    stackBuilder.addParentStack(DialogActivity.class);
                    stackBuilder.addNextIntent(i);
                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(pendingIntent);

                    //Notify
                    Notification notification = builder.build();
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(1, notification);
                }
            }
        } catch (Exception ex){
            Log.e("SQL Push Notification", ex.toString() );
        }

        return rootview;
    }
}
