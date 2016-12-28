package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class InfoFragment extends Fragment implements OnMapReadyCallback{

    private String shopid;
    private ImageView btn_fb, title;
    private ImageView[] gallery = new ImageView[6];
    private TextView shopname, address, description, idea, time, phone, price, popularity;
    private MapView mapView;
    private GoogleMap gMap;
    private ConnectionClass connectionClass = new ConnectionClass();
    private float lat,lng;
    private String name,url;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_info, container, false);
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

        Bundle bundle = this.getArguments();
        if(bundle != null){
            shopid = bundle.getString("shopid");
            Log.d("Bundle", "onCreateView: "+shopid);
        }
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                //Failed to connect to server
                Log.e("SQL", "Unable to connect to server");
            } else {
                //Get arch data from server
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM ShopInfo WHERE shopid='" + shopid + "';");
                if (rs.next()) {
                    lat = rs.getFloat("latitude");
                    Log.d("Info", "Got latitude");
                    lng = rs.getFloat("longitude");
                    Log.d("Info", "Got longitude");
                    name = rs.getString("name");
                    Log.d("Info", "Got name");
                    url = rs.getString("facebookurl");
                    Log.d("Info", "Got facebookurl");
                    shopname.setText(rs.getString("name"));
                    Log.d("Info", "Got name");
                    address.setText(rs.getString("address"));
                    Log.d("Info", "Got address");
                    description.setText(rs.getString("description").replace("\\n","\n"));
                    Log.d("Info", "Got description");
                    idea.setText(rs.getString("idea").replace("\\n","\n"));
                    Log.d("Info", "Got idea");
                    time.setText(rs.getString("buisnesshour").replace("\\n","\n"));
                    Log.d("Info", "Got businesshour");
                    phone.setText(rs.getString("phone"));
                    Log.d("Info", "Got phone");
                    price.setText(rs.getString("price"));
                    Log.d("Info", "Got price");
                    popularity.setText(""+rs.getInt("popularity"));
                    Log.d("Info", "Got popularity");

                    title.setImageDrawable(null);
                    ImageLoader.getInstance().displayImage(rs.getString("titleimg"),title);
                    Log.d("Info", "Got title");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery1"),gallery[0]);
                    Log.d("Info", "Got gallery1");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery2"),gallery[1]);
                    Log.d("Info", "Got gallery2");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery3"),gallery[2]);
                    Log.d("Info", "Got gallery3");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery4"),gallery[3]);
                    Log.d("Info", "Got gallery4");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery5"),gallery[4]);
                    Log.d("Info", "Got gallery5");
                    ImageLoader.getInstance().displayImage(rs.getString("gallery6"),gallery[5]);
                    Log.d("Info", "Got gallery6");
                }
            }
        } catch (Exception ex){
            Log.e("SQL", ex.toString() );
        }


        btn_fb = (ImageView)rootview.findViewById(R.id.info_btn_fb);
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        mapView=(MapView)rootview.findViewById(R.id.info_mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);



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
