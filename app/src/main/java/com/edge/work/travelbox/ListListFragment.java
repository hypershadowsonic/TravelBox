package com.edge.work.travelbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ListListFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    private Spinner spinner;
    private ArrayAdapter locationList;
    private String[] location={"斗六市", "斗南鎮", "西螺鎮", "虎尾鎮", "土庫鎮", "北港鎮"};
    private FragmentManager fm;
    private RecyclerView list;
    private ListAdapter adapter;

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


        fm=getParentFragment().getFragmentManager();
        list = (RecyclerView)rootview.findViewById(R.id.list_recyclerview);
        adapter = new ListAdapter(getActivity(),ListData.getData(getContext(),"P"),fm);
        list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        list.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.swap(getCharForNumber(i+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootview;
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 'a' - 1)) : null;
    }
}
