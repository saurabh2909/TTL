package com.example.saubhagyam.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 10/06/2017.
 */

public class Student_Tab_Layout extends android.support.v4.app.Fragment {
    public ViewPager viewPager;
    public TabLayout tabLayout;

    View convertView;
    int lastTabPosition;

    public int getLastTabPosition() {
        return lastTabPosition;
    }

    public void setLastTabPosition(int lastTabPosition) {
        this.lastTabPosition = lastTabPosition;
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.student_tab_layout, null);
        initScreen();
        return convertView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


        }
    }


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TabBackStack tabBackStack;
    int roleId;
    int status;
    public void initScreen() {






        pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        editor = pref.edit();
        if (status == 1) {

            roleId = 1;
        } else roleId = pref.getInt("roleId", 0);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
    /*    if (roleId == 0) {
            View view = toolbar.getRootView();
            view.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

        } else if (roleId == 1 || roleId == 2 || roleId == 3) {
            View view = toolbar.getRootView();
            view.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
            view.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);


        }*/

        if (roleId==0){
            FragmentStack fragmentStack = FragmentStack.getInstance();
//            fragmentStack.setSize(0);
        }

        tabBackStack = TabBackStack.getInstance();
        tabBackStack.setClassName("class Student_Tab_Layout");


//        Toast.makeText(getContext(), "status "+status, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "roleId "+roleId, Toast.LENGTH_SHORT).show();
        tabLayout = (TabLayout) convertView.findViewById(R.id.student_tabX);


        viewPager = (ViewPager) convertView.findViewById(R.id.student_viewpagerTabLayout);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.tabuser);
        tabLayout.getTabAt(1).setIcon(R.drawable.tabvideo);


        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));
        tabLayout.getTabAt(tabBackStack.getTabPosition()).select();


    }
    private Bundle mMainFragmentArgs;

    public void saveMainFragmentState(Bundle args) {
        mMainFragmentArgs = args;
    }

    public Bundle getSavedMainFragmentState() {
        return mMainFragmentArgs;
    }

    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);


       /* if (status == 0) {
            if (roleId == 0) {*/
        adapter.addFragment(new Available_tutor(), "");
        adapter.addFragment(new VideoList(), "");
        viewPager.setAdapter(adapter);
          /*  }
            if (roleId == 1 || roleId == 2 || roleId == 3) {
                if (fromDesiredTutor == 1) {
                    adapter.addFragment(new Available_tutor(), "");
                    adapter.addFragment(new VideoList(), "");
                    viewPager.setAdapter(adapter);
                } else {
                    adapter.addFragment(new MyDetailsB(), "My Details");
                    adapter.addFragment(new Biography(), "Biography");
                    viewPager.setAdapter(adapter);
                }
            }
        }
        if (status == 1) {
            adapter.addFragment(new MyDetailsB(), "My Details");
            adapter.addFragment(new Biography(), "Biography");
            viewPager.setAdapter(adapter);
        }*/


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


