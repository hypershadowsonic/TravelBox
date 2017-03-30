package com.edge.work.travelbox;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.internal.zzs.TAG;


public class MyislandShopFragment extends Fragment {

    TextView time, name1, name2, name3, price1, price2, price3;
    ImageView thumb1, thumb2, thumb3;
    CardView dec1,dec2,dec3;
    ProgressBar progressBar;
    CountDownTimer countDownTimer, halfTimer;
    Boolean fullt=false;
    Boolean halft=false;
    private ConnectionClass connectionClass = new ConnectionClass();

    public MyislandShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_shop, container, false);
        time = (TextView)rootview.findViewById(R.id.shop_chouter_text);
        progressBar = (ProgressBar)rootview.findViewById(R.id.shop_counter_bar);
        name1 = (TextView)rootview.findViewById(R.id.shop_dec_1_name);
        name2 = (TextView)rootview.findViewById(R.id.shop_dec_2_name);
        name3 = (TextView)rootview.findViewById(R.id.shop_dec_3_name);
        price1 = (TextView)rootview.findViewById(R.id.shop_dec_1_price);
        price2 = (TextView)rootview.findViewById(R.id.shop_dec_2_price);
        price3 = (TextView)rootview.findViewById(R.id.shop_dec_3_price);
        thumb1 = (ImageView)rootview.findViewById(R.id.shop_dec_1_img);
        thumb2 = (ImageView)rootview.findViewById(R.id.shop_dec_2_img);
        thumb3 = (ImageView)rootview.findViewById(R.id.shop_dec_3_img);
        dec1 = (CardView)rootview.findViewById(R.id.shop_dec_1);
        dec2 = (CardView)rootview.findViewById(R.id.shop_dec_2);
        dec3 = (CardView)rootview.findViewById(R.id.shop_dec_3);
        final String FORMAT = "%02d:%02d";
        final SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);

        countDownTimer = new CountDownTimer(360000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(""+String.format(FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),  TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                progressBar.setProgress((int) millisUntilFinished/1000);
                Log.d("full timer", "tick");
                fullt=true;
            }

            @Override
            public void onFinish() {
                updateDecinSQL();
                showDeconScreen(name1,name2,name3,price1,price2,price3,thumb1,thumb2,thumb3,dec1,dec2,dec3);
                ContentValues args = new ContentValues();
                args.put("lastupdate",""+System.currentTimeMillis());
                sqLiteDatabase.update("DecTemp",args,null,null);
                start();
            }
        };

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DecTemp(slot1 TEXT, slot2 TEXT, slot3 TEXT, lastupdate LONG);");
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM DecTemp",null);
        if (cursor.moveToNext()){
            Log.d("Shop", "Row Exists");
            if(System.currentTimeMillis()-cursor.getLong(cursor.getColumnIndex("lastupdate"))>360000){
                Log.d("Shop", "Last update was earlier than 6 mins");
                updateDecinSQL();
                showDeconScreen(name1,name2,name3,price1,price2,price3,thumb1,thumb2,thumb3,dec1,dec2,dec3);
                ContentValues args = new ContentValues();
                args.put("lastupdate",""+System.currentTimeMillis());
                sqLiteDatabase.update("DecTemp",args,null,null);
                countDownTimer.start();
            } else {
                Log.d("Shop", "Last Update was in 6 mins");
                showDeconScreen(name1,name2,name3,price1,price2,price3,thumb1,thumb2,thumb3,dec1,dec2,dec3);
                halfTimer = new CountDownTimer(360000-(System.currentTimeMillis()-cursor.getLong(cursor.getColumnIndex("lastupdate"))),1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        time.setText(""+String.format(FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),  TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        progressBar.setProgress((int) millisUntilFinished/1000);
                        Log.d("half timer", "tick");
                        halft=true;
                    }

                    @Override
                    public void onFinish() {
                        updateDecinSQL();
                        showDeconScreen(name1,name2,name3,price1,price2,price3,thumb1,thumb2,thumb3,dec1,dec2,dec3);
                        ContentValues args = new ContentValues();
                        args.put("lastupdate",""+System.currentTimeMillis());
                        sqLiteDatabase.update("DecTemp",args,null,null);
                        halft=false;
                        countDownTimer.start();
                    }
                }.start();
            }
        } else {
            Log.d("Shop", "Row Not Exists");
            sqLiteDatabase.execSQL("INSERT INTO DecTemp(slot1,slot2,slot3,lastupdate) VALUES('DEC01', 'DEC02', 'DEC03', '0');");
            showDeconScreen(name1,name2,name3,price1,price2,price3,thumb1,thumb2,thumb3,dec1,dec2,dec3);
            countDownTimer.start();
        }


        //sqLiteDatabase.close();
        return rootview;
    }

    public void updateDecinSQL(){
        SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT decid FROM Decoration ORDER BY RANDOM() LIMIT 3;",null);
        for (int i=1;i<=3;i++) {
            cursor.moveToNext();
            ContentValues args = new ContentValues();
            args.put("slot"+i,cursor.getString(cursor.getColumnIndex("decid")));
            sqLiteDatabase.update("DecTemp",args,null,null);
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public void showDeconScreen(TextView name1, TextView name2, TextView name3, TextView price1, TextView price2, TextView price3, ImageView thumb1, ImageView thumb2, ImageView thumb3, final CardView dec1, final CardView dec2, final CardView dec3){
        final SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM DecTemp",null);
        while (cursor.moveToNext()){
            Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM Decoration WHERE decid='"+cursor.getString(cursor.getColumnIndex("slot1"))+"';",null);
            cursor1.moveToFirst();
            name1.setText(cursor1.getString(cursor1.getColumnIndex("name")));
            price1.setText(""+cursor1.getInt(cursor1.getColumnIndex("price")));
            ImageLoader.getInstance().displayImage(cursor1.getString(cursor1.getColumnIndex("thumb")),thumb1);
            dec1.setTag(cursor.getString(cursor.getColumnIndex("slot1")));
            final int p1 = cursor1.getInt(cursor1.getColumnIndex("price"));
            dec1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Title")
                            .setMessage(getString(R.string.buy_dec))
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getActivity(), getString(R.string.unlock), Toast.LENGTH_SHORT).show();
                                    StatusFragment.coinPaySave(false,p1);
                                    SQLiteDatabase sqLite = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                                    sqLite.execSQL("INSERT INTO UserArch(ownerid, arch, archlv) VALUES('"+TravelBox.userId+"', '"+dec1.getTag()+"', 0);");
                                    updateServer((String)dec1.getTag());
                                    dec1.setVisibility(View.GONE);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            cursor1.close();

            cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM Decoration WHERE decid='"+cursor.getString(cursor.getColumnIndex("slot2"))+"';",null);
            cursor1.moveToFirst();
            name2.setText(cursor1.getString(cursor1.getColumnIndex("name")));
            price2.setText(""+cursor1.getInt(cursor1.getColumnIndex("price")));
            ImageLoader.getInstance().displayImage(cursor1.getString(cursor1.getColumnIndex("thumb")),thumb2);
            dec2.setTag(cursor.getString(cursor.getColumnIndex("slot2")));
            final int p2 = cursor1.getInt(cursor1.getColumnIndex("price"));
            dec2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Title")
                            .setMessage(getString(R.string.buy_dec))
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getActivity(), getString(R.string.unlock), Toast.LENGTH_SHORT).show();
                                    StatusFragment.coinPaySave(false,p2);
                                    SQLiteDatabase sqLite = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                                    sqLite.execSQL("INSERT INTO UserArch(ownerid, arch, archlv) VALUES('"+TravelBox.userId+"', '"+dec2.getTag()+"', 0);");
                                    updateServer((String)dec2.getTag());
                                    dec1.setVisibility(View.GONE);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            cursor1.close();

            cursor1 = sqLiteDatabase.rawQuery("SELECT * FROM Decoration WHERE decid='"+cursor.getString(cursor.getColumnIndex("slot3"))+"';",null);
            cursor1.moveToFirst();
            name3.setText(cursor1.getString(cursor1.getColumnIndex("name")));
            price3.setText(""+cursor1.getInt(cursor1.getColumnIndex("price")));
            ImageLoader.getInstance().displayImage(cursor1.getString(cursor1.getColumnIndex("thumb")),thumb3);
            dec3.setTag(cursor.getString(cursor.getColumnIndex("slot3")));
            final int p3 = cursor1.getInt(cursor1.getColumnIndex("price"));
            dec3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Title")
                            .setMessage(getString(R.string.buy_dec))
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getActivity(), getString(R.string.unlock), Toast.LENGTH_SHORT).show();
                                    StatusFragment.coinPaySave(false,p3);
                                    SQLiteDatabase sqLite = getActivity().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                                    sqLite.execSQL("INSERT INTO UserArch(ownerid, arch, archlv) VALUES('"+TravelBox.userId+"', '"+dec3.getTag()+"', 0);");
                                    updateServer((String)dec3.getTag());
                                    dec1.setVisibility(View.GONE);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            cursor1.close();
        }
        cursor.close();
        sqLiteDatabase.close();
    }


    @Override
    public void onPause() {
        if (fullt){
            countDownTimer.cancel();}
        if (halft){
            halfTimer.cancel();}
        super.onPause();
    }

    private void updateServer(String decid){
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Log.e("SQL", "Unable to connect to server");
            } else {
                String query = "INSERT INTO UserArch(ownerid, arch, archlv) VALUES('"+TravelBox.userId+"', '"+decid+"', 0);";
                Log.d(TAG, "updateServer: "+query);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
            }
        } catch (Exception ex){
            Log.e(TAG, "doInBackground: "+ex.toString());
        }
    }
}
