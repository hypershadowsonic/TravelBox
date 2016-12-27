package com.edge.work.travelbox;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MyislandIslandFragment extends Fragment {

    private static ImageView[][] land = new ImageView[6][6];
    private static Land landProp = new Land();
    private View.OnClickListener mListener;
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
    private ImageView archDetailClose, archDetailInfo, archDetailDelete, activeDetailArch;
    private ArchBundle activebundle;


    public MyislandIslandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_myisland_island, container, false);
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
        adapter = new UserIslandListAdapter(this.getActivity().getBaseContext(), UserIslandListData.getData(getContext()));
        recyclerView.setAdapter(adapter);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(llm);


        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Character.getNumericValue(view.getTag().toString().charAt(0));
                int y = Character.getNumericValue(view.getTag().toString().charAt(1));

                Log.d("Land", "onClick: "+x+","+y);
                if(landProp.getIsOpen()) {
                    if (landProp.getAvailableArray(x,y)){
                        //showThisArch(x,y, getArchBundle(currentShopId));
                        pickStatusClose(rootview,x,y);
                    }
                }
            }
        };

        for(int i=0;i<6;i++){
            for (int j=0;j<6;j++){
                land[i][j].setOnClickListener(mListener);
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

    public void pickStatusClose(View rootview, int x, int y){
        int archcount=0;
        int emptyslot=0;

        landProp.setIsOpen(false);

        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                    land[i][j].clearColorFilter();
            }
        }
        //Sync with server
        try{
            Connection con = connectionClass.CONN();
            if (con == null){
                //Failed to connect to server
                Log.e("SQL", "Failed connecting to server");
            } else {

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Island WHERE ownerid='"+ TravelBox.userId +"';");
                if(rs.next()){
                    //Get archcount
                    archcount = rs.getInt("archcount");

                    //find a empty slot
                    for(int i=0;i<9;i++){
                        if(rs.getString("arch"+i)==null){
                            emptyslot=i;
                            i=9;
                        }
                    }
                    //Execute update
                    String query = "UPDATE Island SET archcount="+(archcount+1)+", arch"+emptyslot+"='"+currentShopId+"', archposx"+emptyslot+"="+x+", archposy"+emptyslot+"="+y+" WHERE ownerid="+TravelBox.userId+";";
                    Log.d("SQL", query);
                    stmt.executeUpdate(query);
                }
            }
        } catch (Exception ex){
            Log.e("SQL", "pickStatusClose: "+ex.toString());
        }

        //Reload land status
        Boolean[][] isHead = landProp.initLands(rootview);
        initShowAllArch(isHead);
    }

    public void initShowAllArch(Boolean[][] isHead){
        for (int i=5; i>=0; i--){
            for (int j=5; j>=0; j--){
                if (isHead[i][j]){
                    showThisArch(i, j, getArchBundle(landProp.getContent(i, j)));
                }
            }
        }
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
                ResultSet rs = stmt.executeQuery("SELECT arch0, arch1, arch2, arch3, arch4, arch5, arch6, arch7, arch8 FROM Island WHERE ownerid='" + TravelBox.userId + "';");
                if (rs.next()) {
                    for(int i=0; i<9; i++){
                        if(rs.getString("arch"+i).equals(archBundle.shopid)){
                            //Wipe arch data
                            stmt.executeUpdate("UPDATE Island SET arch"+i+"=null, archposx"+i+"=null, archposy"+i+"=null;");
                            i=9;
                        }
                    }
                }
            }
        } catch (Exception ex){
            Log.e("SQL", "deleteArch: "+ex.toString());
        }
        Boolean[][] isHead= landProp.initLands(rootview);
        initShowAllArch(isHead);
    }

    private ArchBundle getArchBundle(String shopid) {
        ArchBundle archbundle = new ArchBundle();
        String url="";
        try {
            //Get arch image url from server
            Connection con = connectionClass.CONN();
            Statement stmt = con.createStatement();
            //Determine what level does the user have
            int lv=0;
            ResultSet rs = stmt.executeQuery("SELECT archlv FROM UserArch WHERE ownerid='"+ TravelBox.userId +"' AND arch='"+ shopid +"';");
            if(rs.next()){
            lv = rs.getInt("archlv");
            }
            //Fetch the target level image url and name of arch
            ResultSet rs2 = stmt.executeQuery("SELECT archlv"+lv+",name FROM ShopInfo WHERE shopid='"+ shopid +"';");
            if(rs2.next()){
                archbundle.url = rs2.getString("archlv"+lv);
                archbundle.name = rs2.getString("name");
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
        //margin data in dp
        double marginleft=28.5*y-28.5*x-28.5;
        double marginbottom=16.5*y+16.5*x+16-16.5*3;
        //convert dp to pixel
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int mleftpixels = (int) (marginleft * scale + 0.5f);
        int mbottompixels = (int) (marginbottom * scale + 0.5f);
        int wh = (int) (115 * scale + 0.5f);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wh, wh);
        lp.gravity=0x01|0x50; //Gravity= Center Horizontal + Bottom
        lp.leftMargin= mleftpixels;
        lp.bottomMargin= mbottompixels;
        Log.d("Myisland", "New ImageView: "+marginleft+","+marginbottom);
        fl.addView(iv[x*10+y],lp);
        iv[x*10+y].setScaleType(ImageView.ScaleType.FIT_END);
        ImageLoader.getInstance().displayImage(arch.url,iv[x*10+y]);
        iv[x*10+y].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                archDetail.setVisibility(View.VISIBLE);
                activeDetailArch = (ImageView)view;
                for(int i=1;i<5;i++){
                    ImageLoader.getInstance().displayImage(getThumbURL(arch.shopid,i), archDetailImg[i-1]);
                }
                archDetailName.setText(arch.name);
                activebundle = arch;
                return true;
            }
        });
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
    }
}
