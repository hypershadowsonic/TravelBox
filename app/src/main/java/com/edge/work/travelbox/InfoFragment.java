package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class InfoFragment extends Fragment implements OnMapReadyCallback{

    private String shopid,category;
    private ImageView btn_fb, title, artthumb1,artthumb2;
    private ImageView[] gallery = new ImageView[6];
    private TextView shopname, address, description, idea, time, phone, price, popularity, arttitle1,arttitle2;
    private RelativeLayout art1,art2;
    private MapView mapView;
    private GoogleMap gMap;
//    private ConnectionClass connectionClass = new ConnectionClass();
    private float lat,lng;
    private String name,url;
    private View rootview;
    private CardView articles;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            shopid = bundle.getString("shopid");
            category = bundle.getString("category");
            Log.d("Bundle", "onCreateView: "+shopid+","+category);
        }
        if (category.endsWith("x")) {
            rootview = inflater.inflate(R.layout.fragment_info, container, false);
            title = (ImageView) rootview.findViewById(R.id.info_img_title);
            gallery[0] = (ImageView) rootview.findViewById(R.id.info_gal_1);
            gallery[1] = (ImageView) rootview.findViewById(R.id.info_gal_2);
            gallery[2] = (ImageView) rootview.findViewById(R.id.info_gal_3);
            gallery[3] = (ImageView) rootview.findViewById(R.id.info_gal_4);
            gallery[4] = (ImageView) rootview.findViewById(R.id.info_gal_5);
            gallery[5] = (ImageView) rootview.findViewById(R.id.info_gal_6);
            shopname = (TextView) rootview.findViewById(R.id.info_text_name);
            address = (TextView) rootview.findViewById(R.id.info_text_address);
            description = (TextView) rootview.findViewById(R.id.info_text_description);
            idea = (TextView) rootview.findViewById(R.id.info_text_idea);
            time = (TextView) rootview.findViewById(R.id.info_text_time);
            phone = (TextView) rootview.findViewById(R.id.info_text_phone);
            price = (TextView) rootview.findViewById(R.id.info_text_price);
            popularity = (TextView) rootview.findViewById(R.id.info_text_popularity);
            art1 = (RelativeLayout) rootview.findViewById(R.id.info_news1);
            art2 = (RelativeLayout) rootview.findViewById(R.id.info_news2);
            artthumb1 = (ImageView) rootview.findViewById(R.id.info_img_news1);
            artthumb2 = (ImageView) rootview.findViewById(R.id.info_img_news2);
            arttitle1 = (TextView) rootview.findViewById(R.id.info_text_news1);
            arttitle2 = (TextView) rootview.findViewById(R.id.info_text_news2);
            articles = (CardView) rootview.findViewById(R.id.info_news);

            try {
                SQLiteDatabase sqLiteDatabase = getContext().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ShopInfo WHERE shopid='" + shopid + "';",null);
                if(cursor.moveToNext()){
                    lat = cursor.getFloat(cursor.getColumnIndex("latitude"));
                    lng = cursor.getFloat(cursor.getColumnIndex("longitude"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    url = cursor.getString(cursor.getColumnIndex("facebookurl"));
                    shopname.setText(name);
                    address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    description.setText(cursor.getString(cursor.getColumnIndex("description")).replace("\\n", "\n"));
                    idea.setText(cursor.getString(cursor.getColumnIndex("idea")).replace("\\n", "\n"));
                    time.setText(cursor.getString(cursor.getColumnIndex("businesshour")).replace("\\n", "\n"));
                    phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
                    price.setText(cursor.getString(cursor.getColumnIndex("price")));
                    popularity.setText("" + cursor.getInt(cursor.getColumnIndex("popularity")));

                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("titleimg")), title);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery1")), gallery[0]);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery2")), gallery[1]);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery3")), gallery[2]);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery4")), gallery[3]);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery5")), gallery[4]);
                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("gallery6")), gallery[5]);
                }
                cursor.close();
                Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM Articles WHERE shopid='" + shopid + "';", null);
                if (cursor2.moveToNext()){
                    ImageLoader.getInstance().displayImage(cursor2.getString(cursor2.getColumnIndex("thumb")),artthumb1);
                    arttitle1.setText(cursor2.getString(cursor2.getColumnIndex("title")));
                    final String arturl = cursor2.getString(cursor2.getColumnIndex("url"));
                    art1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arturl));
                            startActivity(browserIntent);
                        }
                    });
                } else {
                    articles.setVisibility(View.GONE);
                    Log.d("Articles", "Set GONE");
                }
                if (cursor2.moveToNext()){
                    ImageLoader.getInstance().displayImage(cursor2.getString(cursor2.getColumnIndex("thumb")),artthumb2);
                    arttitle2.setText(cursor2.getString(cursor2.getColumnIndex("title")));
                    final String arturl = cursor2.getString(cursor2.getColumnIndex("url"));
                    art2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arturl));
                            startActivity(browserIntent);
                        }
                    });
                } else {
                    art2.setVisibility(View.GONE);
                }
                cursor2.close();
                sqLiteDatabase.close();
            } catch (Exception ex) {
                Log.e("SQL", ex.toString());
            }

            btn_fb = (ImageView) rootview.findViewById(R.id.info_btn_fb);

            mapView = (MapView) rootview.findViewById(R.id.info_mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        } else {
            rootview = inflater.inflate(R.layout.layout_placeinfo, container, false);
            shopname = (TextView) rootview.findViewById(R.id.place_name);
            address = (TextView) rootview.findViewById(R.id.place_address);
            description = (TextView) rootview.findViewById(R.id.place_description);
            time = (TextView) rootview.findViewById(R.id.place_time);
            title = (ImageView) rootview.findViewById(R.id.place_title);
            btn_fb = (ImageView) rootview.findViewById(R.id.place_facebook);

            try {
                SQLiteDatabase sqLiteDatabase = getContext().openOrCreateDatabase("Local_Data.db", MODE_PRIVATE, null);
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PlaceInfo WHERE placeid='" + shopid + "';",null);
                if (cursor.moveToNext()){
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    url = cursor.getString(cursor.getColumnIndex("facebookurl"));
                    shopname.setText(cursor.getString(cursor.getColumnIndex("name")));
                    address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    description.setText(cursor.getString(cursor.getColumnIndex("description")).replace("\\n", "\n"));
                    time.setText(cursor.getString(cursor.getColumnIndex("businesshour")).replace("\\n", "\n"));

                    ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex("titleimg")), title);
                }
                cursor.close();
                sqLiteDatabase.close();
            } catch (Exception ex) {
                Log.e("SQL", ex.toString());
            }
        }
        if(url!=null){
            btn_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        } else {
            btn_fb.setVisibility(View.GONE);
        }

        return rootview;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap=googleMap;
        setUpMap();
    }

    public void setUpMap(){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.title(name);
        markerOptions.visible(true);
        markerOptions.anchor(0.5f,0.5f);

        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.addMarker(markerOptions);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17.0f));
        //gMap.;
    }
}
