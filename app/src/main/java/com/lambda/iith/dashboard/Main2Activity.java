package com.lambda.iith.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Main2Activity extends Fragment {


    private TabLayout tabLayout ;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_main2, container, false);   }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout =(TabLayout) view.findViewById(R.id.tablayout_id);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        // Adding fragments
        adapter.AddFragment(new FragmentQuiz(), "LOST");
        adapter.AddFragment(new FragmentExplore(), "FOUND");

        // adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }



}
