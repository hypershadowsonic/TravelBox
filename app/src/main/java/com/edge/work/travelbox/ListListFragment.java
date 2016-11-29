package com.edge.work.travelbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ListListFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    private Spinner spinner;
    private ArrayAdapter locationList;
    private String[] location={"斗六市", "斗南鎮", "西螺鎮", "虎尾鎮", "土庫鎮", "北港鎮"};
    private CardView cv01;
    private FragmentManager fm;

    public ListListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_list_list, container, false);
        Context context = container.getContext();
        spinner=(Spinner)rootview.findViewById(R.id.list_spinner);
        locationList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,location);
        spinner.setAdapter(locationList);
        cv01=(CardView)rootview.findViewById(R.id.list_card_01);
        fm=getParentFragment().getFragmentManager();

        cv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.main_container, new InfoFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });

        return rootview;
    }

}
