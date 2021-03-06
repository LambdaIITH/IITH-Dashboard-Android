package com.lambda.iith.dashboard.LostFound;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

import Adapters.ViewPagerAdapter;

public class Main2Activity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        //        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            }
//        });


        /*ImageView home=(ImageView) view.findViewById(R.id.back_id);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(homeIntent);
            }
        });*/

        tabLayout = findViewById(R.id.tablayout_id);
//        appBarLayout =(AppBarLayout) findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Adding fragments
        adapter.AddFragment(new FragmentQuiz(), "LOST");
        adapter.AddFragment(new FragmentExplore(), "FOUND");
        // adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {

        Intent homeIntent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(homeIntent);
        return true;
    }
}

