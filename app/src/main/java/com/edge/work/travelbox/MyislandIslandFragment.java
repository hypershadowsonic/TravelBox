package com.edge.work.travelbox;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;


public class MyislandIslandFragment extends Fragment {

    private static ImageView[][] land = new ImageView[6][6];
    private static Land landProp = new Land();
//    private View.OnClickListener mListener;
    private ConnectionClass connectionClass = new ConnectionClass();
    private ImageView[] iv=new ImageView[66];
    private FrameLayout fl;
    private RecyclerView recyclerView;
    private UserIslandListAdapter adapter;
    private LinearLayoutManager llm;
    private static String currentShopId;
    private RelativeLayout archDetail;
    private ImageView[] archDetailImg = new ImageView[4];
    private TextView archDetailName;
    private ImageView archDetailClose, archDetailInfo, archDetailDelete, activeDetailArch, btnBuild, btnShare, btnHarvest, harvest_glow, btnLock;
    private ArchBundle activebundle;
    private FragmentManager fm;
    private Boolean buildOn=false;
    private Animation fadein, fadeout, glow;
    private LinearLayout buildContainer;
    private long lastHarvest, harvestSeconds;
    private DonutProgress harvestProgress;
    private int scoreAmount =0;



    public MyislandIslandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_myisland_island, container, false);
        fm=getParentFragment().getFragmentManager();

        //Need to figure a better way of these
        land[0][0]=(ImageView) rootview.findViewById(R.id.land1_1);
        land[0][1]=(ImageView) rootview.findViewById(R.id.land1_2);
        land[0][2]=(ImageView) rootview.findViewById(R.id.land1_3);
        land[0][3]=(ImageView) rootview.findViewById(R.id.land1_4);
        land[0][4]=(ImageView) rootview.findViewById(R.id.land1_5);
        land[0][5]=(ImageView) rootview.findViewById(R.id.land1_6);

        land[1][0]=(ImageView) rootview.findViewById(R.id.land2_1);
        land[1][1]=(ImageView) rootview.findViewById(R.id.land2_2);
        land[1][2]=(ImageView) rootview.findViewById(R.id.land2_3);
        land[1][3]=(ImageView) rootview.findViewById(R.id.land2_4);
        land[1][4]=(ImageView) rootview.findViewById(R.id.land2_5);
        land[1][5]=(ImageView) rootview.findViewById(R.id.land2_6);

        land[2][0]=(ImageView) rootview.findViewById(R.id.land3_1);
        land[2][1]=(ImageView) rootview.findViewById(R.id.land3_2);
        land[2][2]=(ImageView) rootview.findViewById(R.id.land3_3);
        land[2][3]=(ImageView) rootview.findViewById(R.id.land3_4);
        land[2][4]=(ImageView) rootview.findViewById(R.id.land3_5);
        land[2][5]=(ImageView) rootview.findViewById(R.id.land3_6);

        land[3][0]=(ImageView) rootview.findViewById(R.id.land4_1);
        land[3][1]=(ImageView) rootview.findViewById(R.id.land4_2);
        land[3][2]=(ImageView) rootview.findViewById(R.id.land4_3);
        land[3][3]=(ImageView) rootview.findViewById(R.id.land4_4);
        land[3][4]=(ImageView) rootview.findViewById(R.id.land4_5);
        land[3][5]=(ImageView) rootview.findViewById(R.id.land4_6);

        land[4][0]=(ImageView) rootview.findViewById(R.id.land5_1);
        land[4][1]=(ImageView) rootview.findViewById(R.id.land5_2);
        land[4][2]=(ImageView) rootview.findViewById(R.id.land5_3);
        land[4][3]=(ImageView) rootview.findViewById(R.id.land5_4);
        land[4][4]=(ImageView) rootview.findViewById(R.id.land5_5);
        land[4][5]=(ImageView) rootview.findViewById(R.id.land5_6);

        land[5][0]=(ImageView) rootview.findViewById(R.id.land6_1);
        land[5][1]=(ImageView) rootview.findViewById(R.id.land6_2);
        land[5][2]=(ImageView) rootview.findViewById(R.id.land6_3);
        land[5][3]=(ImageView) rootview.findViewById(R.id.land6_4);
        land[5][4]=(ImageView) rootview.findViewById(R.id.land6_5);
        land[5][5]=(ImageView) rootview.findViewById(R.id.land6_6);

        harvestProgress = (DonutProgress)rootview.findViewById(R.id.progress_countdown);
        harvest_glow = (ImageView) rootview.findViewById(R.id.myisland_harvest_glow);
        harvest_glow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        glow = new AlphaAnimation(0.0f,1.0f);
        glow.setDuration(1500);
        glow.setInterpolator(new AccelerateDecelerateInterpolator());
        glow.setRepeatMode(Animation.REVERSE);
        glow.setRepeatCount(Animation.INFINITE);

        fadein = new AlphaAnimation(0.0f, 1.0f);
        fadein.setDuration(500);
        fadein.setFillAfter(true);
        fadeout = new AlphaAnimation(1.0f, 0.0f);
        fadeout.setDuration(500);
        fadeout.setFillAfter(true);

        buildContainer = (LinearLayout) rootview.findViewById(R.id.build_container);
        btnBuild = (ImageView) rootview.findViewById(R.id.btn_myisland_build);
        btnBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buildOn){
                    buildContainer.startAnimation(fadeout);
                    buildContainer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    buildOn=false;
                } else {
                    buildContainer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    buildContainer.startAnimation(fadein);
                    buildOn=true;
                }
            }
        });
        btnShare = (ImageView) rootview.findViewById(R.id.btn_myisland_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });

        btnHarvest = (ImageView) rootview.findViewById(R.id.myisland_btn_harvest);
        btnHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHarvest();
            }
        });

        btnLock = (ImageView) rootview.findViewById(R.id.myisland_detail_lock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activebundle.level == 4){
                    //Level 4, Confirm upgrade
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Title")
                            .setMessage(getString(R.string.confirm_unlock))
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getActivity(), getString(R.string.unlock), Toast.LENGTH_SHORT).show();
                                    float scale = getContext().getResources().getDisplayMetrics().density;
                                    btnLock.getLayoutParams().height = (int) (86 * scale + 0.5f);
                                    btnLock.getLayoutParams().width = (int) (86 * scale + 0.5f);
                                    ImageLoader.getInstance().displayImage(getThumbURL(activebundle.shopid,5),btnLock);
                                    unlockLvFive(activebundle.shopid);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        archDetail = (RelativeLayout)rootview.findViewById(R.id.myisland_detail);
        archDetailClose = (ImageView) rootview.findViewById(R.id.myisland_detail_close);
        archDetailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archDetail.setVisibility(View.GONE);
            }
        });
        archDetailInfo = (ImageView) rootview.findViewById(R.id.myisland_detail_info);
        archDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to info page
                Bundle bundle = new Bundle();
                bundle.putString("shopid",activebundle.shopid);
                bundle.putString("category","x");
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.setArguments(bundle);

                fm.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_container, infoFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });
        archDetailDelete = (ImageView) rootview.findViewById(R.id.myisland_detail_delete);
        archDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeDetailArch.setImageDrawable(null);
                archDetail.setVisibility(View.GONE);
                deleteArch(activebundle,rootview);

            }
        });
        archDetailImg[0] = (ImageView) rootview.findViewById(R.id.myisland_detail_lv1);
        archDetailImg[1] = (ImageView) rootview.findViewById(R.id.myisland_detail_lv2);
        archDetailImg[2] = (ImageView) rootview.findViewById(R.id.myisland_detail_lv3);
        archDetailImg[3] = (ImageView) rootview.findViewById(R.id.myisland_detail_lv4);
        archDetailName = (TextView) rootview.findViewById(R.id.myisland_detail_name);

        fl = (FrameLayout)rootview.findViewById(R.id.island_container);
        Boolean[][] isHead = landProp.initLands(rootview);
        initShowAllArch(isHead);

        recyclerView = (RecyclerView)rootview.findViewById(R.id.arch_recyclerview);
        adapter = new UserIslandListAdapter(getContext(), UserIslandListData.getData(getContext()));
        recyclerView.setAdapter(adapter);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(llm);

        View.OnTouchListener changeColorListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
                if (event.getX() < bmp.getWidth() && event.getY() < bmp.getHeight() && event.getX() >= 0 && event.getY() >= 0) {
                    int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                    if (color == Color.TRANSPARENT)
                        return false;
                    else {
                        int x = Character.getNumericValue(view.getTag().toString().charAt(0));
                        int y = Character.getNumericValue(view.getTag().toString().charAt(1));

                        Log.d("Land", "onClick: " + x + "," + y);
                        if (landProp.getIsOpen()) {
                            if (landProp.getAvailableArray(x, y)) {
                                pickStatusClose(rootview, x, y, true);
                            } else {
                                pickStatusClose(rootview, x, y, false);
                            }
                        }

                        return true;
                    }
                }
                return false;
            }
        };
        /*mListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Character.getNumericValue(view.getTag().toString().charAt(0));
                int y = Character.getNumericValue(view.getTag().toString().charAt(1));

                Log.d("Land", "onClick: "+x+","+y);
                if(landProp.getIsOpen()) {
                    if (landProp.getAvailableArray(x,y)){
                        pickStatusClose(rootview,x,y);
                    }
                }
            }
        };*/

        for(int i=0;i<6;i++){
            for (int j=0;j<6;j++){
                land[i][j].setDrawingCacheEnabled(true);
                land[i][j].setOnTouchListener(changeColorListener);
            }
        }



        return rootview;
    }

    public static void setCurrentShopID(String value){
        currentShopId = value;
    }
    public static void pickStatusOpen(Boolean isBig){

        Boolean[][] table = landProp.scanAvailableLand(isBig);
        landProp.setIsOpen(true);

        //Highlight pickable blocks
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                if (table[i][j]){
                    land[i][j].setColorFilter(brightIt(100));
                }
            }
        }
    }

    public void pickStatusClose(View rootview, int x, int y, Boolean placed){
        int archcount=0;
        int emptyslot=0;

        landProp.setIsOpen(false);

        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                    land[i][j].clearColorFilter();
            }
        }
        if(placed) {
            //Sync with server
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    //Failed to connect to server
                    Log.e("SQL", "Failed connecting to server");
                } else {

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Island WHERE ownerid='" + TravelBox.userId + "';");
                    if (rs.next()) {
                        //Get archcount
                        archcount = rs.getInt("archcount");

                        //find a empty slot
                        for (int i = 0; i < 9; i++) {
                            if (rs.getString("arch" + i) == null) {
                                emptyslot = i;
                                i = 9;
                            }
                        }
                        //Execute update
                        String query = "UPDATE Island SET archcount=" + (archcount + 1) + ", arch" + emptyslot + "='" + currentShopId + "', archposx" + emptyslot + "=" + x + ", archposy" + emptyslot + "=" + y + " WHERE ownerid='" + TravelBox.userId + "';";
                        Log.d("SQL", query);
                        stmt.executeUpdate(query);
                    }
                }
            } catch (Exception ex) {
                Log.e("SQL", "pickStatusClose: " + ex.toString());
            }

            //Reload land status
            Boolean[][] isHead = landProp.initLands(rootview);
            initShowAllArch(isHead);
            adapter.updateData(UserIslandListData.getData(getContext()));
        }
    }

    public void initShowAllArch(Boolean[][] isHead){
        scoreAmount = 0;
        for (int i=5; i>=0; i--){
            for (int j=5; j>=0; j--){
                if (isHead[i][j]){
                    showThisArch(i, j, getArchBundle(landProp.getContent(i, j)));
                }
            }
        }
        //Update Harvest Time
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                //Failed to connect to server
                Log.e("SQL", "Unable to connect to server");
            } else {
                //Get arch data from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT lastharvest FROM Island WHERE ownerid='" + TravelBox.userId + "';");
                if (rs.next()) {
                    lastHarvest = rs.getLong("lastharvest");
                }
            }
        }catch (Exception ex){
            Log.e("SQL", "Update harvest time: "+ex.toString());
        }
        StatusFragment.setScore(scoreAmount);
        updateHarvestStatus();
    }

    private void deleteArch(ArchBundle archBundle, View rootview){
        try{
            Connection con = connectionClass.CONN();
            if (con == null){
                //Failed to connect to server
                Log.e("SQL", "Unable to connect to server");
            } else {
                //Get arch data from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Island WHERE ownerid='" + TravelBox.userId + "';");
                if (rs.next()) {
                    for(int i=0; i<9; i++){
                        if (rs.getString("arch" + i)!=null) {
                            if (rs.getString("arch" + i).endsWith(archBundle.shopid)) {
                                //Wipe arch drawable
                                Log.d("MyIsland", "WipeArch: "+((rs.getInt("archposx"+i)+1)*10+rs.getInt("archposy"+i)+1));
                                iv[(rs.getInt("archposx"+i)+1)*10+rs.getInt("archposy"+i)+1].setImageDrawable(null);
                                //Wipe arch data
                                String query = "UPDATE Island SET arch" + i + "=null, archposx" + i + "=null, archposy" + i + "=null, archcount=" + (rs.getInt("archcount") - 1) + " WHERE ownerid='" + TravelBox.userId + "';";
                                stmt.executeUpdate(query);
                                Log.d("MyIsland", "deleteArch: " + query);
                                i = 9;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex){
            Log.e("SQL", "deleteArch: "+ex.toString());
        }
        Boolean[][] isHead= landProp.initLands(rootview);
        initShowAllArch(isHead);
        adapter.updateData(UserIslandListData.getData(getContext()));
    }

    private ArchBundle getArchBundle(String shopid) {
        ArchBundle archbundle = new ArchBundle();
        try {
            //Get arch image url from server
            Connection con = connectionClass.CONN();
            Statement stmt = con.createStatement();
            //Determine what level does the user have
            ResultSet rs = stmt.executeQuery("SELECT archlv FROM UserArch WHERE ownerid='"+ TravelBox.userId +"' AND arch='"+ shopid +"';");
            if(rs.next()){
            archbundle.level = rs.getInt("archlv");
            }
            //Fetch the target level image url and name of arch
            ResultSet rs2 = stmt.executeQuery("SELECT archlv"+archbundle.level+",name,archtype FROM ShopInfo WHERE shopid='"+ shopid +"';");
            if(rs2.next()){
                archbundle.url = rs2.getString("archlv"+archbundle.level);
                archbundle.name = rs2.getString("name");
                archbundle.type = rs2.getString("archtype");
            }

        } catch (Exception ex){
            Log.e("SQL", "getArchBundle: " + ex.toString());
        }
        archbundle.shopid = shopid;
        return archbundle;
    }

    private String getThumbURL(String shopid,int level) {
        String url="";
        try {
            //Get arch image url from server
            Connection con = connectionClass.CONN();
            Statement stmt = con.createStatement();

            //Fetch the target level image url of arch
            ResultSet rs2 = stmt.executeQuery("SELECT thumblv"+level+" FROM ShopInfo WHERE shopid='"+ shopid +"';");
            if(rs2.next()){
                url = rs2.getString("thumblv"+level);
            }
        } catch (Exception ex){
            Log.e("SQL", "getThumbURL: " + ex.toString());
        }
        return url;
    }

    private void showThisArch(int x, int y, final ArchBundle arch) {
        x++;y++;
        //New ImageView
        iv[x*10+y]=new ImageView(getContext());
        Log.d("MyIsland", "showThisArch: "+(x*10+y));

        //margin data in dp
        double marginleft = 50;
        double marginbottom = 50;
        if (arch.type.endsWith("S")) {
            marginleft = 28.5 * y - 28.5 * x - 28.5;
            marginbottom = 16.5 * y + 16.5 * x + 16 - 16.5 * 3;
        } else if (arch.type.endsWith("B")) {
            marginleft = 28.5 * y - 28.5 * x - 28.5-14;
            marginbottom = 16.5 * y + 16.5 * x + 16 - 16.5 * 4;
        }

        //convert dp to pixel
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int mleftpixels = (int) (marginleft * scale + 0.5f);
        int mbottompixels = (int) (marginbottom * scale + 0.5f);
        int wh=100;
        if(arch.type.endsWith("S")) {
            wh = (int) (115 * scale + 0.5f);
        } else if (arch.type.endsWith("B")) {
            wh = (int) (144 * scale + 0.5f);
        }

        //Add layout roles

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh, wh);
        lp.gravity=0x01|0x50; //Gravity= Center Horizontal + Bottom
        lp.leftMargin= mleftpixels;
        lp.bottomMargin= mbottompixels;
        Log.d("Myisland", "New ImageView: "+marginleft+","+marginbottom);

        //Add view
        fl.addView(iv[x*10+y],lp);
        iv[x*10+y].setScaleType(ImageView.ScaleType.FIT_END);
        iv[x*10+y].setDrawingCacheEnabled(true);
        ImageLoader.getInstance().displayImage(arch.url,iv[x*10+y]);
        /*iv[x*10+y].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("MyislandIsland", "onLongClick: "+arch.shopid);
                showArchDetail(arch, view);
                return true;
            }
        });*/
        iv[x*10+y].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
                    if (motionEvent.getX() < bmp.getWidth() && motionEvent.getY() < bmp.getHeight() && motionEvent.getX() >= 0 && motionEvent.getY() >= 0) {
                        int color = bmp.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                        if (color == Color.TRANSPARENT)
                            return false;
                        else {
                            if (landProp.getIsOpen() == false) {
                                showArchDetail(arch, view);
                            }
                        }
                        return true;

                }
                return false;
            }
        });

        //Add to scoreAmount
        scoreAmount += (15 + (arch.level*5));
        //Level 5 bonus
        if (arch.level == 5){
            scoreAmount += 10;
        }

    }

    private void showArchDetail(ArchBundle arch,View view){
        archDetail.setVisibility(View.VISIBLE);
        activeDetailArch = (ImageView)view;
        for(int i=1;i<5;i++){
            ImageLoader.getInstance().displayImage(getThumbURL(arch.shopid,i), archDetailImg[i-1]);
            archDetailImg[i-1].clearColorFilter();
            if(i>arch.level){
                archDetailImg[i-1].setColorFilter(brightIt(255));
            }
        }
        if (arch.level==5){
            final float scale = getContext().getResources().getDisplayMetrics().density;
            btnLock.getLayoutParams().height = (int) (86 * scale + 0.5f);
            btnLock.getLayoutParams().width = (int) (86 * scale + 0.5f);
            ImageLoader.getInstance().displayImage(getThumbURL(arch.shopid,5),btnLock);
        } else {
            btnLock.setImageResource(R.mipmap.houseinfo_locked);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.BELOW,R.id.myisland_detail_lv4);
            btnLock.setLayoutParams(lp);
        }
        archDetailName.setText(arch.name);
        activebundle = arch;
    }

    public static ColorMatrixColorFilter brightIt(int fb) {
        //USE:  TargetImageView.setColorFilter(brightIt(100));
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[] {
                1, 0, 0, 0, fb,
                0, 1, 0, 0, fb,
                0, 0, 1, 0, fb,
                0, 0, 0, 1, 0   });

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(cmB);
        //Canvas c = new Canvas(b2);
        //Paint paint = new Paint();
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
        //paint.setColorFilter(f);
        return f;
    }

    public final class ArchBundle {
        public String name;
        public String url;
        public String shopid;
        public int level;
        public String type;
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void shareScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        //intent.setDataAndType(uri, "image/*");
        intent.setType("image/*");
        //startActivity(intent);
        startActivity(Intent.createChooser(intent, "Share Image to:"));
    }

    private void updateHarvestStatus(){
        //Update harvest status
        if (System.currentTimeMillis()-lastHarvest>21600){
            harvestSeconds = 21600;
            setHarvestStatus(true);
        } else {
            harvestSeconds = System.currentTimeMillis() - lastHarvest;
            setHarvestStatus(false);
        }
        harvestProgress.setProgress(((int) harvestSeconds));
    }

    private void setHarvestStatus(Boolean isOpen){
        if (isOpen){
            harvest_glow.setVisibility(View.VISIBLE);
            harvest_glow.startAnimation(glow);
        } else {
            harvest_glow.setVisibility(View.GONE);
            harvest_glow.clearAnimation();
        }
    }

    private void doHarvest(){

        int harvestAmount = (int) (scoreAmount*scoreAmount)/(scoreAmount*20);
        StatusFragment.coinPaySave(true, harvestAmount);

        //Update lastHarvest
        try{
            Connection con = connectionClass.CONN();
            if (con == null) {
                //Failed to connect to server
                Log.e("SQL", "Unable to connect to server");
            } else {
                //TODO: Uncommand in release version
                /*Statement stmt = con.createStatement();
                lastHarvest = System.currentTimeMillis();
                stmt.executeUpdate("UPDATE Island SET lastharvest="+lastHarvest+" WHERE ownerid='" + TravelBox.userId + "';");*/
            }
        } catch (Exception ex){
            Log.e("Myisland", "doHarvest: "+ex.toString());
        }
        updateHarvestStatus();
    }

    private void unlockLvFive(String shopid){
        try{
            Connection con = connectionClass.CONN();
            if (con == null) {
                //Failed to connect to server
                Log.e("SQL", "Unable to connect to server");
            } else {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE UserArch SET archlv=5 WHERE ownerid='" + TravelBox.userId + "' AND arch='"+shopid+"';");
            }
        } catch (Exception ex){
            Log.e("Myisland", "unlockLvFive: "+ex.toString());
        }
        StatusFragment.coinPaySave(false,50);
    }

    public static Boolean getLandPropIsOpen(){
        return landProp.getIsOpen();
    }
}
