package com.lambda.iith.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class SkipLogin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        setSupportActionBar(toolbar);
        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
        toolbar.setTitle("Mess Menu");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationSL);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toolbar toolbar = findViewById(R.id.toolbar);


                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()) {
                    case R.id.sl_mess: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                        toolbar.setTitle("Mess Menu");
                        return true;
                    }

                    case R.id.sl_bus: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new FragmentBS()).commit();
                        toolbar.setTitle("Bus Schedule");
                        return true;
                    }


                }
                return false;

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
