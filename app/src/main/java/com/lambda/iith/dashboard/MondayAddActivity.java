package com.lambda.iith.dashboard;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MondayAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mondayadd);

        Button home=(Button) findViewById(R.id.add_course);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new Monday()).commit();

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {

        Intent homeIntent = new Intent(MondayAddActivity.this,Monday.class);
        startActivity(homeIntent);
        return true;
    }
}
