package com.edge.work.travelbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class ListNewFragment extends Fragment {

    private RecyclerView collection;
    private CollectionTitleAdapter adapter;
    private FragmentManager fm;

    public ListNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_list_new, container, false);
        fm=getParentFragment().getFragmentManager();
        collection = (RecyclerView) rootview.findViewById(R.id.list_new_recyclerview);
        collection.setHasFixedSize(true);
        adapter = new CollectionTitleAdapter(getActivity(), CollectionTitleData.getData(getContext()),fm);
        collection.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        collection.setAdapter(adapter);
        return rootview;
    }

}
