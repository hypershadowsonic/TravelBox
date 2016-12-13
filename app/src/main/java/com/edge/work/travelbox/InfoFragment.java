package com.edge.work.travelbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;


public class InfoFragment extends Fragment implements OnMapReadyCallback{

    private ImageView btn_fb;
    private MapView mapView;
    private GoogleMap gMap;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_info, container, false);
        btn_fb = (ImageView)rootview.findViewById(R.id.info_btn_fb);
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ottocoffe/"));
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
        markerOptions.position(new LatLng(23.7082459, 120.5374783));
        markerOptions.title("凹凸咖啡館");
        markerOptions.visible(true);
        markerOptions.anchor(0.5f,0.5f);

        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.addMarker(markerOptions);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.7082459, 120.5374783), 17.0f));
        //gMap.;
    }
}
