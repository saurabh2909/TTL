package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 3/29/2017.
 */

public class Tablayout_with_viewpager extends android.support.v4.app.Fragment {
    public ViewPager viewPager;
    public TabLayout tabLayout;



    Bundle savedInstanceState;

    int lastTabPosition;

    public int getLastTabPosition() {
        return lastTabPosition;
    }

    public void setLastTabPosition(int lastTabPosition) {
        this.lastTabPosition = lastTabPosition;
    }

    int roleId;
    int status;

    public Tablayout_with_viewpager(String fromDesiredTutor) {
        this.fromDesiredTutor = 1;
    }

    public Tablayout_with_viewpager(int status, int roleId) {

        this.status = status;
        this.roleId = roleId;


    }

    public Tablayout_with_viewpager(int roleId) {
        this.roleId = roleId;
    }

    public Tablayout_with_viewpager() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    View convertView;
    int fromDesiredTutor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.tab_layout_and_viewpager, null);


        if (savedInstanceState == null) {


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("roleAndStatus", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("roleId", roleId);
            editor.putInt("status", status);
            editor.apply();


        }

        initScreen();
        return convertView;

    }

    public void initScreen() {




        SharedPreferences pref = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        status = pref.getInt("status", 1);
        if (status == 1) roleId = 1;
        else roleId = pref.getInt("roleId", 0);

//        Toast.makeText(getContext(), "status "+status, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "roleId "+roleId, Toast.LENGTH_SHORT).show();
        tabLayout = (TabLayout) convertView.findViewById(R.id.tabX);

        TabBackStack tabBackStack=TabBackStack.getInstance();


        viewPager = (ViewPager) convertView.findViewById(R.id.viewpagerTabLayout);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabBackStack.setClassName("class Tablayout_with_viewpager");

        /*    if (status == 0) {
            if (roleId == 0 || fromDesiredTutor == 1) {


                tabLayout.getTabAt(0).setIcon(R.drawable.tabuser);
                tabLayout.getTabAt(1).setIcon(R.drawable.tabvideo);


            }
        }   */

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));
        tabLayout.getTabAt(tabBackStack.getTabPosition()).select();

    }


    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("roleAndStatus", 0);
        sharedPreferences.contains("roleId");
        sharedPreferences.contains("status");
        int st = sharedPreferences.getInt("status", 1);
        int role = sharedPreferences.getInt("roleId", 0);


      /*  if (status == 0) {
            if (roleId == 0) {
                adapter.addFragment(new Available_tutor(), "");
                adapter.addFragment(new VideoList(), "");
                viewPager.setAdapter(adapter);
            }
            if (roleId == 1 || roleId == 2 || roleId == 3) {
                if (fromDesiredTutor == 1) {
                    adapter.addFragment(new Available_tutor(), "");
                    adapter.addFragment(new VideoList(), "");
                    viewPager.setAdapter(adapter);
                } else {*/
                    adapter.addFragment(new MyDetailsB(), "My Details");
                    adapter.addFragment(new Biography(), "Biography");
                    viewPager.setAdapter(adapter);
                /*}
            }*/
       /* }
        if (status == 1) {
            adapter.addFragment(new MyDetailsB(), "My Details");
            adapter.addFragment(new Biography(), "Biography");
            viewPager.setAdapter(adapter);
        }*/


    }


    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
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
