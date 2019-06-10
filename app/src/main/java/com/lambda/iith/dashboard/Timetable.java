package com.lambda.iith.dashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Timetable extends Fragment {


    private TabLayout tabLayout ;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.timetable, container, false);
        tabLayout =(TabLayout) view.findViewById(R.id.tablayouttime_id);
        viewPager = (ViewPager) view.findViewById(R.id.viewpagertime_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // Adding fragments
        adapter.AddFragment(new Monday(), "LOST");
        adapter.AddFragment(new FragmentExplore(), "FOUND");
        // adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }
}

