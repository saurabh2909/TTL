package com.example.saubhagyam.thetalklist;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TTL_Score extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ttl__score,container,false);

        tabLayout= (TabLayout) view.findViewById(R.id.tabz);

      /*  TabLayout.Tab tab=tabLayout.newTab().setText("Tutor");
        TabLayout.Tab tab1=tabLayout.newTab().setText("Student");*/

        viewPager = (ViewPager) view.findViewById(R.id.ttl_score_viewpager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        /*tabLayout.addTab(tab);
        tabLayout.addTab(tab1);*/

        TextView ttl_score= (TextView) view.findViewById(R.id.ttl_score_ttl);
        SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        ttl_score.setText(String.valueOf(pref.getFloat("ttl_points",0.0f)));

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));

        tabLayout.setSelected(true);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        TTL_Score.ViewPagerAdapter adapter = new TTL_Score.ViewPagerAdapter(fragmentManager);

                adapter.addFragment(new TTL_Score_Drawer_viewpager(), "Tutor");
                adapter.addFragment(new TTL_Score_Drawer_viewpager(), "Student");
                viewPager.setAdapter(adapter);

    }
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
