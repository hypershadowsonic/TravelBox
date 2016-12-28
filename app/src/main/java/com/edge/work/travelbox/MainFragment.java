package com.edge.work.travelbox;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.google.android.gms.internal.zzs.TAG;


public class MainFragment extends Fragment {
    private CardView yunlin;
    private FragmentManager fm;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_main, container, false);
        fm=getFragmentManager();

        yunlin=(CardView)rootview.findViewById(R.id.main_card_yunlin);
        yunlin.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Log.i(TAG, "onClick: Yunlin");
                fm.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_container, new ListAssemblyFragment())
                        .commit();
            }
        });

        return rootview;
    }
}
