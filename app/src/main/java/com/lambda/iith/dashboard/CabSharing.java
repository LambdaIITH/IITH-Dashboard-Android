package com.lambda.iith.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class CabSharing extends AppCompatActivity {
    private FloatingActionButton register , Cancel , Snooze;
    private  FloatingActionMenu floatingActionMenu;
    public int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_sharing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flag = 0;


        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu);
        register = (FloatingActionButton) findViewById(R.id.menu_item) ;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CabSharing.this , CabSharingRegister.class));
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
    }


