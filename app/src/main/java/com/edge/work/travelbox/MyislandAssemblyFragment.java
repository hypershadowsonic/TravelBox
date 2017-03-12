package com.edge.work.travelbox;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyislandAssemblyFragment extends Fragment {

    private int[] myislandNavIconId={
            R.mipmap.myisland_nav_myisland,
            R.mipmap.myisland_nav_friend,
            R.mipmap.myisland_nav_shop
    };
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;


    //private OnFragmentInteractionListener mListener;

    public MyislandAssemblyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        StatusFragment.myIslandStatus.setMyislandStatus(true);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tab_assembly, container, false);

        mViewPager=(ViewPager)rootview.findViewById(R.id.asm_viewpager);
        mTabLayout=(TabLayout)rootview.findViewById(R.id.asm_tabs);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.myisland_nav_myisland));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.myisland_nav_friend));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.mipmap.myisland_nav_shop));

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyislandIslandFragment());
        adapter.addFragment(new MyislandFriendFragment());
        adapter.addFragment(new MyislandShopFragment());

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                lastPosition=position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        setupViewPager(mViewPager);


        mTabLayout.setupWithViewPager(mViewPager);*/
        return rootview;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    /*private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyislandIslandFragment(), "MyIsland");
        adapter.addFragment(new MyislandFriendFragment(), "Friends");
        adapter.addFragment(new MyislandShopFragment(), "Shop");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/

    @Override
    public void onDetach() {
        StatusFragment.myIslandStatus.setMyislandStatus(false);
        super.onDetach();
    }
}
