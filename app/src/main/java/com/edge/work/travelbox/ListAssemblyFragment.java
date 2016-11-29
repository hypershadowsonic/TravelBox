package com.edge.work.travelbox;

import android.content.Context;
import android.net.Uri;
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


public class ListAssemblyFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;


    public ListAssemblyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tab_assembly, container, false);
        mViewPager=(ViewPager)rootview.findViewById(R.id.asm_viewpager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
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

        mTabLayout=(TabLayout)rootview.findViewById(R.id.asm_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        return rootview;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ListListFragment(), "Location");
        adapter.addFragment(new ListNewFragment(), "New");

        viewPager.setAdapter(adapter);
        viewPager.setId(R.id.main_container);
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
    }

}
